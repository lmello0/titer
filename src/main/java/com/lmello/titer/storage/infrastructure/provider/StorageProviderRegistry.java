package com.lmello.titer.storage.infrastructure.provider;

import com.lmello.titer.storage.api.command.StorageTarget;
import com.lmello.titer.storage.exceptions.StorageProviderException;
import com.lmello.titer.storage.properties.StorageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StorageProviderRegistry {

    private final List<StorageProvider> providers;
    private final StorageProperties properties;

    public StorageProvider resolve(StorageTarget target) {
        return providers.stream()
                .filter(p -> {
                    if (target == StorageTarget.DEFAULT) {
                        return p.name().equals(properties.defaultProviderName());
                    }

                    return p.supports(target);
                })
                .findFirst()
                .orElseThrow(() -> new StorageProviderException(
                        "No provider found for target: " + target +
                                " (default: " + properties.defaultProviderName() + ")"
                ));
    }
}
