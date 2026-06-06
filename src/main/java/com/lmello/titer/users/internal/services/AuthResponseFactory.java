package com.lmello.titer.users.internal.services;

import com.lmello.titer.security.JwtService;
import com.lmello.titer.users.api.AuthResponse;
import com.lmello.titer.users.internal.entities.UserEntity;
import com.lmello.titer.users.internal.mapper.AuthMapper;
import com.lmello.titer.users.internal.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class AuthResponseFactory {

    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final AuthMapper authMapper;

    public AuthResponse create(UserEntity user) {
        Set<String> authorities = authMapper.toAuthorities(user);

        String token = jwtService.generateToken(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                authorities
        );

        return new AuthResponse(token, userMapper.toResponse(user));
    }
}
