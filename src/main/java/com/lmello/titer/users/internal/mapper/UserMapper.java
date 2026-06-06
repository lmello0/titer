package com.lmello.titer.users.internal.mapper;

import com.lmello.titer.users.api.UserResponse;
import com.lmello.titer.users.internal.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserResponse toResponse(UserEntity entity);
}
