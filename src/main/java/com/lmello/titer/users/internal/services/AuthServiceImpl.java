package com.lmello.titer.users.internal.services;

import com.lmello.titer.users.AuthService;
import com.lmello.titer.users.api.AuthResponse;
import com.lmello.titer.users.api.LoginRequest;
import com.lmello.titer.users.api.RegisterRequest;
import com.lmello.titer.users.internal.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRegistrationService userRegistrationService;
    private final LocalCredentialService localCredentialService;

    private final AuthResponseFactory authResponseFactory;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        UserEntity newUser = userRegistrationService.createLocalUser(request);

        localCredentialService.create(newUser, request.password());

        return authResponseFactory.create(newUser);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        UserEntity user = localCredentialService.authenticate(request);
        return authResponseFactory.create(user);
    }
}
