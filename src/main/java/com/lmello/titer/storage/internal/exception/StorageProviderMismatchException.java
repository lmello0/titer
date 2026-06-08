package com.lmello.titer.storage.internal.exception;

import com.lmello.titer.storage.internal.enums.StorageProvider;

public class StorageProviderMismatchException extends RuntimeException {
    public StorageProviderMismatchException(StorageProvider expected, StorageProvider actual) {
        super("File is stored by " + expected + " but active storage provider is " + actual);
    }
}
