package com.lmello.titer.users.exception;

import com.lmello.titer.shared.exception.DomainException;

public class InvalidCredentialsException extends DomainException {
    public InvalidCredentialsException() {
        super("Invalid credentials");
    }
}
