package com.lmello.titer.users.api.events;

import java.util.UUID;

public record UserEmailVerificationRequestEvent(UUID userId, String email, String token) {
}
