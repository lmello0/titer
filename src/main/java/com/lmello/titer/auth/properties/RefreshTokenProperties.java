package com.lmello.titer.auth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "app.auth.refresh")
public record RefreshTokenProperties(
        Duration ttl
) {
}
