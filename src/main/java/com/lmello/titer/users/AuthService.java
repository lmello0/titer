package com.lmello.titer.users;

import com.lmello.titer.users.api.AuthResponse;
import com.lmello.titer.users.api.LoginRequest;
import com.lmello.titer.users.api.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
