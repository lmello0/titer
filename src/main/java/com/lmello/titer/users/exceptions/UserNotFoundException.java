package com.lmello.titer.users.exceptions;

import com.lmello.titer.shared.exception.DomainException;

public class UserNotFoundException extends DomainException {
    public UserNotFoundException() {
        super("User not found");
    }
}
