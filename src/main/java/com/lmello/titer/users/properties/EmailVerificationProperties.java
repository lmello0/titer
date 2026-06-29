package com.lmello.titer.users.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "app.users.email-verification")
public record EmailVerificationProperties(Duration ttl) {
}
