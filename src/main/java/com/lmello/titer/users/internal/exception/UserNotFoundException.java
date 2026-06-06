package com.lmello.titer.users.internal.exception;

import com.lmello.titer.shared.exception.DomainException;

import java.util.UUID;

public class UserNotFoundException extends DomainException {
    public UserNotFoundException(UUID id) {
        super("User " + id + " not found");
    }
}
