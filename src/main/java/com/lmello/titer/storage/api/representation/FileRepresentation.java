package com.lmello.titer.storage.api.representation;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record FileRepresentation(
        UUID id,
        String filename,
        String contentType,
        long sizeBytes,
        long originalSizeBytes,
        FileStatus status,
        String publicUrl,
        String storageKey,
        String provider,
        MediaMetadata mediaMetadata,
        Map<String, String> additionalMetadata,
        Instant createdAt,
        Instant processedAt
) {
    public boolean isReady() {
        return status == FileStatus.READY;
    }

    public boolean isImage() {
        return contentType != null && contentType.startsWith("image/");
    }

    public boolean isVideo() {
        return contentType != null && contentType.startsWith("video/");
    }

    public record MediaMetadata(
            Integer width,
            Integer height,
            Long durationMs,
            String codec,
            String colorSpace,
            Boolean hasAlpha
    ) {
        public static MediaMetadata empty() {
            return new MediaMetadata(null, null, null, null, null, null);
        }
    }
}
