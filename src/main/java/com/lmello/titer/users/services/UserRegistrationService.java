package com.lmello.titer.users.services;

import com.lmello.titer.storage.api.StorageService;
import com.lmello.titer.storage.api.command.StoreFileCommand;
import com.lmello.titer.storage.api.representation.FileRepresentation;
import com.lmello.titer.users.dto.RegisterRequest;
import com.lmello.titer.users.entities.RoleEntity;
import com.lmello.titer.users.entities.UserEntity;
import com.lmello.titer.users.entities.UserRoleAuditEntity;
import com.lmello.titer.users.enums.Role;
import com.lmello.titer.users.enums.UserRoleAuditAction;
import com.lmello.titer.users.exception.DuplicateUserException;
import com.lmello.titer.users.exception.RoleNotFoundException;
import com.lmello.titer.users.repositories.RoleRepository;
import com.lmello.titer.users.repositories.UserRepository;
import com.lmello.titer.users.repositories.UserRoleAuditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleAuditRepository userRoleAuditRepository;

    private final StorageService storageService;

    @Transactional
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
                .build();

        newUser.addRole(defaultRole);
        newUser = userRepository.saveAndFlush(newUser);

        String userId = newUser.getId().toString();

        newUser.setCreatedBy(userId);
        newUser.setModifiedBy(userId);

        auditDefaultRoleGrant(newUser, defaultRole);

        FileRepresentation profilePicture = storeProfilePictureIfPresent(
                request.profilePicture()
        );

        if (profilePicture != null) {
            newUser.setProfilePicture(profilePicture.id());
        }

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

    private FileRepresentation storeProfilePictureIfPresent(MultipartFile requestFile) {
        if (requestFile == null || requestFile.isEmpty()) {
            return null;
        }

        try {
            StoreFileCommand storeFileCommand = StoreFileCommand.builder()
                    .filename(requestFile.getOriginalFilename())
                    .contentType(requestFile.getContentType())
                    .contentLength(requestFile.getSize())
                    .content(requestFile.getInputStream())
                    .build();

            return storageService.store(storeFileCommand);
        } catch (IOException exception) {
            throw new IllegalStateException("Could not read profile picture", exception);
        }
    }
}
