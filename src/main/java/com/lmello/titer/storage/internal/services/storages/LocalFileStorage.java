package com.lmello.titer.storage.internal.services.storages;

import com.lmello.titer.storage.dto.download.FileDownload;
import com.lmello.titer.storage.dto.file.StoredFile;
import com.lmello.titer.storage.internal.dto.PhysicalStoredFile;
import com.lmello.titer.storage.internal.dto.PreparedStoredFile;
import com.lmello.titer.storage.internal.dto.StoreFileCommand;
import com.lmello.titer.storage.internal.entities.FileEntity;
import com.lmello.titer.storage.internal.enums.StorageProvider;
import com.lmello.titer.storage.internal.properties.StorageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
@ConditionalOnProperty(
        name = "app.storage.provider",
        havingValue = "local"
)
@RequiredArgsConstructor
public class LocalFileStorage implements FileStorage {

    private final StorageProperties properties;
    private final FileStorageSupport support;

    @Override
    public StorageProvider provider() {
        return StorageProvider.LOCAL;
    }

    @Override
    public PhysicalStoredFile store(StoreFileCommand command) {
        PreparedStoredFile preparedFile = support.prepare(command);

        Path basePath = basePath();
        Path targetPath = basePath
                .resolve(preparedFile.storageKey())
                .normalize();

        if (!targetPath.startsWith(basePath)) {
            throw new IllegalArgumentException("File path escapes storage base path");
        }

        try (InputStream fileInputStream = preparedFile.file().content()) {
            Files.createDirectories(targetPath.getParent());

            Files.copy(fileInputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);

            return new PhysicalStoredFile(
                    provider(),
                    preparedFile.storedName(),
                    null,
                    preparedFile.storageKey(),
                    null,
                    targetPath.toString(),
                    null
            );
        } catch (IOException exception) {
            throw new IllegalStateException("Could not store file locally", exception);
        }
    }

    @Override
    public FileDownload download(FileEntity file, StoredFile metadata) {
        try {
            Path path = localPath(file.getPath());

            return new FileDownload(
                    metadata,
                    Files.newInputStream(path)
            );
        } catch (IOException exception) {
            throw new IllegalStateException("Could not read local file", exception);
        }
    }

    @Override
    public void delete(PhysicalStoredFile file) {
        if (file == null || file.path() == null || file.path().isBlank()) return;

        try {
            Files.deleteIfExists(localPath(file.path()));
        } catch (IOException exception) {
            throw new IllegalStateException("Could not delete local file", exception);
        }
    }

    private Path basePath() {
        return Path.of(properties.local().basePath())
                .toAbsolutePath()
                .normalize();
    }

    private Path localPath(String path) {
        Path basePath = basePath();
        Path localPath = Path.of(path)
                .toAbsolutePath()
                .normalize();

        if (!localPath.startsWith(basePath)) {
            throw new IllegalArgumentException("File path escapes storage base path");
        }

        return localPath;
    }
}
