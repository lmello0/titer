package com.lmello.titer.storage.infrastructure.provider;

import com.lmello.titer.storage.api.command.StorageTarget;
import com.lmello.titer.storage.exceptions.StorageProviderException;
import com.lmello.titer.storage.properties.StorageProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "storage.local.enabled", havingValue = "true", matchIfMissing = true)
public class LocalStorageProvider implements StorageProvider {

    private final Path rootPath;
    private final String baseUrl;

    public LocalStorageProvider(StorageProperties properties) {
        StorageProperties.LocalProperties local = properties.local();

        this.rootPath = Paths.get(local.rootPath()).toAbsolutePath();
        this.baseUrl = properties.baseUrl();

        ensureDirectory(this.rootPath);
    }

    @Override
    public String name() {
        return "local";
    }

    @Override
    public boolean supports(StorageTarget target) {
        return target == StorageTarget.LOCAL || target == StorageTarget.DEFAULT;
    }

    @Override
    public String store(String filename, String contentType, byte[] data) {
        String key = UUID.ofEpochMillis(Instant.now().toEpochMilli()) + "_" + sanitize(filename);
        Path dest = rootPath.resolve(key);

        try {
            Files.write(dest, data);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to write file: " + dest, e);
        }

        return key;
    }

    @Override
    public Resource load(String storageKey) {
        Path file = rootPath.resolve(storageKey);
        if (!Files.exists(file)) {
            throw new StorageProviderException("File not found: " + storageKey);
        }

        return new FileSystemResource(file);
    }

    @Override
    public void delete(String storageKey) {
        try {
            Files.deleteIfExists(rootPath.resolve(storageKey));
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to delete: " + storageKey, e);
        }
    }

    @Override
    public String resolvePublicUrl(UUID fileId) {
        return baseUrl + "/" + fileId;
    }

    private void ensureDirectory(Path path) {
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot create storage root: " + path, e);
        }
    }

    private String sanitize(String filename) {
        return filename.replaceAll("[^a-zA-Z0-9.\\-_]", "_");
    }
}
