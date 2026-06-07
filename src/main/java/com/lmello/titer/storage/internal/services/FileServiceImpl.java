package com.lmello.titer.storage.internal.services;

import com.lmello.titer.storage.api.*;
import com.lmello.titer.storage.internal.entities.FileEntity;
import com.lmello.titer.storage.internal.mapper.FileMapper;
import com.lmello.titer.storage.internal.repositories.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileStorage fileStorage;
    private final FileRepository fileRepository;

    private final FileMapper fileMapper;

    @Transactional
    public StoredFile store(StoreFileRequest request) {
        Instant now = Instant.now();
        UUID fileId = UUID.ofEpochMillis(now.toEpochMilli());

        PhysicalStoredFile physicalFile = fileStorage.store(
                new StoreFileCommand(
                        fileId,
                        request.path(),
                        request.baseName(),
                        request.file(),
                        request.rules()
                )
        );

        try {
            FileEntity entity = FileEntity.builder()
                    .id(fileId)
                    .originalName(request.file().getOriginalFilename())
                    .storedName(physicalFile.storedName())
                    .contentType(request.file().getContentType())
                    .sizeBytes(request.file().getSize())
                    .storageProvider(physicalFile.provider())
                    .bucket(physicalFile.bucket())
                    .storageKey(physicalFile.storageKey())
                    .url(physicalFile.url())
                    .path(physicalFile.path())
                    .data(physicalFile.data())
                    .createdBy(request.createdBy())
                    .createdAt(now)
                    .build();

            return fileMapper.toDTO(fileRepository.save(entity));
        } catch (RuntimeException exception) {
            fileStorage.delete(physicalFile);
            throw exception;
        }
    }

    @Transactional(readOnly = true)
    public String publicUrl(UUID fileId) {
        FileEntity file = fileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("File not found"));

        return publicUrl(file);
    }

    public String publicUrl(FileEntity file) {
        if (file == null) {
            return null;
        }

        return switch (file.getStorageProvider()) {
            case BUCKET -> file.getUrl();
            case LOCAL -> "/uploads/" + file.getStorageKey();
            case DATABASE -> "/files/" + file.getId();
        };
    }

    @Transactional
    public void delete(UUID fileId) {
        fileRepository.deleteById(fileId);
    }
}
