package com.lmello.titer.storage.internal.services.storages;

import com.lmello.titer.storage.dto.download.FileDownload;
import com.lmello.titer.storage.dto.file.StoredFile;
import com.lmello.titer.storage.internal.dto.PhysicalStoredFile;
import com.lmello.titer.storage.internal.dto.StoreFileCommand;
import com.lmello.titer.storage.internal.entities.FileEntity;
import com.lmello.titer.storage.internal.enums.StorageProvider;

public interface FileStorage {
    StorageProvider provider();

    PhysicalStoredFile store(StoreFileCommand command);

    FileDownload download(FileEntity file, StoredFile metadata);

    void delete(PhysicalStoredFile file);
}
