package com.lmello.titer.users.api.exceptions;

import com.lmello.titer.shared.exception.DomainException;

public class UsernameAlreadyExistsException extends DomainException {
    public UsernameAlreadyExistsException(String username) {
        super("Username already in use: " + username);
    }
}
