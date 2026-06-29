package com.lmello.titer.users.api.events;

import java.util.UUID;

public record UserEmailVerifiedEvent(UUID userId) {
}
