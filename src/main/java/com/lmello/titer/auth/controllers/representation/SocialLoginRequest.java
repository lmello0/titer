package com.lmello.titer.auth.controllers.representation;

import jakarta.validation.constraints.NotBlank;

public record SocialLoginRequest(
        @NotBlank String token
) {
}
