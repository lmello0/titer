package com.lmello.titer.storage.api;

import com.lmello.titer.storage.api.command.StoreFileCommand;
import com.lmello.titer.storage.api.representation.FileRepresentation;
import com.lmello.titer.storage.exceptions.ProcessingException;

public interface FileProcessor {

    boolean supports(StoreFileCommand command);

    ProcessingContext process(ProcessingContext ctx) throws ProcessingException;

    record ProcessingContext(
            StoreFileCommand command,
            byte[] data,
            String contentType,
            FileRepresentation.MediaMetadata mediaMetadata
    ) {
        public ProcessingContext withData(byte[] newData) {
            return new ProcessingContext(command, newData, contentType, mediaMetadata);
        }

        public ProcessingContext withContentType(String newContentType) {
            return new ProcessingContext(command, data, newContentType, mediaMetadata);
        }

        public ProcessingContext withMediaMetadata(FileRepresentation.MediaMetadata newMetadata) {
            return new ProcessingContext(command, data, contentType, newMetadata);
        }
    }
}
