package com.lmello.titer.storage.internal.services;

import com.lmello.titer.storage.internal.enums.StorageProvider;
import com.lmello.titer.storage.internal.properties.StorageProperties;
import com.lmello.titer.storage.internal.services.storages.DatabaseFileStorage;
import com.lmello.titer.storage.internal.services.storages.FileStorage;
import com.lmello.titer.storage.internal.services.storages.FileStorageSupport;
import com.lmello.titer.storage.internal.services.storages.LocalFileStorage;
import com.lmello.titer.storage.internal.services.storages.S3FileStorage;
import org.junit.jupiter.api.Test;
import org.springframework.boot.servlet.autoconfigure.MultipartProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import software.amazon.awssdk.services.s3.S3Client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class FileStorageProviderSelectionTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withBean(FileValidator.class, () -> new FileValidator(new MultipartProperties()))
            .withBean(StorageProperties.class, () -> new StorageProperties(
                    null,
                    "http://localhost:8080",
                    new StorageProperties.Local("uploads"),
                    new StorageProperties.S3("users-bucket")
            ))
            .withBean(S3Client.class, () -> mock(S3Client.class))
            .withUserConfiguration(StorageImplementations.class);

    @Test
    void localProviderSelectsLocalStorage() {
        contextRunner
                .withPropertyValues("app.storage.provider=local")
                .run(context -> {
                    assertThat(context).hasSingleBean(FileStorage.class);
                    assertThat(context.getBean(FileStorage.class).provider()).isEqualTo(StorageProvider.LOCAL);
                });
    }

    @Test
    void databaseProviderSelectsDatabaseStorage() {
        contextRunner
                .withPropertyValues("app.storage.provider=database")
                .run(context -> {
                    assertThat(context).hasSingleBean(FileStorage.class);
                    assertThat(context.getBean(FileStorage.class).provider()).isEqualTo(StorageProvider.DATABASE);
                });
    }

    @Test
    void bucketProviderSelectsS3Storage() {
        contextRunner
                .withPropertyValues("app.storage.provider=bucket")
                .run(context -> {
                    assertThat(context).hasSingleBean(FileStorage.class);
                    assertThat(context.getBean(FileStorage.class).provider()).isEqualTo(StorageProvider.BUCKET);
                });
    }

    @Configuration
    @Import({
            LocalFileStorage.class,
            DatabaseFileStorage.class,
            S3FileStorage.class,
            FileStorageSupport.class
    })
    static class StorageImplementations {
    }
}
