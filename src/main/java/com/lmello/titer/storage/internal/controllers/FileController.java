package com.lmello.titer.storage.internal.controllers;

import com.lmello.titer.storage.api.FileService;
import com.lmello.titer.storage.dto.download.FileDownload;
import com.lmello.titer.storage.dto.file.StoredFile;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/{fileId}")
    ResponseEntity<StoredFile> getFileMetadata(@PathVariable UUID fileId) {
        return ResponseEntity.ok(fileService.metadata(fileId));
    }

    @GetMapping("/{fileId}/download")
    ResponseEntity<InputStreamResource> getFile(@PathVariable UUID fileId) {
        FileDownload file = fileService.download(fileId);
        StoredFile metadata = file.metadata();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(metadata.contentType()))
                .contentLength(metadata.sizeBytes())
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.inline()
                        .filename(safeFilename(metadata.originalName()))
                        .build()
                        .toString())
                .body(new InputStreamResource(file.content()));
    }


    private String safeFilename(String filename) {
        if (filename == null || filename.isBlank()) {
            return "file";
        }

        return filename.replaceAll("[\\r\\n\\\\/]", "_");
    }
}
