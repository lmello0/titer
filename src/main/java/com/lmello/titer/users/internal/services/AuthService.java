package com.lmello.titer.users.internal.services;

import com.lmello.titer.security.JwtService;
import com.lmello.titer.users.api.AuthResponse;
import com.lmello.titer.users.api.LoginRequest;
import com.lmello.titer.users.api.RegisterRequest;
import com.lmello.titer.users.internal.entities.UserAuthEntity;
import com.lmello.titer.users.internal.entities.UserEntity;
import com.lmello.titer.users.internal.enums.AuthProvider;
import com.lmello.titer.users.internal.mapper.UserMapper;
import com.lmello.titer.users.internal.repositories.UserAuthRepository;
import com.lmello.titer.users.internal.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserAuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            // TODO: throw domain exception
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.existsByEmail(request.email())) {
            // TODO: throw domain exception
            throw new IllegalArgumentException("Email already exists");
        }

        UserEntity u = userRepository.save(
                UserEntity.builder()
                        .username(request.username())
                        .email(request.email())
                        .name(request.name())
                        .profilePicture(request.profilePicture())
                        .build()
        );

        String passwordHash = passwordEncoder.encode(request.password());
        authRepository.save(UserAuthEntity.local(u, passwordHash));

        String token = jwtService.generate(u.getId(), u.getUsername());

        return new AuthResponse(token, userMapper.toResponse(u));
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        UserAuthEntity auth = authRepository.findByUserUsernameAndIdProvider(request.identifier(), AuthProvider.LOCAL)
                .or(() -> authRepository.findByUserEmailAndIdProvider(request.identifier(), AuthProvider.LOCAL))
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), auth.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        UserEntity u = auth.getUser();
        String token = jwtService.generate(u.getId(), u.getUsername());

        return new AuthResponse(token, userMapper.toResponse(u));
    }
}
