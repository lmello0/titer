package com.lmello.titer.users.services;

import com.lmello.titer.users.dto.AuthResponse;
import com.lmello.titer.users.dto.LoginRequest;
import com.lmello.titer.users.dto.RegisterRequest;
import com.lmello.titer.users.entities.UserEntity;
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
