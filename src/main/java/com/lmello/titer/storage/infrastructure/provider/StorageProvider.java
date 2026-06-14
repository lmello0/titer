package com.lmello.titer.storage.infrastructure.provider;

import com.lmello.titer.storage.api.command.StorageTarget;
import org.springframework.core.io.Resource;

import java.util.UUID;

public interface StorageProvider {

    String name();

    boolean supports(StorageTarget target);

    String store(String filename, String contentType, byte[] data);

    Resource load(String storageKey);

    void delete(String storageKey);

    String resolvePublicUrl(UUID fileId);
}
