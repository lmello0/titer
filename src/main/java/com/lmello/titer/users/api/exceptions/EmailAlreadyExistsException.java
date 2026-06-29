package com.lmello.titer.users.api.exceptions;

import com.lmello.titer.shared.exception.DomainException;

public class EmailAlreadyExistsException extends DomainException {
    public EmailAlreadyExistsException(String email) {
        super("Email already in use: " + email);
    }
}
