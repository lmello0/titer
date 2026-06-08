package com.lmello.titer.storage.dto.upload;

public record StoreFileRequest(
        UploadFile file,
        String path,
        String createdBy,
        FileRules rules
) {
    public FileRules rules() {
        return rules == null ? FileRules.defaultImage() : rules;
    }
}
