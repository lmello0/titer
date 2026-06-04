package com.lmello.titer.users.api;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String email,
        String name,
        String profilePicture
) {
}
