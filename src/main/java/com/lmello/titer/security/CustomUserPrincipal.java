package com.lmello.titer.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public record CustomUserPrincipal(
        UUID userId,
        String username,
        Collection<? extends GrantedAuthority> authorities
) implements UserDetails {

    public CustomUserPrincipal(UUID userId, String username) {
        this(userId, username, List.of());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
