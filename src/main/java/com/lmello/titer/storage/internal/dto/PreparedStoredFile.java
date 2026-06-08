package com.lmello.titer.storage.internal.dto;

import com.lmello.titer.storage.dto.upload.UploadFile;

public record PreparedStoredFile(
        UploadFile file,
        String storedName,
        String storageKey
) {
}
