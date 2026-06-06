package com.lmello.titer.users.internal.repositories;

import com.lmello.titer.users.internal.entities.UserRoleAuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRoleAuditRepository extends JpaRepository<UserRoleAuditEntity, UUID> {
}
