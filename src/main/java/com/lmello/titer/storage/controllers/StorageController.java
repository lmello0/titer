package com.lmello.titer.storage.controllers;

import com.lmello.titer.storage.api.StorageService;
import com.lmello.titer.storage.api.command.StoreFileCommand;
import com.lmello.titer.storage.api.representation.FileRepresentation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileRepresentation> upload(@RequestParam("file") MultipartFile file) throws IOException {
        StoreFileCommand command = StoreFileCommand.builder()
                .filename(file.getOriginalFilename())
                .contentType(file.getContentType())
                .contentLength(file.getSize())
                .content(file.getInputStream())
                .build();

        return ResponseEntity
                .accepted()
                .body(storageService.store(command));
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<FileRepresentation> getMetadata(@PathVariable UUID fileId) {
        FileRepresentation file = storageService.findById(fileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found: " + fileId));

        return ResponseEntity
                .ok(file);
    }

    @GetMapping("/{fileId}/content")
    public ResponseEntity<Resource> download(@PathVariable UUID fileId) {
        FileRepresentation meta = storageService.findById(fileId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found: " + fileId));

        return switch (meta.status()) {
            case PENDING, PROCESSING -> ResponseEntity.status(HttpStatus.ACCEPTED)
                    .header("Retry-After", "2")
                    .build();
            case FAILED -> ResponseEntity.status(HttpStatus.GONE).build();
            case READY -> {
                Resource resource = storageService.load(fileId);
                yield ResponseEntity
                        .ok()
                        .contentType(MediaType.parseMediaType(meta.contentType()))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + meta.filename() + "\"")
                        .body(resource);
            }
        };
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> delete(@PathVariable UUID fileId) {
        storageService.delete(fileId);

        return ResponseEntity.noContent().build();
    }
}
