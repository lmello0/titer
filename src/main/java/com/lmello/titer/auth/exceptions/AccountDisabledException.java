package com.lmello.titer.auth.exceptions;

import com.lmello.titer.shared.exception.DomainException;

public class AccountDisabledException extends DomainException {
    public AccountDisabledException(String message) {
        super(message);
    }
}
