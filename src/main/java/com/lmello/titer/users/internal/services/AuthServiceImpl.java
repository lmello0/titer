package com.lmello.titer.users.internal.services;

import com.lmello.titer.security.JwtService;
import com.lmello.titer.users.api.AuthResponse;
import com.lmello.titer.users.api.LoginRequest;
import com.lmello.titer.users.api.RegisterRequest;
import com.lmello.titer.users.internal.entities.RoleEntity;
import com.lmello.titer.users.internal.entities.UserAuthEntity;
import com.lmello.titer.users.internal.entities.UserEntity;
import com.lmello.titer.users.internal.enums.AuthProvider;
import com.lmello.titer.users.internal.exception.DuplicateUserException;
import com.lmello.titer.users.internal.exception.InvalidCredentialsException;
import com.lmello.titer.users.internal.exception.RoleNotFoundException;
import com.lmello.titer.users.internal.mapper.AuthMapper;
import com.lmello.titer.users.internal.mapper.UserMapper;
import com.lmello.titer.users.internal.repositories.RoleRepository;
import com.lmello.titer.users.internal.repositories.UserAuthRepository;
import com.lmello.titer.users.internal.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserAuthRepository authRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final UserMapper userMapper;
    private final AuthMapper authMapper;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsernameOrEmail(request.username(), request.email())) {
            throw new DuplicateUserException();
        }

        RoleEntity defaultRole = roleRepository.findByRole(request.role())
                .orElseThrow(() -> new RoleNotFoundException(request.role()));

        UserEntity newUser = UserEntity.builder()
                .username(request.username())
                .email(request.email())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .profilePicture(request.profilePicture())
                .build();

        newUser.addRole(defaultRole);
        newUser = userRepository.save(newUser);

        String passwordHash = passwordEncoder.encode(request.password());

        authRepository.save(UserAuthEntity.local(newUser, passwordHash));

        Set<String> authorities = authMapper.toAuthorities(newUser);

        String token = jwtService.generateToken(
                newUser.getId(),
                newUser.getUsername(),
                newUser.getEmail(),
                authorities
        );

        return new AuthResponse(token, userMapper.toResponse(newUser));
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        UserAuthEntity auth =
                authRepository.findByUserUsernameAndIdProvider(request.identifier(), AuthProvider.LOCAL)
                        .or(() -> authRepository.findByUserEmailAndIdProvider(request.identifier(), AuthProvider.LOCAL))
                        .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), auth.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        UserEntity u = auth.getUser();
        Set<String> authorities = authMapper.toAuthorities(u);

        String token = jwtService.generateToken(u.getId(), u.getUsername(), u.getEmail(), authorities);

        return new AuthResponse(token, userMapper.toResponse(u));
    }
}
