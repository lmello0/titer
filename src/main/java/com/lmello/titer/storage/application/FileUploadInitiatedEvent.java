package com.lmello.titer.storage.application;

import com.lmello.titer.storage.api.command.StoreFileCommand;

import java.util.UUID;

public record FileUploadInitiatedEvent(UUID fileId, StoreFileCommand command) {
}
