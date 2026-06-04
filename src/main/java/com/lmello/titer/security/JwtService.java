package com.lmello.titer.security;

import java.util.UUID;

public interface JwtService {
    String generate(UUID userId, String username);

    UUID extractUserId(String token);

    String extractUsername(String token);

    boolean isValid(String token);
}
