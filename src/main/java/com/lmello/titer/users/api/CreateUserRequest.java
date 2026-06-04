package com.lmello.titer.users.api;

public record CreateUserRequest(
        String username,
        String email,
        String name,
        String profilePicture
) {
}
