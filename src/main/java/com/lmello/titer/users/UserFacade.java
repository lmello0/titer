package com.lmello.titer.users;

import com.lmello.titer.users.api.UserResponse;

import java.util.UUID;

public interface UserFacade {
    UserResponse getUser(UUID id);
}
