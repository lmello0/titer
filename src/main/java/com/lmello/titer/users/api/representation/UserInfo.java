package com.lmello.titer.users.api.representation;

import java.util.Set;
import java.util.UUID;

public record UserInfo(
        UUID id,
        String username,
        String email,
        String firstName,
        String lastName,
        boolean isEmailVerified,
        boolean isActive,
        Set<String> roles,
        String profilePictureUrl
) {
}
