package com.lmello.titer.storage.internal.services;

import com.lmello.titer.storage.api.PhysicalStoredFile;
import com.lmello.titer.storage.api.StoreFileCommand;
import com.lmello.titer.storage.internal.enums.StorageProvider;
import com.lmello.titer.storage.internal.properties.StorageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@ConditionalOnProperty(
        name = "app.storage.provider",
        havingValue = "local"
)
@RequiredArgsConstructor
public class LocalFileStorage implements FileStorage {

    private final StorageProperties properties;
    private final FileValidator fileValidator;

    @Override
    public StorageProvider provider() {
        return StorageProvider.LOCAL;
    }

    @Override
    public PhysicalStoredFile store(StoreFileCommand command) {
        MultipartFile file = command.file();

        fileValidator.validate(file, command.rules());

        String extension = fileValidator.extensionFrom(file);
        String storedName = "%s.%s".formatted(command.fileId(), extension);

        String storageKey = "%s/%s".formatted(normalizePath(command.path()), storedName);

        Path targetPath = Path.of(properties.local().basePath())
                .resolve(storageKey)
                .normalize();

        try {
            Files.createDirectories(targetPath.getParent());
            file.transferTo(targetPath);

            return new PhysicalStoredFile(
                    provider(),
                    storedName,
                    null,
                    storageKey,
                    null,
                    targetPath.toString(),
                    null
            );
        } catch (IOException exception) {
            throw new IllegalStateException("Could not store file locally", exception);
        }
    }

    @Override
    public void delete(PhysicalStoredFile file) {
        if (file == null || file.path() == null || file.path().isBlank()) return;

        try {
            Files.deleteIfExists(Path.of(file.path()));
        } catch (IOException exception) {
            throw new IllegalStateException("Could not delete local file", exception);
        }
    }

    private String normalizePath(String path) {
        return path.replaceAll("^/+", "").replaceAll("/+$", "");
    }
}
