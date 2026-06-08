package com.lmello.titer.users.internal.services;

import com.lmello.titer.storage.api.FileService;
import com.lmello.titer.storage.dto.file.StoredFile;
import com.lmello.titer.storage.dto.upload.FileRules;
import com.lmello.titer.storage.dto.upload.StoreFileRequest;
import com.lmello.titer.storage.dto.upload.UploadFile;
import com.lmello.titer.storage.dto.upload.UploadFileMetadata;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleAuditRepository userRoleAuditRepository;

    private final FileService fileService;

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

        StoredFile profilePicture = storeProfilePictureIfPresent(
                newUser,
                request.profilePicture()
        );

        if (profilePicture != null) {
            newUser.setProfilePicture(profilePicture.fileId());
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

    private StoredFile storeProfilePictureIfPresent(UserEntity user, MultipartFile requestFile) {
        if (requestFile == null || requestFile.isEmpty()) {
            return null;
        }

        try {
            UploadFileMetadata metadata = UploadFileMetadata.builder()
                    .originalName(requestFile.getOriginalFilename())
                    .namePrefix("pfp")
                    .contentType(requestFile.getContentType())
                    .sizeBytes(requestFile.getSize())
                    .build();

            UploadFile f = UploadFile.builder()
                    .metadata(metadata)
                    .content(requestFile.getInputStream())
                    .build();

            return fileService.store(
                    new StoreFileRequest(
                            f,
                            null,
                            user.getId().toString(),
                            FileRules.defaultImage()
                    )
            );
        } catch (IOException exception) {
            throw new IllegalStateException("Could not read profile picture", exception);
        }
    }
}
