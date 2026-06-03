package com.lmello.titer.user.internal;

import com.lmello.titer.user.UserDTO;
import com.lmello.titer.user.internal.web.dto.CreateUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
interface UserMapper {

    UserDTO toDTO(User user);

    User toEntity(UserDTO entity);

    User toEntity(CreateUserDTO entity);
}
