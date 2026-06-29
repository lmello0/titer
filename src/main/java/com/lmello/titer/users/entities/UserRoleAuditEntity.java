package com.lmello.titer.users.entities;

import com.lmello.titer.users.enums.RoleAuditAction;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_role_audits", schema = "users")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Builder
public class UserRoleAuditEntity {

    @Id
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, updatable = false)
    private UUID userId;

    @Column(nullable = false, updatable = false)
    private Long roleId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20, updatable = false)
    private RoleAuditAction action;

    @Column(nullable = false, length = 50, updatable = false)
    private String performedBy;

    @Column(updatable = false)
    private String reason;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private UserRoleAuditEntity(UUID userId, Long roleId, RoleAuditAction action, String performedBy, String reason) {
        this.userId = userId;
        this.roleId = roleId;
        this.action = action;
        this.performedBy = performedBy;
        this.reason = reason;
        this.createdAt = Instant.now();
    }

    public static UserRoleAuditEntity granted(UUID userId, Long roleId, String performedBy, String reason) {
        return new UserRoleAuditEntity(userId, roleId, RoleAuditAction.GRANTED, performedBy, reason);
    }

    public static UserRoleAuditEntity revoked(UUID userId, Long roleId, String performedBy, String reason) {
        return new UserRoleAuditEntity(userId, roleId, RoleAuditAction.REVOKED, performedBy, reason);
    }
}
