package com.lmello.titer.auth.api;

import com.lmello.titer.auth.enums.AuthProvider;
import com.lmello.titer.users.api.representation.UserInfo;

import java.util.UUID;

public interface AuthService {
    record Tokens(
            String accessToken,
            String refreshToken,
            long expiresInSeconds,
            UserInfo user
    ) {
    }

    Tokens registerLocal(
            String username,
            String email,
            String rawPassword,
            String firstName,
            String lastName
    );

    Tokens login(String identifier, String rawPassword);

    Tokens socialLogin(AuthProvider provider, String token);

    Tokens refresh(String refreshToken);

    void logout(String rawRefreshToken);

    void logoutEverywhere(UUID userId);
}
