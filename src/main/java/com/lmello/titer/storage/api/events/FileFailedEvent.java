package com.lmello.titer.storage.api.events;

import org.jmolecules.event.annotation.Externalized;

import java.time.Instant;
import java.util.UUID;

@Externalized("storage.file-failed")
public record FileFailedEvent(
        UUID fileId,
        String reason,
        Instant occurredOn
) {
    public static FileFailedEvent of(UUID fileId, String reason) {
        return new FileFailedEvent(fileId, reason, Instant.now());
    }
}
