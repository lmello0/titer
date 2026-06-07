package com.lmello.titer.shared.api.jwt;

import java.util.Set;
import java.util.UUID;

public interface JwtService {
    String generateToken(
            UUID userId,
            String username,
            String email,
            Set<String> authorities
    );
}
