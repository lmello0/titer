package com.lmello.titer.users.api.exceptions;

import com.lmello.titer.shared.exception.DomainException;

public class EmailVerificationException extends DomainException {
    public EmailVerificationException(String message) {
        super(message);
    }
}
