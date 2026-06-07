package com.lmello.titer.storage.internal.mapper;

import com.lmello.titer.storage.api.StoredFile;
import com.lmello.titer.storage.internal.entities.FileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FileMapper {
    StoredFile toDTO(FileEntity entity);
}
