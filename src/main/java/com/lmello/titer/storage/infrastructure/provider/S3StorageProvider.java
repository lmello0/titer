package com.lmello.titer.storage.infrastructure.provider;

import com.lmello.titer.storage.api.command.StorageTarget;
import com.lmello.titer.storage.properties.StorageProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "app.storage.s3.enabled", havingValue = "true")
public class S3StorageProvider implements StorageProvider {

    private final String baseUrl;

    public S3StorageProvider(StorageProperties properties) {
        this.baseUrl = properties.baseUrl();
    }

    @Override
    public String name() {
        return "s3";
    }

    @Override
    public boolean supports(StorageTarget target) {
        return target == StorageTarget.BUCKET || target == StorageTarget.DEFAULT;
    }

    @Override
    public String store(String filename, String contentType, byte[] data) {
        String key = UUID.ofEpochMillis(Instant.now().toEpochMilli()) + "/" + filename;

        return key;
    }

    @Override
    public Resource load(String storageKey) {
        throw new UnsupportedOperationException("S3 client not configured");
    }

    @Override
    public void delete(String storageKey) {

    }

    @Override
    public String resolvePublicUrl(UUID fileId) {
        return baseUrl + fileId;
    }
}
