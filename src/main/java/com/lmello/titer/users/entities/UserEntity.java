package com.lmello.titer.users.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users", schema = "users")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, unique = true, length = 30)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 100)
    private String firstName;

    @Column(length = 100)
    private String lastName;

    @Column(nullable = false)
    private boolean isEmailVerified;

    @Column(name = "profile_picture_file_id", length = 2048)
    private UUID profilePictureFileId;

    @Column(nullable = false, length = 50, updatable = false)
    private String createdBy;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(length = 50)
    private String modifiedBy;

    private Instant modifiedAt;

    @Column(length = 50)
    private String deletedBy;

    private Instant deletedAt;

    @Column(length = 50)
    private String deactivatedBy;

    private Instant deactivatedAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles", schema = "users",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles = new HashSet<>();

    private UserEntity(String username, String email, String firstName, String lastName, boolean isEmailVerified, String createdBy) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isEmailVerified = isEmailVerified;
        this.createdBy = createdBy;
        this.createdAt = Instant.now();
    }

    public static UserEntity create(String username, String email, String firstName, String lastName, boolean isEmailVerified, String createdBy) {
        return new UserEntity(username, email, firstName, lastName, isEmailVerified, createdBy);
    }

    private void touch(String by) {
        this.modifiedBy = by;
        this.modifiedAt = Instant.now();
    }

    public void rename(String username, String by) {
        this.username = username;
        touch(by);
    }

    public void updateName(String firstName, String lastName, String by) {
        this.firstName = firstName;
        this.lastName = lastName;
        touch(by);
    }

    public void markEmailVerified() {
        this.isEmailVerified = true;
    }

    public void changeProfilePicture(UUID fileId, String by) {
        this.profilePictureFileId = fileId;
        touch(by);
    }

    public void softDelete(String by) {
        this.deletedAt = Instant.now();
        this.deletedBy = by;
    }

    public void deactivate(String by) {
        this.deactivatedAt = Instant.now();
        this.deactivatedBy = by;
    }

    public void reactivate(String by) {
        this.deactivatedAt = null;
        this.deactivatedBy = null;
        touch(by);
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public boolean isActive() {
        return deletedAt == null && deactivatedAt == null;
    }

    public void grant(RoleEntity role) {
        this.roles.add(role);
    }

    public void revoke(RoleEntity role) {
        this.roles.removeIf(r -> r.getId().equals(role.getId()));
    }

    public boolean hasRole(RoleEntity role) {
        return this.roles.stream()
                .anyMatch(r -> r.getId().equals(role.getId()));
    }
}
