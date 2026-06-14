package com.lmello.titer.users.entities;

import com.lmello.titer.users.enums.AuthProvider;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class UserAuthId implements Serializable {

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    AuthProvider provider;
    private UUID userId;
}
