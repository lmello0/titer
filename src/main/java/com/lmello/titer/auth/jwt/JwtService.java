package com.lmello.titer.auth.jwt;

import com.lmello.titer.auth.exceptions.InvalidTokenException;
import com.lmello.titer.auth.properties.JwtProperties;
import com.lmello.titer.users.api.representation.UserInfo;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class JwtService {

    private static final String TYPE_CLAIM = JwtConfig.TOKEN_TYPE_CLAIM;
    private static final String ACCESS = JwtConfig.ACCESS_TOKEN_TYPE;
    private static final String REFRESH = "refresh";

    private final JwtEncoder encoder;
    private final JwtProperties properties;
    private final NimbusJwtDecoder refreshTokenDecoder;

    public JwtService(JwtEncoder encoder, SecretKey signingKey, JwtProperties properties) {
        this.encoder = encoder;
        this.properties = properties;

        this.refreshTokenDecoder = NimbusJwtDecoder.withSecretKey(signingKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();

        this.refreshTokenDecoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
                JwtValidators.createDefault(),
                new JwtIssuerValidator(properties.issuer()),
                new JwtClaimValidator<String>(TYPE_CLAIM, REFRESH::equals)
        ));
    }

    public String issueAccessToken(UserInfo user) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(properties.issuer())
                .issuedAt(now)
                .expiresAt(now.plus(properties.accessTokenTTL()))
                .subject(user.id().toString())
                .claim(TYPE_CLAIM, ACCESS)
                .claim("username", user.username())
                .claim("email", user.email())
                .claim("email_verified", user.isEmailVerified())
                .claim("roles", List.copyOf(user.roles()))
                .build();

        return encode(claims);
    }

    public String issueRefreshToken(UUID userId) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(properties.issuer())
                .issuedAt(now)
                .expiresAt(now.plus(properties.refreshTokenTTL()))
                .subject(userId.toString())
                .claim(TYPE_CLAIM, REFRESH)
                .build();

        return encode(claims);
    }

    public UUID parseRefreshToken(String refreshToken) {
        try {
            Jwt jwt = refreshTokenDecoder.decode(refreshToken);
            return UUID.fromString(jwt.getSubject());
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException("Invalid refresh token", e);
        }
    }

    public long accessTokenTTLSeconds() {
        return properties.accessTokenTTL().toSeconds();
    }

    private String encode(JwtClaimsSet claims) {
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}
