package com.lmello.titer.storage.dto.download;

import com.lmello.titer.storage.dto.file.StoredFile;

import java.io.InputStream;

public record FileDownload(
        StoredFile metadata,
        InputStream content
) {
}
