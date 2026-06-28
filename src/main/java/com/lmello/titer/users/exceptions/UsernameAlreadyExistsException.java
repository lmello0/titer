package com.lmello.titer.users.exceptions;

import com.lmello.titer.shared.exception.DomainException;

public class UsernameAlreadyExistsException extends DomainException {
    public UsernameAlreadyExistsException(String username) {
        super("A user already exists with username: " + username);
    }
}
