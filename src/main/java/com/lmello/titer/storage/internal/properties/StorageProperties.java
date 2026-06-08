package com.lmello.titer.storage.internal.properties;


import com.lmello.titer.storage.internal.enums.StorageProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.storage")
public record StorageProperties(
        StorageProvider provider,
        String publicBaseUrl,
        Local local,
        S3 s3
) {
    public StorageProvider provider() {
        return provider == null ? StorageProvider.DATABASE : provider;
    }

    public record Local(
            String basePath
    ) {
    }

    public record S3(
            String bucket
    ) {
    }
}
