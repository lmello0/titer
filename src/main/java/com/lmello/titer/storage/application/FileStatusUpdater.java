package com.lmello.titer.storage.application;

import com.lmello.titer.storage.api.FileProcessor;
import com.lmello.titer.storage.api.representation.FileRepresentation;
import com.lmello.titer.storage.events.FileFailedEvent;
import com.lmello.titer.storage.events.FileReadyEvent;
import com.lmello.titer.storage.infrastructure.persistence.FileRepository;
import com.lmello.titer.storage.mappers.FileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileStatusUpdater {

    private final FileMapper mapper;
    private final FileRepository repository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    void markProcessing(UUID fileId) {
        repository.findById(fileId).ifPresent(file -> {
            file.markProcessing();
            repository.save(file);
        });
    }

    @Transactional
    void markReady(UUID fileId, String storageKey, String publicUrl, FileProcessor.ProcessingContext ctx) {
        repository.findById(fileId).ifPresent(file -> {
            file.markReady(storageKey, publicUrl, ctx.data().length, ctx.contentType(), ctx.mediaMetadata());
            repository.save(file);

            FileRepresentation repr = mapper.toRepresentation(file);
            eventPublisher.publishEvent(FileReadyEvent.from(repr));

            log.info("File READY [id={}, url={}]", fileId, publicUrl);
        });
    }

    @Transactional
    void markFailed(UUID fileId, String reason) {
        repository.findById(fileId).ifPresent(file -> {
            file.markFailed(reason);
            repository.save(file);
            eventPublisher.publishEvent(FileFailedEvent.of(fileId, reason));
            log.warn("File FAILED [id={}, reason={}]", fileId, reason);
        });
    }
}
