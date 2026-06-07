package com.lmello.titer.storage.api;

import org.springframework.web.multipart.MultipartFile;

public record StoreFileRequest(
        MultipartFile file,
        String path,
        String baseName,
        String createdBy,
        FileRules rules
) {
    public FileRules rules() {
        return rules == null ? FileRules.defaultImage() : rules;
    }
}
