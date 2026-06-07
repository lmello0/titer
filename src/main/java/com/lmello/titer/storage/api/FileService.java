package com.lmello.titer.storage.api;

import java.util.UUID;

public interface FileService {
    StoredFile store(StoreFileRequest request);

    String publicUrl(UUID fileId);

    void delete(UUID fileId);
}
