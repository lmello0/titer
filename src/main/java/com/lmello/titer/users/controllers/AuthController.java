package com.lmello.titer.users.controllers;

import com.lmello.titer.users.services.AuthService;
import com.lmello.titer.users.dto.AuthResponse;
import com.lmello.titer.users.dto.LoginRequest;
import com.lmello.titer.users.dto.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(
            value = "/signup",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    ResponseEntity<AuthResponse> register(@ModelAttribute @Valid RegisterRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(request));
    }

    @PostMapping("/signin")
    ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity
                .ok(authService.login(request));
    }
}
