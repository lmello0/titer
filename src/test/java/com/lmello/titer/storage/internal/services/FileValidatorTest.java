package com.lmello.titer.storage.internal.services;

import com.lmello.titer.storage.dto.upload.FileRules;
import com.lmello.titer.storage.dto.upload.UploadFile;
import com.lmello.titer.storage.dto.upload.UploadFileMetadata;
import org.junit.jupiter.api.Test;
import org.springframework.boot.servlet.autoconfigure.MultipartProperties;
import org.springframework.util.unit.DataSize;

import java.io.ByteArrayInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileValidatorTest {

    private final MultipartProperties multipartProperties = new MultipartProperties();
    private final FileValidator validator = new FileValidator(multipartProperties);

    @Test
    void acceptsAllowedContentTypeWithinEffectiveMaxSize() {
        multipartProperties.setMaxFileSize(DataSize.ofBytes(10));

        UploadFile file = file("avatar.png", "image/png", 4);

        validator.validate(file, FileRules.defaultImage());

        assertThat(validator.extensionFrom(file)).isEqualTo("png");
    }

    @Test
    void rejectsBlockedContentType() {
        UploadFile file = file("avatar.gif", "image/gif", 4);

        assertThatThrownBy(() -> validator.validate(file, FileRules.defaultImage()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("File content type is not allowed");
    }

    @Test
    void rejectsFilesAboveEffectiveMaxSize() {
        multipartProperties.setMaxFileSize(DataSize.ofBytes(3));
        UploadFile file = file("avatar.png", "image/png", 4);

        assertThatThrownBy(() -> validator.validate(file, FileRules.defaultImage()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("File must be less than or equal to 3 bytes");
    }

    @Test
    void rejectsMissingContentTypeAndContent() {
        UploadFile missingType = file("avatar.png", null, 4);
        UploadFile missingContent = new UploadFile(metadata("avatar.png", "image/png", 4), null);

        assertThatThrownBy(() -> validator.validate(missingType, FileRules.defaultImage()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("File content type is required");
        assertThatThrownBy(() -> validator.validate(missingContent, FileRules.defaultImage()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("File content is required");
    }

    @Test
    void rejectsNegativeSize() {
        UploadFile file = new UploadFile(metadata("avatar.png", "image/png", -1), new ByteArrayInputStream(new byte[0]));

        assertThatThrownBy(() -> validator.validate(file, FileRules.defaultImage()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("File size must be greater than or equal to 0 bytes");
    }

    private UploadFile file(String name, String contentType, long size) {
        return new UploadFile(
                metadata(name, contentType, size),
                new ByteArrayInputStream(new byte[(int) Math.max(size, 0)])
        );
    }

    private UploadFileMetadata metadata(String name, String contentType, long size) {
        return new UploadFileMetadata(name, null, null, contentType, size);
    }
}
