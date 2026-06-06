package com.lmello.titer.users.internal.repositories;

import com.lmello.titer.users.internal.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    boolean existsByUsernameOrEmail(String username, String email);
}
