package com.lmello.titer.storage.internal.repositories;

import com.lmello.titer.storage.internal.entities.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileRepository extends JpaRepository<FileEntity, UUID> {
}
