package com.lmello.titer.storage.mappers;

import com.lmello.titer.storage.api.representation.FileRepresentation;
import com.lmello.titer.storage.entities.FileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FileMapper {

    FileRepresentation toRepresentation(FileEntity entity);
}
