package com.lmello.titer.storage.infrastructure.provider;

import com.lmello.titer.shared.properties.AwsProperties;
import com.lmello.titer.storage.api.command.StorageTarget;
import com.lmello.titer.storage.exceptions.StorageProviderException;
import com.lmello.titer.storage.properties.StorageProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
@ConditionalOnProperty(name = "app.storage.s3.enabled", havingValue = "true")
public class S3StorageProvider implements StorageProvider {

    private final String baseUrl;
    private final String bucket;
    private final boolean usePresignedUrls;
    private final Duration presignedUrlExpiry;

    private final S3Client s3Client;
    private final S3Presigner presigner;


    public S3StorageProvider(
            AwsProperties awsProperties,
            StorageProperties storageProperties
    ) {
        StorageProperties.S3Properties s3 = storageProperties.s3();

        Region region = Region.of(awsProperties.region());

        var s3ClientBuilder = S3Client.builder()
                .region(region)
                .credentialsProvider(DefaultCredentialsProvider.builder().build())
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(awsProperties.pathStyleAccessEnabled())
                        .build()
                );

        var s3PresignerBuilder = S3Presigner.builder()
                .region(region)
                .credentialsProvider(DefaultCredentialsProvider.builder().build())
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(awsProperties.pathStyleAccessEnabled())
                        .build()
                );

        if (awsProperties.endpointUrl() != null && !awsProperties.endpointUrl().isBlank()) {
            URI awsEndpoint = URI.create(awsProperties.endpointUrl());

            s3ClientBuilder.endpointOverride(awsEndpoint);
            s3PresignerBuilder.endpointOverride(awsEndpoint);
        }

        this.s3Client = s3ClientBuilder.build();
        this.presigner = s3PresignerBuilder.build();

        this.bucket = s3.bucket();
        this.baseUrl = storageProperties.baseUrl();
        this.usePresignedUrls = s3.usePresignedUrls();
        this.presignedUrlExpiry = s3.presignedUrlExpiry();

        log.info("S3StorageProvider initialized [bucket={}, region={}, presigned={}, endpointUrl={}]",
                bucket, region, usePresignedUrls, awsProperties.endpointUrl());
    }

    @Override
    public String name() {
        return "s3";
    }

    @Override
    public boolean supports(StorageTarget target) {
        return target == StorageTarget.BUCKET || target == StorageTarget.DEFAULT;
    }

    @Override
    public String store(String filename, String contentType, byte[] data) {
        String key = UUID.ofEpochMillis(Instant.now().toEpochMilli()) + "/" + filename;

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .contentType(contentType)
                        .contentLength((long) data.length)
                        .build(),
                RequestBody.fromBytes(data)
        );

        log.debug("Stored S3 object [bucket={}, key={}, size={}b]", bucket, key, data.length);
        return key;
    }

    @Override
    public Resource load(String storageKey) {
        try {
            var response = s3Client.getObject(
                    GetObjectRequest.builder()
                            .bucket(bucket)
                            .key(storageKey)
                            .build()
            );

            return new InputStreamResource(response);
        } catch (NoSuchKeyException e) {
            throw new StorageProviderException("S3 object not found: " + storageKey, e);
        } catch (S3Exception e) {
            throw new StorageProviderException("Failed to load S3 object: " + storageKey, e);
        }
    }

    @Override
    public void delete(String storageKey) {
        try {
            s3Client.deleteObject(
                    DeleteObjectRequest.builder()
                            .bucket(bucket)
                            .key(storageKey)
                            .build()
            );

            log.debug("Deleted S3 object [bucket={}, key={}]", bucket, storageKey);
        } catch (S3Exception e) {
            throw new StorageProviderException("Failed to delete S3 object: " + storageKey, e);
        }
    }

    @Override
    public String resolvePublicUrl(UUID fileId) {
        return baseUrl + "/" + fileId;
    }

    public String presign(String storageKey) {
        var presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(presignedUrlExpiry)
                .getObjectRequest(r -> r.bucket(bucket).key(storageKey))
                .build();

        return presigner
                .presignGetObject(presignRequest)
                .url()
                .toString();
    }
}
