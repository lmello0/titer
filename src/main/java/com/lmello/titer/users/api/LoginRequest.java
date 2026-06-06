package com.lmello.titer.users.api;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank
        String identifier,

        @NotBlank
        String password
) {
}
