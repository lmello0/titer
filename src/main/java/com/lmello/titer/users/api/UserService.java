package com.lmello.titer.users.api;

import com.lmello.titer.users.api.command.CreateUserCommand;
import com.lmello.titer.users.api.representation.UserInfo;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    Optional<UserInfo> findById(UUID userId);

    Page<UserInfo> getAll(Pageable pageable);

    UserInfo patch(UUID id, UserPatch patch);

    void softDelete(UUID id, String deletedBy);

    UserInfo create(CreateUserCommand command);

    Optional<UserInfo> findByEmail(String email);

    Optional<UserInfo> findByUsername(String username);

    Optional<UserInfo> findByUsernameOrEmail(String identifier);

    void markEmailVerified(UUID id);

    record UserPatch(
            @Nullable String username,
            @Nullable String firstName,
            @Nullable String lastName
    ) {
    }
}
