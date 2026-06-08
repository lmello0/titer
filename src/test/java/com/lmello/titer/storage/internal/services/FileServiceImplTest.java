package com.lmello.titer.storage.internal.services;

import com.lmello.titer.storage.dto.download.FileDownload;
import com.lmello.titer.storage.dto.file.StoredFile;
import com.lmello.titer.storage.dto.upload.FileRules;
import com.lmello.titer.storage.dto.upload.StoreFileRequest;
import com.lmello.titer.storage.dto.upload.UploadFile;
import com.lmello.titer.storage.dto.upload.UploadFileMetadata;
import com.lmello.titer.storage.internal.dto.PhysicalStoredFile;
import com.lmello.titer.storage.internal.dto.StoreFileCommand;
import com.lmello.titer.storage.internal.entities.FileEntity;
import com.lmello.titer.storage.internal.enums.StorageProvider;
import com.lmello.titer.storage.internal.exception.FileNotFoundException;
import com.lmello.titer.storage.internal.mapper.FileMapper;
import com.lmello.titer.storage.internal.properties.StorageProperties;
import com.lmello.titer.storage.internal.repositories.FileRepository;
import com.lmello.titer.storage.internal.services.storages.FileStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceImplTest {

    @Mock
    private FileStorage fileStorage;

    @Mock
    private FileRepository fileRepository;

    private final FileMapper fileMapper = new FileMapper() {
    };

    private FileServiceImpl fileService;

    @Test
    void storesMetadataAndReturnsPublicDto() {
        fileService = fileService();
        PhysicalStoredFile physicalFile = physicalFile(UUID.randomUUID());
        when(fileStorage.store(any(StoreFileCommand.class))).thenReturn(physicalFile);
        when(fileRepository.saveAndFlush(any(FileEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StoredFile stored = fileService.store(request());

        assertThat(stored.fileId()).isNotNull();
        assertThat(stored.url()).startsWith("http://localhost:8080/files/");
        assertThat(stored.originalName()).isEqualTo("avatar.png");
        assertThat(stored.contentType()).isEqualTo("image/png");
        assertThat(stored.sizeBytes()).isEqualTo(4);
        assertThat(stored.createdAt()).isNotNull();
    }

    @Test
    void deletesPhysicalFileWhenMetadataSaveFails() {
        fileService = fileService();
        PhysicalStoredFile physicalFile = physicalFile(UUID.randomUUID());
        when(fileStorage.store(any(StoreFileCommand.class))).thenReturn(physicalFile);
        when(fileRepository.saveAndFlush(any(FileEntity.class))).thenThrow(new IllegalStateException("db failed"));

        assertThatThrownBy(() -> fileService.store(request()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("db failed");

        verify(fileStorage).delete(physicalFile);
    }

    @Test
    void deleteRemovesMetadataThenPhysicalFile() {
        fileService = fileService();
        UUID fileId = UUID.randomUUID();
        FileEntity entity = entity(fileId);
        when(fileRepository.findById(fileId)).thenReturn(Optional.of(entity));
        when(fileStorage.provider()).thenReturn(StorageProvider.LOCAL);

        fileService.delete(fileId);

        verify(fileRepository).delete(entity);
        verify(fileRepository).flush();

        ArgumentCaptor<PhysicalStoredFile> captor = ArgumentCaptor.forClass(PhysicalStoredFile.class);
        verify(fileStorage).delete(captor.capture());
        assertThat(captor.getValue().path()).isEqualTo("/tmp/avatar.png");
    }

    @Test
    void metadataMapsStoredFileFields() {
        fileService = fileService();
        UUID fileId = UUID.randomUUID();
        FileEntity entity = entity(fileId);
        when(fileRepository.findById(fileId)).thenReturn(Optional.of(entity));

        StoredFile metadata = fileService.metadata(fileId);

        assertThat(metadata.fileId()).isEqualTo(fileId);
        assertThat(metadata.url()).isEqualTo("http://localhost:8080/files/" + fileId);
        assertThat(metadata.originalName()).isEqualTo("avatar.png");
        assertThat(metadata.contentType()).isEqualTo("image/png");
        assertThat(metadata.sizeBytes()).isEqualTo(4);
        assertThat(metadata.createdAt()).isEqualTo(entity.getCreatedAt());
    }

    @Test
    void downloadDelegatesToActiveStorageProviderWithMetadata() {
        fileService = fileService();
        UUID fileId = UUID.randomUUID();
        FileEntity entity = entity(fileId);
        FileDownload download = new FileDownload(
                new StoredFile(fileId, "/files/" + fileId, "avatar.png", "image/png", 4, entity.getCreatedAt()),
                new ByteArrayInputStream(new byte[]{1, 2, 3, 4})
        );
        when(fileRepository.findById(fileId)).thenReturn(Optional.of(entity));
        when(fileStorage.provider()).thenReturn(StorageProvider.LOCAL);
        when(fileStorage.download(eq(entity), any(StoredFile.class))).thenReturn(download);

        assertThat(fileService.download(fileId)).isSameAs(download);
    }

    @Test
    void publicUrlRejectsMissingFile() {
        fileService = fileService();
        UUID fileId = UUID.randomUUID();
        when(fileRepository.findById(fileId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fileService.publicUrl(fileId))
                .isInstanceOf(FileNotFoundException.class);
    }

    private StoreFileRequest request() {
        return new StoreFileRequest(
                new UploadFile(
                        new UploadFileMetadata("avatar.png", null, null, "image/png", 4),
                        new ByteArrayInputStream(new byte[]{1, 2, 3, 4})
                ),
                "profile-pictures",
                "user-id",
                FileRules.defaultImage()
        );
    }

    private FileServiceImpl fileService() {
        return new FileServiceImpl(storageProperties(), fileStorage, fileRepository, fileMapper);
    }

    private StorageProperties storageProperties() {
        return new StorageProperties(
                StorageProvider.LOCAL,
                "http://localhost:8080",
                new StorageProperties.Local("uploads"),
                new StorageProperties.S3("users-bucket")
        );
    }

    private PhysicalStoredFile physicalFile(UUID id) {
        return new PhysicalStoredFile(
                StorageProvider.LOCAL,
                id + ".png",
                null,
                "profile-pictures/" + id + ".png",
                null,
                "/tmp/avatar.png",
                null
        );
    }

    private FileEntity entity(UUID fileId) {
        return FileEntity.builder()
                .id(fileId)
                .originalName("avatar.png")
                .storedName(fileId + ".png")
                .contentType("image/png")
                .sizeBytes(4)
                .storageProvider(StorageProvider.LOCAL)
                .storageKey("profile-pictures/" + fileId + ".png")
                .path("/tmp/avatar.png")
                .createdBy("user-id")
                .createdAt(Instant.now())
                .build();
    }
}
