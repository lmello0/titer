package com.lmello.titer.users.services;

import com.lmello.titer.users.dto.UserResponse;

import java.util.Optional;

public interface UserService {
    Optional<UserResponse> findUserByUsernameOrEmail(String username, String email);
}
