package com.lmello.titer.auth.service;

import com.lmello.titer.auth.api.AuthService;
import com.lmello.titer.auth.entities.UserAuth;
import com.lmello.titer.auth.enums.AuthProvider;
import com.lmello.titer.auth.exceptions.AccountDisabledException;
import com.lmello.titer.auth.exceptions.InvalidSocialTokenException;
import com.lmello.titer.auth.exceptions.InvalidTokenException;
import com.lmello.titer.auth.jwt.JwtService;
import com.lmello.titer.auth.repositories.UserAuthRepository;
import com.lmello.titer.auth.socialproviders.SocialIdentity;
import com.lmello.titer.auth.socialproviders.SocialTokenVerifierRegistry;
import com.lmello.titer.users.api.UserService;
import com.lmello.titer.users.api.command.CreateUserCommand;
import com.lmello.titer.users.api.representation.UserInfo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final UserAuthRepository userAuthRepository;
    private final SocialTokenVerifierRegistry verifierRegistry;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;
    private final PlatformTransactionManager transactionManager;

    private TransactionTemplate tx;
    private String dummyHash;

    @PostConstruct
    void init() {
        this.tx = new TransactionTemplate(transactionManager);
        this.dummyHash = passwordEncoder.encode("timing-attack-mitigation-placeholder");
    }

    @Transactional
    public Tokens registerLocal(
            String username,
            String email,
            String rawPassword,
            String firstName,
            String lastName
    ) {
        try {
            UserInfo user = userService.create(new CreateUserCommand(
                    username,
                    email,
                    firstName,
                    lastName,
                    false,
                    "SELF_REGISTRATION"
            ));

            userAuthRepository.save(UserAuth.local(user.id(), passwordEncoder.encode(rawPassword)));

            log.info("Local registration succeeded userId={}", user.id());

            return issueFor(user);
        } catch (DataIntegrityViolationException e) {
            log.warn("Registration conflict for email/username (race on unique index)");
            throw new InvalidSocialTokenException("Email or username is already in use");
        }
    }

    public Tokens login(String identifier, String rawPassword) {
        UserInfo user = userService.findByUsernameOrEmail(identifier).orElse(null);

        String hashToCheck = dummyHash;
        if (user != null) {
            Optional<UserAuth> credential = userAuthRepository
                    .findByUserIdAndProvider(user.id(), AuthProvider.LOCAL);

            if (credential.isPresent() && credential.get().getPasswordHash() != null) {
                hashToCheck = credential.get().getPasswordHash();
            }
        }

        boolean passwordOk = passwordEncoder.matches(rawPassword, hashToCheck);

        if (user == null || !passwordOk) {
            log.warn("Login failed (invalid credentials)");
            throw new BadCredentialsException("Invalid credentials");
        }

        ensureActive(user);

        log.info("Login succeeded userId={}", user.id());
        return issueFor(user);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public Tokens socialLogin(AuthProvider provider, String token) {
        SocialIdentity identity = verifierRegistry.get(provider).verify(token);

        // [1] Already linked -> re-login keyed on the provider's stable subject id
        var alreadyLinked = userAuthRepository.findByProviderAndProviderId(provider, identity.providerId());
        if (alreadyLinked.isPresent()) {
            return issueForExistingLink(provider, alreadyLinked.get().getUserId());
        }

        // [2] First sight of this identity: only trust the email if the provider verified it.
        if (!identity.emailVerified()) {
            log.warn("Social login rejected: provider has not verified the email provider={}", provider);
            throw new InvalidSocialTokenException("Provider has not verified this email address");
        }

        // [3] Normalize before lookup AND creation so they agree with the lower(email) unique index.
        String email = identity.email().trim().toLowerCase(Locale.ROOT);

        try {
            return tx.execute(status -> linkOrCreate(provider, identity, email));
        } catch (DataIntegrityViolationException race) {
            log.warn("Concurrent social login detected provider={}; retrying lookup", provider);

            Optional<UserAuth> nowLinked =
                    userAuthRepository.findByProviderAndProviderId(provider, identity.providerId());

            if (nowLinked.isPresent()) {
                return issueForExistingLink(provider, nowLinked.get().getUserId());
            }

            throw race;
        }
    }

    private Tokens linkOrCreate(AuthProvider provider, SocialIdentity identity, String email) {
        Optional<UserInfo> existing = userService.findByEmail(email);

        if (existing.isPresent()) {
            UserInfo user = existing.get();
            ensureActive(user);

            if (!user.isEmailVerified()) {
                log.warn("Social link blocked: existing account email not verified userId={}", user.id());
                throw new InvalidSocialTokenException("An unverified account already exists for this email; verify it before linking");
            }

            if (userAuthRepository.existsByUserIdAndProvider(user.id(), provider)) {
                log.warn("Social link conflict provider={} userId={}", provider, user.id());
                throw new InvalidSocialTokenException("This account is already linked to a different " + provider.name() + " identity");
            }

            userAuthRepository.save(UserAuth.social(user.id(), provider, identity.providerId()));
            log.info("Linked social provider={} to existing userId={}", provider, user.id());

            return issueFor(user);
        }

        UserInfo created = userService.create(new CreateUserCommand(
                null,
                email,
                identity.firstName(),
                identity.lastName(),
                true,
                provider.name()
        ));

        userAuthRepository.save(UserAuth.social(created.id(), provider, identity.providerId()));
        log.info("Created account from social provider={} userId={}", provider, created.id());

        return issueFor(created);
    }

    private Tokens issueForExistingLink(AuthProvider provider, UUID userId) {
        UserInfo user = userService.findById(userId)
                .orElseThrow(() -> new InvalidSocialTokenException("Linked account no longer exists"));

        ensureActive(user);

        log.info("Social re-login provider={} userId={}", provider, user.id());

        return issueFor(user);
    }

    public Tokens refresh(String rawRefreshToken) {
        RefreshTokenService.Rotation rotation = refreshTokenService.rotate(rawRefreshToken);

        UserInfo user = userService.findById(rotation.userId())
                .filter(UserInfo::isActive)
                .orElseThrow(() -> {
                    refreshTokenService.revokeAllForUser(rotation.userId());

                    log.warn("Refresh blocked: account unavailable/inactive userId={}", rotation.userId());
                    return new InvalidTokenException("Account is unavailable");
                });

        log.info("Access token refreshed userId={}", user.id());
        return new Tokens(
                jwtService.issueAccessToken(user),
                rotation.newToken(),
                jwtService.accessTokenTTLSeconds(),
                user
        );
    }

    public void logout(String rawRefreshToken) {
        refreshTokenService.revoke(rawRefreshToken);
    }

    public void logoutEverywhere(UUID userId) {
        refreshTokenService.revokeAllForUser(userId);
        log.info("Logout-everywhere requested userId={}", userId);
    }

    private void ensureActive(UserInfo user) {
        if (!user.isActive()) {
            log.warn("Accoss blocked: account not active userId={}", user.id());
            throw new AccountDisabledException("Account is not active");
        }
    }

    private Tokens issueFor(UserInfo user) {
        return new Tokens(
                jwtService.issueAccessToken(user),
                jwtService.issueRefreshToken(user.id()),
                jwtService.accessTokenTTLSeconds(),
                user
        );
    }
}
