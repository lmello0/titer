package com.lmello.titer.users.events;

import java.util.UUID;

public record UserDeletedEvent(UUID userId) {
}
