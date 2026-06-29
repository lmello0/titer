package com.lmello.titer.storage.application;

import com.lmello.titer.storage.api.FileProcessor.ProcessingContext;
import com.lmello.titer.storage.api.StorageService;
import com.lmello.titer.storage.api.command.StorageTarget;
import com.lmello.titer.storage.api.command.StoreFileCommand;
import com.lmello.titer.storage.api.representation.FileRepresentation;
import com.lmello.titer.storage.api.representation.FileStatus;
import com.lmello.titer.storage.entities.FileEntity;
import com.lmello.titer.storage.exceptions.StorageProviderException;
import com.lmello.titer.storage.infrastructure.persistence.FileRepository;
import com.lmello.titer.storage.infrastructure.provider.StorageProvider;
import com.lmello.titer.storage.infrastructure.provider.StorageProviderRegistry;
import com.lmello.titer.storage.mappers.FileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final FileRepository repository;
    private final FileProcessingPipeline pipeline;
    private final StorageProviderRegistry providerRegistry;
    private final ApplicationEventPublisher eventPublisher;
    private final FileMapper mapper;
    private final FileStatusUpdater statusUpdater;

    @Override
    @Transactional
    public FileRepresentation store(StoreFileCommand command) {
        StorageProvider provider = providerRegistry.resolve(command.target());

        UUID fileId = UUID.ofEpochMillis(Instant.now().toEpochMilli());
        String publicUrl = provider.resolvePublicUrl(fileId);

        FileEntity file = FileEntity.pending(
                fileId,
                publicUrl,
                command.filename(),
                command.contentType(),
                command.contentLength(),
                provider.name(),
                command.metadata()
        );
        repository.save(file);

        eventPublisher.publishEvent(new FileUploadInitiatedEvent(file.getId(), command));

        log.info("File '{}' accepted as PENDING [id={}]", command.filename(), file.getId());
        return mapper.toRepresentation(file);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FileRepresentation> findById(UUID fileId) {
        return repository.findById(fileId)
                .map(mapper::toRepresentation);
    }

    @Override
    @Transactional(readOnly = true)
    public Resource load(UUID fileId) {
        FileEntity file = repository.findByIdAndStatus(fileId, FileStatus.READY)
                .orElseThrow(() -> new StorageProviderException("No READY file found with id: " + fileId));

        StorageProvider provider = providerRegistry.resolve(resolveTarget(file.getProvider()));

        return provider.load(file.getStorageKey());
    }

    @Override
    @Transactional
    public void delete(UUID fileId) {
        repository.findById(fileId)
                .ifPresent(file -> {
                    try {
                        StorageProvider provider = providerRegistry.resolve(resolveTarget(file.getProvider()));
                        provider.delete(file.getStorageKey());
                    } catch (Exception e) {
                        log.warn("Failed to delete file from provider, proceeding with DB deletion: {}", e.getMessage());
                    }

                    repository.delete(file);
                    log.info("File deleted [id={}]", fileId);
                });
    }

    @Override
    public URI publicUrl(UUID fileId) {
        return repository.findById(fileId)
                .map(entity -> URI.create(entity.getPublicUrl()))
                .orElse(null);
    }


    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void processFile(FileUploadInitiatedEvent event) {
        UUID fileId = event.fileId();
        log.info("Starting async processing for file [id={}]", fileId);

        statusUpdater.markProcessing(fileId);

        try {
            StoreFileCommand command = event.command();
            byte[] rawData = command.content().readAllBytes();

            ProcessingContext ctx = pipeline.execute(command, rawData);

            StorageProvider provider = providerRegistry.resolve(command.target());
            String storageKey = provider.store(command.filename(), command.contentType(), ctx.data());
            String publicUrl = provider.resolvePublicUrl(fileId);

            statusUpdater.markReady(fileId, storageKey, publicUrl, ctx);

        } catch (IOException e) {
            log.error("I/O error while processing file [id={}]: {}", fileId, e.getMessage(), e);
            statusUpdater.markFailed(fileId, "I/O error: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while processing file [id={}]: {}", fileId, e.getMessage(), e);
            statusUpdater.markFailed(fileId, e.getMessage());
        }
    }


    private StorageTarget resolveTarget(String providerName) {
        return switch (providerName) {
            case "s3" -> StorageTarget.BUCKET;
            default -> StorageTarget.LOCAL;
        };
    }
}
