package com.lmello.titer.users.service;

import com.lmello.titer.storage.api.StorageService;
import com.lmello.titer.users.api.UserService;
import com.lmello.titer.users.api.command.CreateUserCommand;
import com.lmello.titer.users.api.events.UserDeactivatedEvent;
import com.lmello.titer.users.api.events.UserDeletedEvent;
import com.lmello.titer.users.api.events.UserEmailVerificationRequestEvent;
import com.lmello.titer.users.api.exceptions.*;
import com.lmello.titer.users.api.representation.UserInfo;
import com.lmello.titer.users.entities.EmailVerificationTokenEntity;
import com.lmello.titer.users.entities.RoleEntity;
import com.lmello.titer.users.entities.UserEntity;
import com.lmello.titer.users.entities.UserRoleAuditEntity;
import com.lmello.titer.users.properties.EmailVerificationProperties;
import com.lmello.titer.users.repositories.EmailVerificationTokenRepository;
import com.lmello.titer.users.repositories.RoleRepository;
import com.lmello.titer.users.repositories.UserRepository;
import com.lmello.titer.users.repositories.UserRoleAuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final EmailVerificationProperties verificationProperties;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleAuditRepository auditRepository;
    private final EmailVerificationTokenRepository tokenRepository;

    private final StorageService storageService;
    private final ApplicationEventPublisher eventPublisher;

    private final StringKeyGenerator tokenGenerator = new Base64StringKeyGenerator(32);

    // read

    @Override
    public UserInfo getById(UUID id) {
        return userRepository.findActiveById(id)
                .map(this::toInfo)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public Optional<UserInfo> findById(UUID id) {
        return userRepository
                .findActiveById(id)
                .map(this::toInfo);
    }

    @Override
    public Optional<UserInfo> findByEmail(String email) {
        return userRepository
                .fincActiveByEmail(normalize(email))
                .map(this::toInfo);
    }

    @Override
    public Optional<UserInfo> findByUsername(String username) {
        return userRepository
                .findActiveByUsername(username.trim())
                .map(this::toInfo);
    }

    @Override
    public Optional<UserInfo> findByUsernameOrEmail(String identifier) {
        return userRepository
                .findActiveByUsernameOrEmail(identifier.trim())
                .map(this::toInfo);
    }

    @Override
    public List<UserInfo> getAll() {
        return userRepository
                .findAllActive()
                .stream()
                .map(this::toInfo)
                .toList();
    }

    // lifecycle

    @Override
    @Transactional
    public UserInfo create(CreateUserCommand command) {
        String email = normalize(command.email());

        if (userRepository.existsActiveByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        String username = (command.username() == null || command.username().isBlank())
                ? generateUsername(email)
                : command.username().trim();

        if (userRepository.existsActiveByUsername(username)) {
            throw new UsernameAlreadyExistsException(username);
        }

        UserEntity u = userRepository.save(UserEntity.create(
                username,
                email,
                command.firstName(),
                command.lastName(),
                command.isEmailVerified(),
                command.createdBy()
        ));
        log.info("User created userId={} verified={} by={}", u.getId(), command.isEmailVerified(), command.createdBy());

        this.grantRole(u.getId(), "USER", "SELF_REGISTRATION", "USER CREATION");

        if (!command.isEmailVerified()) {
            issueVerification(u);
        }

        return toInfo(u);
    }

    @Override
    @Transactional
    public UserInfo patch(UUID id, UserPatch patch, String modifiedBy) {
        UserEntity u = activeOrThrow(id);

        if (patch.username() != null && !patch.username().isBlank()) {
            String username = patch.username().trim();

            if (!username.equalsIgnoreCase(u.getUsername()) && userRepository.existsActiveByUsername(username)) {
                throw new UsernameAlreadyExistsException(username);
            }

            u.rename(username, modifiedBy);
        }

        if (patch.firstName() != null || patch.lastName() != null) {
            u.updateName(patch.firstName(), patch.lastName(), modifiedBy);
        }

        log.info("User patched userId={} by={}", id, modifiedBy);
        return toInfo(u);
    }

    @Override
    @Transactional
    public void softDelete(UUID id, String deletedBy) {
        UserEntity u = activeOrThrow(id);

        u.softDelete(deletedBy);
        tokenRepository.deleteByUserId(id);

        eventPublisher.publishEvent(new UserDeletedEvent(id));
        log.info("User soft-deleted userId={} by={}", id, deletedBy);
    }

    @Override
    public void deactivate(UUID id, String performedBy) {
        UserEntity u = userRepository
                .findActiveById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (u.getDeactivatedAt() == null) {
            u.deactivate(performedBy);
            eventPublisher.publishEvent(new UserDeactivatedEvent(id));

            log.info("User deactivated userId={} by={}", id, performedBy);
        }
    }

    @Override
    @Transactional
    public void reactivate(UUID id, String performedBy) {
        UserEntity u = userRepository
                .findActiveById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        u.reactivate(performedBy);
        log.info("User reactivated userId={} by={}", id, performedBy);
    }

    // email verification

    @Override
    @Transactional
    public void markEmailVerified(UUID id) {
        activeOrThrow(id).markEmailVerified();
        log.info("Email marked verified userId={}", id);
    }

    @Override
    public void resendEmailVerification(UUID id) {
        UserEntity u = activeOrThrow(id);

        if (u.isEmailVerified()) {
            return;
        }

        issueVerification(u);
    }

    @Override
    @Transactional
    public UserInfo confirmEmailVerification(String token) {
        EmailVerificationTokenEntity record = tokenRepository.findByTokenHash(hash(token))
                .orElseThrow(() -> new EmailVerificationException("Invalid verification token"));

        Instant now = Instant.now();
        if (record.isUsed() || record.isExpired(now)) {
            throw new EmailVerificationException("Verification token is expired or already used");
        }

        UserEntity u = activeOrThrow(record.getUserId());
        if (!u.getEmail().equalsIgnoreCase(record.getEmail())) {
            throw new EmailVerificationException("Verification token no longer matches the account email");
        }

        record.markUsed();
        u.markEmailVerified();

        log.info("Email verified userId={}", u.getId());

        return toInfo(u);
    }

    private void issueVerification(UserEntity user) {
        tokenRepository.deleteByUserId(user.getId());

        String raw = tokenGenerator.generateKey();
        tokenRepository.save(new EmailVerificationTokenEntity(
                user.getId(),
                user.getEmail(),
                hash(raw),
                Instant.now().plus(verificationProperties.ttl())
        ));

        eventPublisher.publishEvent(new UserEmailVerificationRequestEvent(user.getId(), user.getEmail(), raw));

        log.info("Email verification issued userId={}", user.getId());
    }

    // role

    @Override
    @Transactional
    public UserInfo grantRole(UUID userId, String roleName, String performedBy, String reason) {
        UserEntity u = activeOrThrow(userId);
        RoleEntity role = roleRepository
                .findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(roleName));

        if (!u.hasRole(role)) {
            u.grant(role);
            auditRepository.save(UserRoleAuditEntity.granted(userId, role.getId(), performedBy, reason));

            log.info("Role granted role={} userId={} by={}", roleName, userId, performedBy);
        }

        return toInfo(u);
    }

    @Override
    @Transactional
    public UserInfo revokeRole(UUID userId, String roleName, String performedBy, String reason) {
        UserEntity u = activeOrThrow(userId);
        RoleEntity role = roleRepository
                .findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(roleName));

        if (u.hasRole(role)) {
            u.revoke(role);
            auditRepository.save(UserRoleAuditEntity.revoked(userId, role.getId(), performedBy, reason));

            log.info("Role revoked role={} userId={} by={}", roleName, userId, performedBy);
        }

        return toInfo(u);
    }

    // profile picture

    @Override
    @Transactional
    public UserInfo setProfilePicture(UUID userId, UUID fileId, String modifiedBy) {
        UserEntity u = activeOrThrow(userId);
        UUID previous = u.getProfilePictureFileId();

        u.changeProfilePicture(fileId, modifiedBy);

        if (previous != null && !previous.equals(fileId)) {
            safeDeleteFile(previous);
        }

        log.info("Profile picture set userId={} fileId={}", userId, fileId);
        return toInfo(u);
    }

    @Override
    @Transactional
    public UserInfo removeProfilePicture(UUID userId, String modifiedBy) {
        UserEntity u = activeOrThrow(userId);
        UUID previous = u.getProfilePictureFileId();

        if (previous != null) {
            u.changeProfilePicture(null, modifiedBy);
            safeDeleteFile(previous);

            log.info("Profile picture removed userId={}", userId);
        }

        return toInfo(u);
    }

    // helpers

    private UserEntity activeOrThrow(UUID id) {
        return userRepository
                .findActiveById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    private UserInfo toInfo(UserEntity u) {
        Set<String> roleNames = u.getRoles().stream()
                .map(RoleEntity::getName)
                .collect(Collectors.toSet());

        String pictureUrl = null;
        if (u.getProfilePictureFileId() != null) {
            try {
                pictureUrl = storageService.publicUrl(u.getProfilePictureFileId()).toString();
            } catch (RuntimeException e) {
                log.warn("Could not resolve profile picture URL fileId={} userId={}", u.getProfilePictureFileId(), u.getId());
            }
        }

        return new UserInfo(
                u.getId(),
                u.getUsername(),
                u.getEmail(),
                u.getFirstName(),
                u.getLastName(),
                u.isEmailVerified(),
                u.isActive(),
                roleNames,
                pictureUrl
        );
    }

    private void safeDeleteFile(UUID fileId) {
        try {
            storageService.delete(fileId);
        } catch (RuntimeException e) {
            log.warn("Failed to delete old profile-picture file fileId={}", fileId, e);
        }
    }

    private String generateUsername(String email) {
        String base = email.substring(0, email.indexOf('@'))
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9._-]]", "");

        if (base.isBlank()) {
            base = "user";
        }

        if (base.length() > 20) {
            base = base.substring(0, 20);
        }

        String candidate = base;
        for (int attempt = 0; attempt < 5 && userRepository.existsActiveByUsername(candidate); attempt++) {
            String suffix = Integer.toString(1_000 + (int) (Math.random() * 9_000));
            candidate = base + suffix;
        }

        return candidate;
    }

    private static String normalize(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private static String hash(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(token.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 unavailable", e);
        }
    }
}
