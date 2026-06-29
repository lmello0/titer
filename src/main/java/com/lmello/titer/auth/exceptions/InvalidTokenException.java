package com.lmello.titer.auth.exceptions;

import com.lmello.titer.shared.exception.DomainException;

public class InvalidTokenException extends DomainException {
    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
