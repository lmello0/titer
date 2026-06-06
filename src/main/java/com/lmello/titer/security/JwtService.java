package com.lmello.titer.security;

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
