package com.lmello.titer.storage.api;

import com.lmello.titer.storage.internal.enums.StorageProvider;

import java.util.UUID;

public record StoredFile(
        UUID fileId,
        String originalName,
        String storedName,
        String contentType,
        long sizeBytes,
        StorageProvider storageProvider,
        String publicUrl
) {
}
