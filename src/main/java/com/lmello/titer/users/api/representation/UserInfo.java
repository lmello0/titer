package com.lmello.titer.users.api.representation;

import jakarta.annotation.Nullable;

import java.util.Set;
import java.util.UUID;

public record UserInfo(
        UUID id,
        String username,
        String email,
        @Nullable String firstName,
        @Nullable String lastName,
        boolean emailVerified,
        Set<String> roles,
        @Nullable UUID profilePictureFileId
) {
}
