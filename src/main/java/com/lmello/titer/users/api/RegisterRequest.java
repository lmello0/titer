package com.lmello.titer.users.api;

public record RegisterRequest(
        String username,
        String email,
        String password,
        String name,
        String profilePicture
) {
}
