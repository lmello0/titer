package com.lmello.titer.users.internal.repositories;

import com.lmello.titer.users.internal.entities.RoleEntity;
import com.lmello.titer.users.internal.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    @Query("""
                SELECT r
                FROM RoleEntity r
                WHERE r.name = :#{role.name()}
            """)
    Optional<RoleEntity> findByRole(@Param("role") Role role);
}
