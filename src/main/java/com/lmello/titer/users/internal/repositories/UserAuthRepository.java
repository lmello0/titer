package com.lmello.titer.users.internal.repositories;

import com.lmello.titer.users.internal.entities.UserAuthEntity;
import com.lmello.titer.users.internal.entities.UserAuthId;
import com.lmello.titer.users.internal.enums.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserAuthRepository extends JpaRepository<UserAuthEntity, UserAuthId> {
    Optional<UserAuthEntity> findByUserUsernameAndIdProvider(String username, AuthProvider provider);

    Optional<UserAuthEntity> findByUserEmailAndIdProvider(String email, AuthProvider provider);

    @Query("""
            SELECT a
            FROM UserAuthEntity a
            JOIN FETCH a.user u
            LEFT JOIN FETCH u.roles r
            WHERE u.username = :username
            AND a.id.provider = :provider
            """)
    Optional<UserAuthEntity> findByUsernameAndProviderWithAuthorities(
            @Param("username") String username,
            @Param("provider") AuthProvider provider
    );

    @Query("""
            SELECT a
            FROM UserAuthEntity a
            JOIN FETCH a.user u
            LEFT JOIN FETCH u.roles r
            WHERE u.email = :email
            AND a.id.provider = :provider
            """)
    Optional<UserAuthEntity> findByEmailAndProviderWithAuthorities(
            @Param("email") String email,
            @Param("provider") AuthProvider provider
    );
}
