package com.lmello.titer.users.api;

public record AuthResponse(
        String accessToken,
        UserResponse user
) {
}
