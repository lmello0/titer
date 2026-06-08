package com.lmello.titer.storage.internal.exception;

public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException() {
        super("File not found");
    }
}
