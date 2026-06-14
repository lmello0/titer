package com.lmello.titer.storage.api;

import com.lmello.titer.storage.api.command.StoreFileCommand;
import com.lmello.titer.storage.api.representation.FileRepresentation;
import org.springframework.core.io.Resource;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

public interface StorageService {
    FileRepresentation store(StoreFileCommand command);

    Optional<FileRepresentation> findById(UUID fileId);

    Resource load(UUID fileId);

    void delete(UUID fileId);

    URI publicUrl(UUID fileId);
}
