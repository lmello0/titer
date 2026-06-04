package com.lmello.titer.users.internal.mapper;

import com.lmello.titer.users.api.UserResponse;
import com.lmello.titer.users.internal.entities.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(UserEntity entity);
}
