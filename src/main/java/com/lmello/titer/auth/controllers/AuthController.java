package com.lmello.titer.auth.controllers;

import com.lmello.titer.auth.api.AuthService;
import com.lmello.titer.auth.controllers.representation.*;
import com.lmello.titer.auth.enums.AuthProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthService.Tokens tokens = authService.registerLocal(
                request.username(),
                request.email(),
                request.password(),
                request.firstName(),
                request.lastName()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(TokenResponse.from(tokens));
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        return TokenResponse.from(authService.login(request.identifier(), request.password()));
    }

    @PostMapping("/social/{provider}")
    public TokenResponse socialLogin(
            @PathVariable String provider,
            @Valid @RequestBody SocialLoginRequest request
    ) {
        AuthProvider resolved = AuthProvider.fromString(provider);
        return TokenResponse.from(authService.socialLogin(resolved, request.token()));
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(@Valid @RequestBody RefreshRequest request) {
        return TokenResponse.from(authService.refresh(request.refreshToken()));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshRequest request) {
        authService.logout(request.refreshToken());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout-all")
    public ResponseEntity<Void> logoutAll(@AuthenticationPrincipal Jwt jwt) {
        authService.logoutEverywhere(UUID.fromString(jwt.getSubject()));

        return ResponseEntity.noContent().build();
    }
}
