package com.lmello.titer.users.repositories;

import com.lmello.titer.users.api.representation.UserInfo;
import com.lmello.titer.users.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUsername(String username);

    @Query("""
            select u
            from UserEntity u
            where u.username = :identifier
            or u.email = :identifier
            """)
    Optional<UserEntity> findByUsernameOrEmail(String identifier);
}
