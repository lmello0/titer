package com.lmello.titer.users.mapper;

import com.lmello.titer.storage.api.StorageService;
import com.lmello.titer.users.dto.UserResponse;
import com.lmello.titer.users.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final StorageService storageService;

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
                entity.getProfilePicture() == null ? null : storageService.publicUrl(entity.getProfilePicture()).toString()
        );
    }
}
