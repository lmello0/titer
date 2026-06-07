package com.lmello.titer.storage.internal.services;

import com.lmello.titer.storage.api.PhysicalStoredFile;
import com.lmello.titer.storage.api.StoreFileCommand;
import com.lmello.titer.storage.internal.enums.StorageProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@ConditionalOnProperty(
        name = "app.storage.provider",
        havingValue = "database"
)
@RequiredArgsConstructor
public class DatabaseFileStorage implements FileStorage {

    private final FileValidator fileValidator;

    @Override
    public StorageProvider provider() {
        return StorageProvider.DATABASE;
    }

    @Override
    public PhysicalStoredFile store(StoreFileCommand command) {
        MultipartFile file = command.file();

        fileValidator.validate(file, command.rules());

        String extension = fileValidator.extensionFrom(file);

        String storedName = "%s.%s".formatted(command.fileId(), extension);

        try {
            return new PhysicalStoredFile(
                    provider(),
                    storedName,
                    null,
                    String.valueOf(command.fileId()),
                    "/files/" + command.fileId(),
                    null,
                    file.getBytes()
            );
        } catch (IOException exception) {
            throw new IllegalStateException("Could not read file bytes", exception);
        }

    }

    @Override
    public void delete(PhysicalStoredFile file) {
        // Nothing to delete
    }
}
