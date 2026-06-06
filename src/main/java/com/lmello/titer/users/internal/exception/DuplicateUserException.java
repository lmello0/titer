package com.lmello.titer.users.internal.exception;

import com.lmello.titer.shared.exception.DomainException;

public class DuplicateUserException extends DomainException {
    public DuplicateUserException(String email, String username) {
        super("User " + email + "/" + username + " already exists");
    }
}
