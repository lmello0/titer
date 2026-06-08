package com.lmello.titer.storage.dto.file;

import java.time.Instant;
import java.util.UUID;

public record StoredFile(
        UUID fileId,
        String url,
        String originalName,
        String contentType,
        long sizeBytes,
        Instant createdAt
) {
}
