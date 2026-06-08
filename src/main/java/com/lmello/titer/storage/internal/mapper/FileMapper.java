package com.lmello.titer.storage.internal.mapper;

import com.lmello.titer.storage.dto.file.StoredFile;
import com.lmello.titer.storage.internal.entities.FileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FileMapper {
    default StoredFile toDTO(FileEntity entity, String url) {
        if (entity == null) {
            return null;
        }

        return new StoredFile(
                entity.getId(),
                url,
                entity.getOriginalName(),
                entity.getContentType(),
                entity.getSizeBytes(),
                entity.getCreatedAt()
        );
    }
}
