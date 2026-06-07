package com.lmello.titer.storage.api;

import com.lmello.titer.storage.internal.enums.StorageProvider;

public record PhysicalStoredFile(
        StorageProvider provider,
        String storedName,
        String bucket,
        String storageKey,
        String url,
        String path,
        byte[] data
) {
}
