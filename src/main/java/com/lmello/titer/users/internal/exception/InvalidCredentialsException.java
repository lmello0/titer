package com.lmello.titer.users.internal.exception;

import com.lmello.titer.shared.exception.DomainException;

public class InvalidCredentialsException extends DomainException {
    public InvalidCredentialsException() {
        super("Invalid credentials");
    }
}
