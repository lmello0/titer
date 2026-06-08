package com.lmello.titer.storage.internal.services;

import com.lmello.titer.storage.dto.download.FileDownload;
import com.lmello.titer.storage.dto.file.StoredFile;
import com.lmello.titer.storage.dto.upload.FileRules;
import com.lmello.titer.storage.dto.upload.UploadFile;
import com.lmello.titer.storage.dto.upload.UploadFileMetadata;
import com.lmello.titer.storage.internal.dto.PhysicalStoredFile;
import com.lmello.titer.storage.internal.dto.StoreFileCommand;
import com.lmello.titer.storage.internal.entities.FileEntity;
import com.lmello.titer.storage.internal.enums.StorageProvider;
import com.lmello.titer.storage.internal.properties.StorageProperties;
import com.lmello.titer.storage.internal.services.storages.FileStorageSupport;
import com.lmello.titer.storage.internal.services.storages.LocalFileStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.servlet.autoconfigure.MultipartProperties;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LocalFileStorageTest {

    @TempDir
    Path tempDir;

    @Test
    void storesAndReadsInsideBasePath() throws Exception {
        LocalFileStorage storage = storage();
        UUID fileId = UUID.randomUUID();

        PhysicalStoredFile stored = storage.store(command(fileId, "profile-pictures"));

        assertThat(stored.provider()).isEqualTo(StorageProvider.LOCAL);
        assertThat(stored.storageKey()).isEqualTo("profile-pictures/" + fileId + ".png");
        assertThat(Path.of(stored.path())).startsWith(tempDir.toAbsolutePath().normalize());
        assertThat(Files.exists(Path.of(stored.path()))).isTrue();

        FileEntity entity = FileEntity.builder()
                .id(fileId)
                .originalName("avatar.png")
                .contentType("image/png")
                .sizeBytes(4)
                .path(stored.path())
                .build();

        StoredFile metadata = new StoredFile(fileId, "/files/" + fileId, "avatar.png", "image/png", 4, Instant.now());
        FileDownload resource = storage.download(entity, metadata);

        assertThat(resource.content().readAllBytes()).isEqualTo(new byte[]{1, 2, 3, 4});
        assertThat(resource.metadata()).isSameAs(metadata);
    }

    @Test
    void rejectsPathTraversal() {
        LocalFileStorage storage = storage();

        assertThatThrownBy(() -> storage.store(command(UUID.randomUUID(), "../outside")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("File path escapes storage base path");
    }

    @Test
    void deletesStoredFile() {
        LocalFileStorage storage = storage();
        PhysicalStoredFile stored = storage.store(command(UUID.randomUUID(), "profile-pictures"));

        storage.delete(stored);

        assertThat(Files.exists(Path.of(stored.path()))).isFalse();
    }

    private LocalFileStorage storage() {
        StorageProperties properties = new StorageProperties(
                StorageProvider.LOCAL,
                "http://localhost:8080",
                new StorageProperties.Local(tempDir.toString()),
                new StorageProperties.S3("users-bucket")
        );

        return new LocalFileStorage(properties, new FileStorageSupport(new FileValidator(new MultipartProperties())));
    }

    private StoreFileCommand command(UUID fileId, String path) {
        return new StoreFileCommand(
                fileId,
                path,
                new UploadFile(
                        new UploadFileMetadata("avatar.png", null, null, "image/png", 4),
                        new ByteArrayInputStream(new byte[]{1, 2, 3, 4})
                ),
                FileRules.defaultImage()
        );
    }
}
