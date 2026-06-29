package com.lmello.titer.storage.api.events;

import com.lmello.titer.storage.api.representation.FileRepresentation;
import org.jmolecules.event.annotation.Externalized;

import java.time.Instant;
import java.util.UUID;

@Externalized("storage.file-ready")
public record FileReadyEvent(
        UUID fileId,
        String publicUrl,
        String contentType,
        long sizeBytes,
        Instant occurredOn,
        FileRepresentation snapshot
) {
    public boolean isImage() {
        return contentType != null && contentType.startsWith("image/");
    }

    public boolean isVideo() {
        return contentType != null && contentType.startsWith("video/");
    }

    public static FileReadyEvent from(FileRepresentation file) {
        return new FileReadyEvent(
                file.id(),
                file.publicUrl(),
                file.contentType(),
                file.sizeBytes(),
                Instant.now(),
                file
        );
    }
}
