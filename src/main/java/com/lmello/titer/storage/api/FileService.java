package com.lmello.titer.storage.api;

import com.lmello.titer.storage.dto.download.FileDownload;
import com.lmello.titer.storage.dto.file.StoredFile;
import com.lmello.titer.storage.dto.upload.StoreFileRequest;

import java.util.UUID;

public interface FileService {
    StoredFile store(StoreFileRequest request);

    String publicUrl(UUID fileId);

    FileDownload download(UUID fileId);

    void delete(UUID fileId);

    StoredFile metadata(UUID fileId);
}
