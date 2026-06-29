package com.lmello.titer.users.api.events;

import java.util.UUID;

// TODO: implement account deactivation (not deletion)
public record UserDeactivatedEvent(UUID userId) {
}
