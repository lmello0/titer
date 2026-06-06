package com.lmello.titer.users;

import com.lmello.titer.users.api.UserResponse;

import java.util.Optional;

public interface UserService {
    Optional<UserResponse> findUserByUsernameOrEmail(String username, String email);
}
