package com.lmello.titer.user;

import java.util.UUID;

public record UserDTO(
        UUID id,
        String username,
        String email,
        String name,
        String profilePicture
) {
}
