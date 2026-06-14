package com.lmello.titer.storage.exceptions;

public class StorageProviderException extends RuntimeException {
    public StorageProviderException(String message) {
        super(message);
    }

    public StorageProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}
