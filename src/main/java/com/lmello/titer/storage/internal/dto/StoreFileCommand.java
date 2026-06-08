package com.lmello.titer.storage.internal.dto;

import com.lmello.titer.storage.dto.upload.FileRules;
import com.lmello.titer.storage.dto.upload.UploadFile;

import java.util.UUID;

public record StoreFileCommand(
        UUID fileId,
        String path,
        UploadFile file,
        FileRules rules
) {
}
