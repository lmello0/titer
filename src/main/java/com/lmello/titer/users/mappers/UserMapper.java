package com.lmello.titer.users.mappers;

import com.lmello.titer.users.api.representation.UserInfo;
import com.lmello.titer.users.entities.RoleEntity;
import com.lmello.titer.users.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserInfo toUserInfo(UserEntity entity);

    default String map(RoleEntity role) {
        return role.getName();
    }
}
