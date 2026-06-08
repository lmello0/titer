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
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;

@Service
@ConditionalOnProperty(
        name = "app.storage.provider",
        havingValue = "bucket"
)
@RequiredArgsConstructor
public class S3FileStorage implements FileStorage {

    private final S3Client s3Client;
    private final StorageProperties properties;
    private final FileStorageSupport support;

    @Override
    public StorageProvider provider() {
        return StorageProvider.BUCKET;
    }

    @Override
    public PhysicalStoredFile store(StoreFileCommand command) {
        PreparedStoredFile preparedFile = support.prepare(command);

        String bucket = properties.s3().bucket();
        String url = properties.publicBaseUrl() + "/" + preparedFile.storageKey();

        try (InputStream inputStream = preparedFile.file().content()) {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(preparedFile.storageKey())
                    .contentType(preparedFile.file().metadata().contentType())
                    .contentLength(preparedFile.file().metadata().sizeBytes())
                    .build();

            s3Client.putObject(
                    request,
                    RequestBody.fromInputStream(inputStream, preparedFile.file().metadata().sizeBytes())
            );

            return new PhysicalStoredFile(
                    provider(),
                    preparedFile.storedName(),
                    bucket,
                    preparedFile.storageKey(),
                    url,
                    null,
                    null
            );
        } catch (IOException exception) {
            throw new IllegalStateException("Could not upload file to S3", exception);
        }
    }

    @Override
    public FileDownload download(FileEntity file, StoredFile metadata) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(file.getBucket())
                .key(file.getStorageKey())
                .build();

        return new FileDownload(
                metadata,
                s3Client.getObject(request)
        );
    }

    @Override
    public void delete(PhysicalStoredFile file) {
        if (file == null || file.storageKey() == null || file.storageKey().isBlank()) return;

        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucket(file.bucket())
                .key(file.storageKey())
                .build();

        s3Client.deleteObject(request);
    }
}
