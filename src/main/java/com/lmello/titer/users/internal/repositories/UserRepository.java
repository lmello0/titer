package com.lmello.titer.users.internal.repositories;

import com.lmello.titer.users.internal.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    boolean existsByUsernameOrEmail(String username, String email);

    @Modifying
    @Query(value = """
            update users.users
            set created_by = id::text,
                modified_by = id::text
            where id = :id
            """, nativeQuery = true)
    void setCreatedByAndModifiedByToSelf(UUID id);
}
