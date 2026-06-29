package com.lmello.titer.auth.entities;

import com.lmello.titer.auth.enums.AuthProvider;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_auths", schema = "auth")
@IdClass(UserAuthId.class)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Builder
public class UserAuth {

    @Id
    @Column(nullable = false)
    private UUID userId;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private AuthProvider provider;

    private @Nullable String providerId;

    private @Nullable String passwordHash;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    private @Nullable Instant modifiedAt;

    private UserAuth(
            UUID userId,
            AuthProvider provider,
            @Nullable String providerId,
            @Nullable String passwordHash
    ) {
        this.userId = userId;
        this.provider = provider;
        this.providerId = providerId;
        this.passwordHash = passwordHash;
    }

    public static UserAuth local(UUID userId, String passwordHash) {
        return new UserAuth(userId, AuthProvider.LOCAL, null, passwordHash);
    }

    public static UserAuth social(UUID userId, AuthProvider provider, String providerId) {
        return new UserAuth(userId, provider, providerId, null);
    }

    public void changePassword(String newPasswordHash) {
        this.passwordHash = newPasswordHash;
        this.modifiedAt = Instant.now();
    }
}
