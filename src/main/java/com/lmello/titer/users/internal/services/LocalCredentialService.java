package com.lmello.titer.users.internal.services;

import com.lmello.titer.users.api.LoginRequest;
import com.lmello.titer.users.internal.entities.UserAuthEntity;
import com.lmello.titer.users.internal.entities.UserEntity;
import com.lmello.titer.users.internal.enums.AuthProvider;
import com.lmello.titer.users.internal.exception.InvalidCredentialsException;
import com.lmello.titer.users.internal.repositories.UserAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocalCredentialService {

    private final UserAuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    public void create(UserEntity user, String rawPassword) {
        String passwordHash = passwordEncoder.encode(rawPassword);
        authRepository.save(UserAuthEntity.local(user, passwordHash));
    }

    public UserEntity authenticate(LoginRequest request) {
        UserAuthEntity auth = authRepository.findByUserUsernameAndIdProvider(request.identifier(), AuthProvider.LOCAL)
                .or(() -> authRepository.findByUserEmailAndIdProvider(request.identifier(), AuthProvider.LOCAL))
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), auth.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        return auth.getUser();
    }
}
