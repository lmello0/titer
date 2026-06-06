package com.lmello.titer.users.internal.repositories;

import com.lmello.titer.users.internal.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    @Query("""
                SELECT
                    CASE
                        WHEN COUNT(u) > 0 THEN TRUE
                        ELSE FALSE
                    END
                FROM UserEntity u
                WHERE (:email IS NULL or u.email = :email)
                   OR (:username IS NULL or u.username = :username)
            """)
    boolean existsByEmailOrUsername(String email, String username);
}
