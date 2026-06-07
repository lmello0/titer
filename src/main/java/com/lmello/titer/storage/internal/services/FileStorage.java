package com.lmello.titer.storage.internal.services;

import com.lmello.titer.storage.api.PhysicalStoredFile;
import com.lmello.titer.storage.api.StoreFileCommand;
import com.lmello.titer.storage.internal.enums.StorageProvider;

public interface FileStorage {
    StorageProvider provider();

    PhysicalStoredFile store(StoreFileCommand command);

    void delete(PhysicalStoredFile file);
}
