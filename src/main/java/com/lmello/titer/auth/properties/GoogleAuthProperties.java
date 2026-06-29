package com.lmello.titer.auth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app.auth.social.google")
public record GoogleAuthProperties(
        List<String> clientIds
) {
}
