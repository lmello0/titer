package com.lmello.titer.auth.socialproviders;

import jakarta.annotation.Nullable;

public record SocialIdentity(
        String providerId,
        String email,
        boolean emailVerified,
        @Nullable String firstName,
        @Nullable String lastName
) {
}
