package com.lmello.titer.users.api;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String email,
        String username,
        String firstName,
        String lastName,
        String profilePicture
) {
}
