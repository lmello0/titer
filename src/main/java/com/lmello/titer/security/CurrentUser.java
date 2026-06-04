package com.lmello.titer.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CurrentUser {

    public static Optional<CustomUserPrincipal> principal() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return Optional.empty();
        }

        if (!(authentication.getPrincipal() instanceof CustomUserPrincipal principal)) {
            return Optional.empty();
        }

        return Optional.of(principal);
    }

    public static UUID idOrThrow() {
        return principal()
                .map(CustomUserPrincipal::userId)
                .orElseThrow(() -> new IllegalStateException("No authenticated user"));
    }

    public static String usernameOrThrow() {
        return principal()
                .map(CustomUserPrincipal::getUsername)
                .orElseThrow(() -> new IllegalStateException("No authenticated user"));
    }
}
