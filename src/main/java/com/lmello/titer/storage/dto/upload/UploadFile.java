package com.lmello.titer.storage.dto.upload;

import lombok.Builder;

import java.io.InputStream;

@Builder
public record UploadFile(
        UploadFileMetadata metadata,
        InputStream content
) {
}
