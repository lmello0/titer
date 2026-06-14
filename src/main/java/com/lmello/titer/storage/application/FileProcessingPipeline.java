package com.lmello.titer.storage.application;

import com.lmello.titer.storage.api.FileProcessor;
import com.lmello.titer.storage.api.FileProcessor.ProcessingContext;
import com.lmello.titer.storage.api.command.StoreFileCommand;
import com.lmello.titer.storage.api.representation.FileRepresentation;
import com.lmello.titer.storage.exceptions.ProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class FileProcessingPipeline {

    private final List<FileProcessor> processors;

    FileProcessingPipeline(List<FileProcessor> processors) {
        this.processors = processors;
        log.info("Storage pipeline initialized with {} processors: {}",
                processors.size(),
                processors.stream()
                        .map(p -> p.getClass().getSimpleName())
                        .toList());
    }

    ProcessingContext execute(StoreFileCommand command, byte[] rawData) throws ProcessingException {
        ProcessingContext ctx = new ProcessingContext(
                command,
                rawData,
                command.contentType(),
                FileRepresentation.MediaMetadata.empty()
        );

        for (FileProcessor processor : processors) {
            if (!processor.supports(command)) {
                log.trace("Skipping {} - does not support command", processor.getClass().getSimpleName());
                continue;
            }

            log.debug("Running processor: {}", processor.getClass().getSimpleName());
            ctx = processor.process(ctx);
        }

        return ctx;
    }
}
