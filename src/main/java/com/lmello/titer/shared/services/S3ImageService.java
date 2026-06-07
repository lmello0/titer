package com.lmello.titer.shared.services;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class S3ImageService {

    private static final long MAX_FILE_SIZE_MB = 5 * 1024 * 1024;

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(

    );
}
