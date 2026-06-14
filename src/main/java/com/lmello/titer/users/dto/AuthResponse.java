package com.lmello.titer.users.dto;

public record AuthResponse(
        String accessToken,
        UserResponse user
) {
}
