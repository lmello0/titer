package com.lmello.titer.auth.enums;

import com.lmello.titer.auth.exceptions.UnsupportedProviderException;

import java.util.Locale;

public enum AuthProvider {
    LOCAL,
    GOOGLE;

    public static AuthProvider fromString(String raw) {
        try {
            return AuthProvider.valueOf(raw.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new UnsupportedProviderException(raw);
        }
    }
}
