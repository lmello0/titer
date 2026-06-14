package com.lmello.titer.storage.api.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.io.InputStream;
import java.util.Map;

@Builder
public record StoreFileCommand(
        @NotBlank(message = "filename must not be blank")
        String filename,

        @NotBlank(message = "contentType must not be blank")
        String contentType,

        @Positive(message = "contentLength must be positive")
        long contentLength,

        @NotNull(message = "content must not be null")
        InputStream content,

        StorageTarget target,

        Map<String, String> metadata
) {
    public StoreFileCommand {
        metadata = metadata == null ? Map.of() : Map.copyOf(metadata);
        target = target == null ? StorageTarget.DEFAULT : target;
    }

    public boolean isImage() {
        return contentType.startsWith("image/");
    }

    public boolean isVideo() {
        return contentType.startsWith("video/");
    }
}
