package com.lmello.titer.storage.infrastructure.persistence;

import com.lmello.titer.storage.api.representation.FileStatus;
import com.lmello.titer.storage.entities.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileRepository extends JpaRepository<FileEntity, UUID> {

    Optional<FileEntity> findByIdAndStatus(UUID id, FileStatus status);

    List<FileEntity> findAllByStatus(FileStatus status);
}
