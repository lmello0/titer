package com.lmello.titer.storage.internal.services;

import com.lmello.titer.storage.api.PhysicalStoredFile;
import com.lmello.titer.storage.api.StoreFileCommand;
import com.lmello.titer.storage.internal.enums.StorageProvider;
import com.lmello.titer.storage.internal.properties.StorageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
@ConditionalOnProperty(
        name = "app.storage.provider",
        havingValue = "s3"
)
@RequiredArgsConstructor
public class S3FileStorage implements FileStorage {

    private final S3Client s3Client;
    private final StorageProperties properties;
    private final FileValidator fileValidator;

    @Override
    public StorageProvider provider() {
        return StorageProvider.BUCKET;
    }

    @Override
    public PhysicalStoredFile store(StoreFileCommand command) {
        MultipartFile file = command.file();

        fileValidator.validate(file, command.rules());

        String extension = fileValidator.extensionFrom(file);
        String storedName = "%s.%s".formatted(command.fileId(), extension);

        String storageKey = "%s/%s".formatted(normalizePath(command.path()), storedName);

        String bucket = properties.s3().bucket();
        String url = properties.s3().publicBaseUrl() + "/" + storageKey;

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(storageKey)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();

            s3Client.putObject(
                    request,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );

            return new PhysicalStoredFile(
                    provider(),
                    storedName,
                    bucket,
                    storageKey,
                    url,
                    null,
                    null
            );
        } catch (IOException exception) {
            throw new IllegalStateException("Could not upload file to S3", exception);
        }
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

    private String normalizePath(String path) {
        return path.replaceAll("^/+", "").replaceAll("/+$", "");
    }
}
