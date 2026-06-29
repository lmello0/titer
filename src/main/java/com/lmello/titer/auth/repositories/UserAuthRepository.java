package com.lmello.titer.auth.repositories;

import com.lmello.titer.auth.entities.UserAuth;
import com.lmello.titer.auth.entities.UserAuthId;
import com.lmello.titer.auth.enums.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;
import java.util.UUID;

public interface UserAuthRepository extends JpaRepository<UserAuth, UserAuthId> {

    Optional<UserAuth> findByUserIdAndProvider(UUID userId, AuthProvider provider);

    Optional<UserAuth> findByProviderAndProviderId(AuthProvider provider, String providerId);

    boolean existsByUserIdAndProvider(UUID userId, AuthProvider provider);

    @Modifying
    void deleteByUserId(UUID userId);
}
