package com.lmello.titer.auth.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Slf4j
@ConfigurationProperties(prefix = "app.auth.jwt")
public record JwtProperties(
        String secret,
        String issuer,
        Duration accessTokenTTL,
        Duration refreshTokenTTL
) {
    public JwtProperties {
        if (secret == null || secret.getBytes().length < 32) {
            throw new IllegalStateException("app.auth.jwt.secret must be set and at least 32 bytes long for HS256 signing");
        }

        log.info("JWT access token TTL: {}, refresh token TTL: {}", accessTokenTTL, refreshTokenTTL);
    }
}
