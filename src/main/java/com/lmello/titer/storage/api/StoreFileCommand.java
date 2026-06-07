package com.lmello.titer.storage.api;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record StoreFileCommand(
        UUID fileId,
        String path,
        String baseName,
        MultipartFile file,
        FileRules rules
) {
}
