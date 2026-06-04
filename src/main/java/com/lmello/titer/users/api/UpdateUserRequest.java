package com.lmello.titer.users.api;

public record UpdateUserRequest(
        String username,
        String email,
        String name,
        String profilePicture
) {
}
