package com.lmello.titer.storage.internal.services;

import com.lmello.titer.storage.dto.upload.FileRules;
import com.lmello.titer.storage.dto.upload.UploadFile;
import com.lmello.titer.storage.dto.upload.UploadFileMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.servlet.autoconfigure.MultipartProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class FileValidator {

    private static final Map<String, String> EXTENSIONS_BY_CONTENT_TYPE = Map.of(
            "image/jpeg", "jpg",
            "image/png", "png",
            "image/webp", "webp",
            "application/pdf", "pdf",
            "text/plain", "txt",
            "text/csv", "csv"
    );

    private final MultipartProperties multipartProperties;

    public void validate(UploadFile file, FileRules rules) {
        if (file == null) {
            throw new IllegalArgumentException("File is empty");
        }

        UploadFileMetadata metadata = file.metadata();

        if (metadata == null) {
            throw new IllegalArgumentException("File metadata is required");
        }

        if (metadata.sizeBytes() < 0) {
            throw new IllegalArgumentException("File size must be greater than or equal to 0 bytes");
        }

        if (file.content() == null) {
            throw new IllegalArgumentException("File content is required");
        }

        FileRules resolvedRules = rules == null
                ? FileRules.anyFile()
                : rules;

        long effectiveMaxSize = resolveEffectiveMaxSizeBytes(resolvedRules);

        if (effectiveMaxSize >= 0 && metadata.sizeBytes() > effectiveMaxSize) {
            throw new IllegalArgumentException("File must be less than or equal to " + effectiveMaxSize + " bytes");
        }

        String contentType = metadata.contentType();

        if (contentType == null || contentType.isBlank()) {
            throw new IllegalArgumentException("File content type is required");
        }

        if (!resolvedRules.allowsContentType(metadata.contentType())) {
            throw new IllegalArgumentException("File content type is not allowed");
        }
    }

    public String extensionFrom(UploadFile file) {
        UploadFileMetadata metadata = file.metadata();
        String contentType = metadata.contentType();

        String extension = EXTENSIONS_BY_CONTENT_TYPE.get(contentType);

        if (extension != null) {
            return extension;
        }

        String originalFilename = metadata.originalName();

        if (originalFilename == null || !originalFilename.contains(".")) {
            return "bin";
        }

        String rawExtension = originalFilename.substring(
                originalFilename.lastIndexOf(".") + 1
        );

        String sanitizedExtension = rawExtension
                .replaceAll("[^a-zA-Z0-9]", "")
                .toLowerCase();

        return sanitizedExtension.isBlank() ? "bin" : sanitizedExtension;
    }

    private long resolveEffectiveMaxSizeBytes(FileRules rules) {
        Long ruleMaxSize = rules.maxSizeBytes();
        Long multipartMaxSize = multipartMaxFileSizeBytes();

        if (ruleMaxSize == null) {
            return multipartMaxSize == null ? -1 : multipartMaxSize;
        }

        if (multipartMaxSize == null) {
            return ruleMaxSize;
        }

        return Math.min(ruleMaxSize, multipartMaxSize);
    }

    private Long multipartMaxFileSizeBytes() {
        DataSize maxFileSize = multipartProperties.getMaxFileSize();

        long bytes = maxFileSize.toBytes();

        if (bytes < 0) {
            return null;
        }

        return bytes;
    }
}
