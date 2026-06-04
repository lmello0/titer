package com.lmello.titer.users.api;

public record LoginRequest(
        String identifier,
        String password
) {
}
