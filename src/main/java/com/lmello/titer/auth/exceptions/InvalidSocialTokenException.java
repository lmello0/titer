package com.lmello.titer.auth.exceptions;

import com.lmello.titer.shared.exception.DomainException;

public class InvalidSocialTokenException extends DomainException {
    public InvalidSocialTokenException(String message) {
        super(message);
    }

    public InvalidSocialTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
