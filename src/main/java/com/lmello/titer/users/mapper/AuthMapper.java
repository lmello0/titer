package com.lmello.titer.users.mapper;

import com.lmello.titer.users.entities.RoleEntity;
import com.lmello.titer.users.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuthMapper {

    default Set<String> toAuthorities(UserEntity entity) {
        if (entity == null || entity.getRoles() == null) {
            return Collections.emptySet();
        }

        return entity.getRoles()
                .stream()
                .map(RoleEntity::getName)
                .collect(Collectors.toSet());
    }
}
