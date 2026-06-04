package com.lmello.titer.users.internal.controllers;

import com.lmello.titer.security.CustomUserPrincipal;
import com.lmello.titer.users.api.UpdateUserRequest;
import com.lmello.titer.users.api.UserResponse;
import com.lmello.titer.users.internal.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    UserResponse get(@PathVariable UUID id) {
        return userService.getUser(id);
    }

    @PutMapping("/{id}")
    UserResponse update(@PathVariable UUID id, @RequestBody @Valid UpdateUserRequest request) {
        return userService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable UUID id) {
        userService.delete(id);
    }

    @GetMapping("/me")
    UserResponse me(@AuthenticationPrincipal CustomUserPrincipal principal) {
        return userService.getUser(principal.userId());
    }
}
