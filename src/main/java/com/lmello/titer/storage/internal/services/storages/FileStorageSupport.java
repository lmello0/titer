package com.lmello.titer.storage.internal.services.storages;

import com.lmello.titer.storage.dto.upload.UploadFile;
import com.lmello.titer.storage.internal.dto.PreparedStoredFile;
import com.lmello.titer.storage.internal.dto.StoreFileCommand;
import com.lmello.titer.storage.internal.services.FileValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileStorageSupport {

    private final FileValidator fileValidator;

    PreparedStoredFile prepare(StoreFileCommand command) {
        UploadFile file = command.file();

        fileValidator.validate(file, command.rules());

        String extension = fileValidator.extensionFrom(file);
        String storedName = storedName(command, extension);

        return new PreparedStoredFile(
                file,
                storedName,
                storageKey(command.path(), storedName)
        );
    }

    private String storedName(StoreFileCommand command, String extension) {
        String namePrefix = command.file().metadata().namePrefix() == null
                ? ""
                : command.file().metadata().namePrefix() + "_";
        String nameSuffix = command.file().metadata().nameSuffix() == null
                ? ""
                : "_" + command.file().metadata().nameSuffix();

        return "%s%s%s.%s".formatted(namePrefix, command.fileId(), nameSuffix, extension);
    }

    private String storageKey(String path, String storedName) {
        String normalizedPath = normalizePath(path);

        if (normalizedPath.isBlank()) {
            return storedName;
        }

        return "%s/%s".formatted(normalizedPath, storedName);
    }

    private String normalizePath(String path) {
        if (path == null || path.isBlank()) {
            return "";
        }

        return path.replaceAll("^/+", "").replaceAll("/+$", "");
    }
}
