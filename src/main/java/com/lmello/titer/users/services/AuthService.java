package com.lmello.titer.users.services;

import com.lmello.titer.users.dto.AuthResponse;
import com.lmello.titer.users.dto.LoginRequest;
import com.lmello.titer.users.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
