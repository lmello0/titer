package com.lmello.titer.users.exceptions;

import com.lmello.titer.shared.exception.DomainException;

public class EmailAlreadyExistsException extends DomainException {
    public EmailAlreadyExistsException(String email) {
        super("A user already exists with email: " + email);
    }
}
