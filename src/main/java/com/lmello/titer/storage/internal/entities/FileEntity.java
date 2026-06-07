package com.lmello.titer.storage.internal.entities;

import com.lmello.titer.storage.internal.enums.StorageProvider;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "files", schema = "public")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Builder
public class FileEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    private String originalName;

    @Column(nullable = false)
    private String storedName;

    @Column(nullable = false, length = 100)
    private String contentType;

    @Column(nullable = false)
    private long sizeBytes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StorageProvider storageProvider;

    private String bucket;

    @Column(length = 1024)
    private String storageKey;

    @Column(length = 2048)
    private String url;

    @Column(length = 2048)
    private String path;

    @JdbcTypeCode(Types.BINARY)
    @Basic(fetch = FetchType.LAZY)
    private byte[] data;

    @Column(nullable = false, length = 50)
    private String createdBy;

    @Column(nullable = false)
    private Instant createdAt;
}
