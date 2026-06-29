package com.lmello.titer.auth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "auth.cors")
public record CorsProperties(
   List<String> allowedOrigins
) {
}
