package com.lmello.titer.users.internal.repositories;

import com.lmello.titer.users.internal.entities.UserAuthEntity;
import com.lmello.titer.users.internal.entities.UserAuthId;
import com.lmello.titer.users.internal.enums.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAuthRepository extends JpaRepository<UserAuthEntity, UserAuthId> {
    Optional<UserAuthEntity> findByUserUsernameAndIdProvider(String username, AuthProvider provider);

    Optional<UserAuthEntity> findByUserEmailAndIdProvider(String email, AuthProvider provider);
}
