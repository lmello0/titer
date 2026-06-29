package com.lmello.titer.users.api.exceptions;

import com.lmello.titer.shared.exception.DomainException;

import java.util.UUID;

public class UserNotFoundException extends DomainException {
    public UserNotFoundException(UUID id) {
        super("User not found: " + id);
    }
}
