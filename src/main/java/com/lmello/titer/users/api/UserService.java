package com.lmello.titer.users.api;

import com.lmello.titer.users.api.command.CreateUserCommand;
import com.lmello.titer.users.api.representation.UserInfo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    // reads

    UserInfo getById(UUID id);

    Optional<UserInfo> findById(UUID id);

    Optional<UserInfo> findByEmail(String email);

    Optional<UserInfo> findByUsername(String username);

    Optional<UserInfo> findByUsernameOrEmail(String identifier);

    List<UserInfo> getAll();

    // lifecycle

    UserInfo create(CreateUserCommand command);

    UserInfo patch(UUID id, UserPatch patch, String modifiedBy);

    void softDelete(UUID id, String deletedBy);

    void deactivate(UUID id, String performedBy);

    void reactivate(UUID id, String performedBy);

    // email verification

    void markEmailVerified(UUID id);

    void resendEmailVerification(UUID id);

    UserInfo confirmEmailVerification(String token);

    // roles

    UserInfo grantRole(UUID userId, String roleName, String performedBy, String reason);

    UserInfo revokeRole(UUID userId, String roleName, String performedBy, String reason);

    // profile picture

    UserInfo setProfilePicture(UUID userId, UUID fileId, String modifiedBy);

    UserInfo removeProfilePicture(UUID userId, String modifiedBy);

    record UserPatch(String username, String firstName, String lastName) {
    }
}
