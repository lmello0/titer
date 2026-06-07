package com.lmello.titer.storage.api;

import java.util.Set;

public record FileRules(
        Long maxSizeBytes,
        Set<String> allowedContentTypes
) {
    public static FileRules defaultImage() {
        return image(null);
    }

    public static FileRules image(Long maxSizeBytes) {
        return new FileRules(
                maxSizeBytes,
                Set.of("image/jpeg", "image/png", "image/webp")
        );
    }

    public static FileRules pdfOnly(Long maxSizeBytes) {
        return new FileRules(
                maxSizeBytes,
                Set.of("application/pdf")
        );
    }

    public static FileRules anyFile() {
        return new FileRules(null, Set.of());
    }

    public boolean allowsContentType(String contentType) {
        return allowedContentTypes == null
                || allowedContentTypes.isEmpty()
                || allowedContentTypes.contains(contentType);
    }
}
