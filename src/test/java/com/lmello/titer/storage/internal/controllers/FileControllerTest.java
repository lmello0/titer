package com.lmello.titer.storage.internal.controllers;

import com.lmello.titer.storage.api.FileService;
import com.lmello.titer.storage.dto.download.FileDownload;
import com.lmello.titer.storage.dto.file.StoredFile;
import com.lmello.titer.storage.internal.exception.FileNotFoundException;
import jakarta.servlet.Filter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.ByteArrayInputStream;
import java.time.Instant;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig(FileControllerTest.TestConfig.class)
@WebAppConfiguration
class FileControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private FileService fileService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        reset(fileService);
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(authFilter())
                .build();
    }

    @Test
    void requiresAuthentication() throws Exception {
        mockMvc.perform(get("/files/{fileId}", UUID.randomUUID()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void returnsMetadataForAuthenticatedRequest() throws Exception {
        UUID fileId = UUID.randomUUID();
        Instant createdAt = Instant.parse("2026-06-07T12:00:00Z");
        when(fileService.metadata(fileId)).thenReturn(new StoredFile(
                fileId,
                "/files/" + fileId,
                "avatar.png",
                "image/png",
                4,
                createdAt
        ));

        mockMvc.perform(get("/files/{fileId}", fileId).header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fileId").value(fileId.toString()))
                .andExpect(jsonPath("$.url").value("/files/" + fileId))
                .andExpect(jsonPath("$.originalName").value("avatar.png"))
                .andExpect(jsonPath("$.contentType").value("image/png"))
                .andExpect(jsonPath("$.sizeBytes").value(4))
                .andExpect(jsonPath("$.createdAt").value(createdAt.toString()));
    }

    @Test
    void returnsInlineFileBytesForAuthenticatedDownloadRequest() throws Exception {
        UUID fileId = UUID.randomUUID();
        StoredFile metadata = new StoredFile(
                fileId,
                "/files/" + fileId,
                "avatar.png",
                "image/png",
                4,
                Instant.parse("2026-06-07T12:00:00Z")
        );
        when(fileService.download(fileId)).thenReturn(new FileDownload(
                metadata,
                new ByteArrayInputStream(new byte[]{1, 2, 3, 4})
        ));

        mockMvc.perform(get("/files/{fileId}/download", fileId).header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "image/png"))
                .andExpect(header().string("Content-Length", "4"))
                .andExpect(header().string("Content-Disposition", "inline; filename=\"avatar.png\""))
                .andExpect(content().bytes(new byte[]{1, 2, 3, 4}));
    }

    @Test
    void returnsNotFoundForMissingFile() throws Exception {
        UUID fileId = UUID.randomUUID();
        when(fileService.metadata(fileId)).thenThrow(new FileNotFoundException());

        mockMvc.perform(get("/files/{fileId}", fileId).header("Authorization", "Bearer token"))
                .andExpect(status().isNotFound());
    }

    @Test
    void returnsNotFoundForMissingDownload() throws Exception {
        UUID fileId = UUID.randomUUID();
        when(fileService.download(fileId)).thenThrow(new FileNotFoundException());

        mockMvc.perform(get("/files/{fileId}/download", fileId).header("Authorization", "Bearer token"))
                .andExpect(status().isNotFound());
    }

    private Filter authFilter() {
        return (request, response, chain) -> {
            if (((jakarta.servlet.http.HttpServletRequest) request).getHeader("Authorization") == null) {
                ((jakarta.servlet.http.HttpServletResponse) response).sendError(401);
                return;
            }

            chain.doFilter(request, response);
        };
    }

    @Configuration
    @EnableWebMvc
    static class TestConfig {

        @Bean
        FileController fileController(FileService fileService) {
            return new FileController(fileService);
        }

        @Bean
        FileExceptionHandler fileExceptionHandler() {
            return new FileExceptionHandler();
        }

        @Bean
        FileService fileService() {
            return mock(FileService.class);
        }
    }
}
