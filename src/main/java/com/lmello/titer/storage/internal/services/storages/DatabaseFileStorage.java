package com.lmello.titer.storage.internal.services.storages;

import com.lmello.titer.storage.dto.download.FileDownload;
import com.lmello.titer.storage.dto.file.StoredFile;
import com.lmello.titer.storage.internal.dto.PhysicalStoredFile;
import com.lmello.titer.storage.internal.dto.PreparedStoredFile;
import com.lmello.titer.storage.internal.dto.StoreFileCommand;
import com.lmello.titer.storage.internal.entities.FileEntity;
import com.lmello.titer.storage.internal.enums.StorageProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@ConditionalOnProperty(
        name = "app.storage.provider",
        havingValue = "database"
)
@RequiredArgsConstructor
public class DatabaseFileStorage implements FileStorage {

    private final FileStorageSupport support;

    @Override
    public StorageProvider provider() {
        return StorageProvider.DATABASE;
    }

    @Override
    public PhysicalStoredFile store(StoreFileCommand command) {
        PreparedStoredFile preparedFile = support.prepare(command);

        try (InputStream inputStream = preparedFile.file().content()) {
            return new PhysicalStoredFile(
                    provider(),
                    preparedFile.storedName(),
                    null,
                    String.valueOf(command.fileId()),
                    "/files/" + command.fileId(),
                    null,
                    inputStream.readAllBytes()
            );
        } catch (IOException exception) {
            throw new IllegalStateException("Could not read file bytes", exception);
        }

    }

    @Override
    public FileDownload download(FileEntity file, StoredFile metadata) {
        return new FileDownload(
                metadata,
                new ByteArrayInputStream(file.getData())
        );
    }

    @Override
    public void delete(PhysicalStoredFile file) {
        // Nothing to delete
    }
}
