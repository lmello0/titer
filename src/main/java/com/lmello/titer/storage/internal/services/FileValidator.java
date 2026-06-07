package com.lmello.titer.storage.internal.services;

import com.lmello.titer.storage.api.FileRules;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.servlet.autoconfigure.MultipartProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

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

    public void validate(MultipartFile file, FileRules rules) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        FileRules resolvedRules = rules == null
                ? FileRules.anyFile()
                : rules;

        long effectiveMaxSize = resolveEffectiveMaxSizeBytes(resolvedRules);

        if (effectiveMaxSize >= 0 && file.getSize() > effectiveMaxSize) {
            throw new IllegalArgumentException("File must be less than or equal to " + effectiveMaxSize + " bytes");
        }

        String contentType = file.getContentType();

        if (contentType == null || contentType.isBlank()) {
            throw new IllegalArgumentException("File content type is required");
        }

        if (!resolvedRules.allowsContentType(file.getContentType())) {
            throw new IllegalArgumentException("File content type is not allowed");
        }
    }

    public String extensionFrom(MultipartFile file) {
        String contentType = file.getContentType();

        String extension = EXTENSIONS_BY_CONTENT_TYPE.get(contentType);

        if (extension != null) {
            return extension;
        }

        String originalFilename = file.getOriginalFilename();

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
