package com.lmello.titer.storage.internal.services.storages;

import com.lmello.titer.storage.dto.upload.FileRules;
import com.lmello.titer.storage.dto.upload.UploadFile;
import com.lmello.titer.storage.dto.upload.UploadFileMetadata;
import com.lmello.titer.storage.internal.dto.PreparedStoredFile;
import com.lmello.titer.storage.internal.dto.StoreFileCommand;
import com.lmello.titer.storage.internal.services.FileValidator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.servlet.autoconfigure.MultipartProperties;

import java.io.ByteArrayInputStream;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileStorageSupportTest {

    private final FileStorageSupport support = new FileStorageSupport(new FileValidator(new MultipartProperties()));

    @Test
    void preparesStoredNameFromFileIdAndContentType() {
        UUID fileId = UUID.randomUUID();

        PreparedStoredFile preparedFile = support.prepare(command(fileId, "profile-pictures", null, null));

        assertThat(preparedFile.file()).isNotNull();
        assertThat(preparedFile.storedName()).isEqualTo(fileId + ".png");
        assertThat(preparedFile.storageKey()).isEqualTo("profile-pictures/" + fileId + ".png");
    }

    @Test
    void appliesPrefixAndSuffixToStoredName() {
        UUID fileId = UUID.randomUUID();

        PreparedStoredFile preparedFile = support.prepare(command(fileId, "profile-pictures", "prefix", "suffix"));

        assertThat(preparedFile.storedName()).isEqualTo("prefix_" + fileId + "_suffix.png");
        assertThat(preparedFile.storageKey()).isEqualTo("profile-pictures/prefix_" + fileId + "_suffix.png");
    }

    @Test
    void trimsLeadingAndTrailingSlashesFromPath() {
        UUID fileId = UUID.randomUUID();

        PreparedStoredFile preparedFile = support.prepare(command(fileId, "/profile-pictures/", null, null));

        assertThat(preparedFile.storageKey()).isEqualTo("profile-pictures/" + fileId + ".png");
    }

    @Test
    void returnsStoredNameAsStorageKeyWhenPathIsBlank() {
        UUID fileId = UUID.randomUUID();

        PreparedStoredFile preparedFile = support.prepare(command(fileId, " ", null, null));

        assertThat(preparedFile.storageKey()).isEqualTo(fileId + ".png");
    }

    @Test
    void validatesFileBeforePreparingIt() {
        StoreFileCommand command = new StoreFileCommand(
                UUID.randomUUID(),
                "profile-pictures",
                null,
                FileRules.defaultImage()
        );

        assertThatThrownBy(() -> support.prepare(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("File is empty");
    }

    private StoreFileCommand command(UUID fileId, String path, String namePrefix, String nameSuffix) {
        return new StoreFileCommand(
                fileId,
                path,
                new UploadFile(
                        new UploadFileMetadata("avatar.png", namePrefix, nameSuffix, "image/png", 4),
                        new ByteArrayInputStream(new byte[]{1, 2, 3, 4})
                ),
                FileRules.defaultImage()
        );
    }
}
