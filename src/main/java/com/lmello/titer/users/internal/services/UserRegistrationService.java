package com.lmello.titer.users.internal.services;

import com.lmello.titer.users.api.RegisterRequest;
import com.lmello.titer.users.internal.entities.RoleEntity;
import com.lmello.titer.users.internal.entities.UserEntity;
import com.lmello.titer.users.internal.entities.UserRoleAuditEntity;
import com.lmello.titer.users.internal.enums.Role;
import com.lmello.titer.users.internal.enums.UserRoleAuditAction;
import com.lmello.titer.users.internal.exception.DuplicateUserException;
import com.lmello.titer.users.internal.exception.RoleNotFoundException;
import com.lmello.titer.users.internal.repositories.RoleRepository;
import com.lmello.titer.users.internal.repositories.UserRepository;
import com.lmello.titer.users.internal.repositories.UserRoleAuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleAuditRepository userRoleAuditRepository;

    public UserEntity createLocalUser(RegisterRequest request) {
        if (userRepository.existsByUsernameOrEmail(request.username(), request.email())) {
            throw new DuplicateUserException();
        }

        RoleEntity defaultRole = roleRepository.findByName(Role.USER.name())
                .orElseThrow(() -> new RoleNotFoundException(Role.USER));

        UserEntity newUser = UserEntity.builder()
                .username(request.username())
                .email(request.email())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .profilePicture(request.profilePicture())
                .build();

        newUser.addRole(defaultRole);
        newUser = userRepository.saveAndFlush(newUser);

        userRepository.setCreatedByAndModifiedByToSelf(newUser.getId());
        auditDefaultRoleGrant(newUser, defaultRole);

        return newUser;
    }

    private void auditDefaultRoleGrant(UserEntity user, RoleEntity role) {
        UserRoleAuditEntity roleAudit = UserRoleAuditEntity.builder()
                .user(user)
                .role(role)
                .action(UserRoleAuditAction.GRANTED)
                .performedBy(user.getId().toString())
                .reason("Default role granted during user registration")
                .build();

        userRoleAuditRepository.save(roleAudit);
    }
}
