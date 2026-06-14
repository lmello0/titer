package com.lmello.titer.storage.entities;

import com.lmello.titer.storage.api.command.StorageTarget;
import com.lmello.titer.storage.api.representation.FileRepresentation;
import com.lmello.titer.storage.api.representation.FileStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
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

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false, length = 100)
    private String contentType;

    @Column(nullable = false)
    private long sizeBytes;

    @Column(nullable = false)
    private long originalSizeBytes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileStatus status;

    @Column(length = 2048)
    private String publicUrl;

    @Column(nullable = false)
    private String storageKey;

    @Column(nullable = false, length = 30)
    private String provider;

    private Integer mediaWidth;
    private Integer mediaHeight;
    private Long mediaDurationMs;
    private String mediaCodec;
    private String mediaColorSpace;
    private Boolean mediaHasAlpha;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, String> customMetadata = new HashMap<>();

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    private Instant processedAt;

    private String failureReason;

    public static FileEntity pending(
            UUID id,
            String publicUrl,
            String filename,
            String contentType,
            long originalSizeBytes,
            String provider,
            Map<String, String> customMetadata
    ) {
        return FileEntity.builder()
                .id(id)
                .publicUrl(publicUrl)
                .filename(filename)
                .contentType(contentType)
                .originalSizeBytes(originalSizeBytes)
                .sizeBytes(originalSizeBytes)
                .status(FileStatus.PENDING)
                .storageKey("")
                .provider(provider)
                .customMetadata(new HashMap<>(customMetadata))
                .createdAt(Instant.now())
                .build();
    }

    public void markProcessing() {
        this.status = FileStatus.PROCESSING;
    }

    public void markReady(
            String storageKey,
            String publicUrl,
            long sizeBytes,
            String contentType,
            FileRepresentation.MediaMetadata media
    ) {
        this.status = FileStatus.READY;
        this.storageKey = storageKey;
        this.publicUrl = publicUrl;
        this.sizeBytes = sizeBytes;
        this.contentType = contentType;
        this.processedAt = Instant.now();
        applyMediaMetadata(media);
    }

    public void markFailed(String reason) {
        this.status = FileStatus.FAILED;
        this.failureReason = reason;
        this.processedAt = Instant.now();
    }

    private void applyMediaMetadata(FileRepresentation.MediaMetadata m) {
        if (m == null) return;

        this.mediaWidth = m.width();
        this.mediaHeight = m.height();
        this.mediaDurationMs = m.durationMs();
        this.mediaCodec = m.codec();
        this.mediaColorSpace = m.colorSpace();
        this.mediaHasAlpha = m.hasAlpha();
    }
}
