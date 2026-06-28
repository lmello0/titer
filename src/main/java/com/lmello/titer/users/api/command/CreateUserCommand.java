package com.lmello.titer.users.api.command;

import jakarta.annotation.Nullable;

public record CreateUserCommand(
        String username,
        String email,
        @Nullable String firstName,
        @Nullable String lastName,
        boolean emailVerified,
        String createdBy
) {
}
