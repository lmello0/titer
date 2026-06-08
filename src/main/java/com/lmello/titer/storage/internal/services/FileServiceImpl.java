package com.lmello.titer.storage.internal.services;

import com.lmello.titer.storage.api.FileService;
import com.lmello.titer.storage.dto.download.FileDownload;
import com.lmello.titer.storage.dto.file.StoredFile;
import com.lmello.titer.storage.dto.upload.StoreFileRequest;
import com.lmello.titer.storage.internal.dto.PhysicalStoredFile;
import com.lmello.titer.storage.internal.dto.StoreFileCommand;
import com.lmello.titer.storage.internal.entities.FileEntity;
import com.lmello.titer.storage.internal.exception.FileNotFoundException;
import com.lmello.titer.storage.internal.exception.StorageProviderMismatchException;
import com.lmello.titer.storage.internal.mapper.FileMapper;
import com.lmello.titer.storage.internal.properties.StorageProperties;
import com.lmello.titer.storage.internal.repositories.FileRepository;
import com.lmello.titer.storage.internal.services.storages.FileStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final StorageProperties storageProperties;
    private final FileStorage fileStorage;
    private final FileRepository fileRepository;

    private final FileMapper fileMapper;

    @Transactional
    public StoredFile store(StoreFileRequest request) {
        Instant now = Instant.now();
        UUID fileId = UUID.randomUUID();

        PhysicalStoredFile physicalFile = fileStorage.store(
                new StoreFileCommand(
                        fileId,
                        request.path(),
                        request.file(),
                        request.rules()
                )
        );

        try {
            FileEntity entity = FileEntity.builder()
                    .id(fileId)
                    .originalName(request.file().metadata().originalName())
                    .storedName(physicalFile.storedName())
                    .contentType(request.file().metadata().contentType())
                    .sizeBytes(request.file().metadata().sizeBytes())
                    .storageProvider(physicalFile.provider())
                    .bucket(physicalFile.bucket())
                    .storageKey(physicalFile.storageKey())
                    .url(physicalFile.url())
                    .path(physicalFile.path())
                    .data(physicalFile.data())
                    .createdBy(request.createdBy())
                    .createdAt(now)
                    .build();

            FileEntity saved = fileRepository.saveAndFlush(entity);

            return fileMapper.toDTO(saved, publicUrl(saved));
        } catch (RuntimeException exception) {
            fileStorage.delete(physicalFile);
            throw exception;
        }
    }

    @Transactional(readOnly = true)
    public String publicUrl(UUID fileId) {
        FileEntity file = fileRepository.findById(fileId)
                .orElseThrow(FileNotFoundException::new);

        return publicUrl(file);
    }

    public String publicUrl(FileEntity file) {
        if (file == null) {
            return null;
        }

        return String.join("/", storageProperties.publicBaseUrl(), "files", file.getId().toString());
    }

    @Override
    @Transactional(readOnly = true)
    public FileDownload download(UUID fileId) {
        FileEntity file = fileRepository.findById(fileId)
                .orElseThrow(FileNotFoundException::new);

        assertActiveProvider(file);

        return fileStorage.download(file, fileMapper.toDTO(file, publicUrl(file)));
    }

    @Override
    @Transactional(readOnly = true)
    public StoredFile metadata(UUID fileId) {
        FileEntity file = fileRepository.findById(fileId)
                .orElseThrow(FileNotFoundException::new);

        return fileMapper.toDTO(file, publicUrl(file));
    }

    @Override
    @Transactional
    public void delete(UUID fileId) {
        FileEntity file = fileRepository.findById(fileId)
                .orElseThrow(FileNotFoundException::new);
        assertActiveProvider(file);

        PhysicalStoredFile physicalFile = new PhysicalStoredFile(
                file.getStorageProvider(),
                file.getStoredName(),
                file.getBucket(),
                file.getStorageKey(),
                file.getUrl(),
                file.getPath(),
                file.getData()
        );

        fileRepository.delete(file);
        fileRepository.flush();
        fileStorage.delete(physicalFile);
    }

    private void assertActiveProvider(FileEntity file) {
        if (file.getStorageProvider() != fileStorage.provider()) {
            throw new StorageProviderMismatchException(file.getStorageProvider(), fileStorage.provider());
        }
    }
}
