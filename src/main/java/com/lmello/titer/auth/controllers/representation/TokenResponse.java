package com.lmello.titer.auth.controllers.representation;

import com.lmello.titer.auth.api.AuthService;
import com.lmello.titer.users.api.representation.UserInfo;

import java.util.Set;
import java.util.UUID;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresIn,
        User user
) {
    public record User(
            UUID id,
            String username,
            String email,
            boolean emailVerified,
            Set<String> roles
    ) {
    }

    public static TokenResponse from(AuthService.Tokens tokens) {
        UserInfo u = tokens.user();

        return new TokenResponse(
                tokens.accessToken(),
                tokens.refreshToken(),
                "Bearer",
                tokens.expiresInSeconds(),
                new User(u.id(), u.username(), u.email(), u.isEmailVerified(), u.roles())
                );
    }
}
