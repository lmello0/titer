package com.lmello.titer.users.repositories;

import com.lmello.titer.users.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    @Query("select u from UserEntity u where u.id = :id and u.deletedAt is null")
    Optional<UserEntity> findActiveById(@Param("id") UUID id);

    @Query("select u from UserEntity u where lower(u.email) = lower(:email) and u.deletedAt is null")
    Optional<UserEntity> fincActiveByEmail(@Param("email") String email);

    @Query("select u from UserEntity u where lower(u.username) = lower(:username) and u.deletedAt is null")
    Optional<UserEntity> findActiveByUsername(@Param("username") String username);

    @Query("""
            select u from UserEntity u
            where (lower(u.username) = lower(:identifier) or lower(u.email) = lower(:identifier))
            and u.deletedAt is null
            """)
    Optional<UserEntity> findActiveByUsernameOrEmail(@Param("identifier") String identifier);

    @Query("select u from UserEntity u where u.deletedAt is null order by u.createdAt")
    List<UserEntity> findAllActive();

    @Query("""
            select count(u) > 0
            from UserEntity u
            where lower(u.email) = lower(:email)
            and u.deletedAt is null
            """)
    boolean existsActiveByEmail(@Param("email") String email);

    @Query("""
            select count(u) > 0
            from UserEntity u
            where lower(u.username) = lower(:username)
            and u.deletedAt is null
            """)
    boolean existsActiveByUsername(@Param("username") String username);
}
