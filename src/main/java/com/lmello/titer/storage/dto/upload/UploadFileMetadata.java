package com.lmello.titer.storage.dto.upload;

import lombok.Builder;

@Builder
public record UploadFileMetadata(
        String originalName,
        String namePrefix,
        String nameSuffix,
        String contentType,
        long sizeBytes
) {
}
