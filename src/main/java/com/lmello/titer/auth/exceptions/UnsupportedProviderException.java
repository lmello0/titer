package com.lmello.titer.auth.exceptions;

import com.lmello.titer.shared.exception.DomainException;

public class UnsupportedProviderException extends DomainException {
    public UnsupportedProviderException(String provider) {
        super("Unsupported authentication provider: " + provider);
    }
}
