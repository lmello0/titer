package com.lmello.titer.users.internal.entities;

import com.lmello.titer.users.internal.enums.AuthProvider;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_auths")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Builder
public class UserAuthEntity {

    @EmbeddedId
    private UserAuthId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String providerId;

    private String passwordHash;

    public static UserAuthEntity local(
            UserEntity user,
            String passwordHash
    ) {
        return UserAuthEntity.builder()
                .id(new UserAuthId(
                        user.getId(),
                        AuthProvider.LOCAL
                ))
                .user(user)
                .passwordHash(passwordHash)
                .build();
    }
}
