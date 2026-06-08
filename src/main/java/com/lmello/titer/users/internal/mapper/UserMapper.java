package com.lmello.titer.users.internal.mapper;

import com.lmello.titer.storage.api.FileService;
import com.lmello.titer.users.api.UserResponse;
import com.lmello.titer.users.internal.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final FileService fileService;

    public UserResponse toResponse(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        return new UserResponse(
                entity.getId(),
                entity.getEmail(),
                entity.getUsername(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getProfilePicture() == null ? null : fileService.publicUrl(entity.getProfilePicture())
        );
    }
}
