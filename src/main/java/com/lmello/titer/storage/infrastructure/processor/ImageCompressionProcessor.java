package com.lmello.titer.storage.infrastructure.processor;

import com.lmello.titer.storage.api.FileProcessor;
import com.lmello.titer.storage.api.command.StoreFileCommand;
import com.lmello.titer.storage.exceptions.ProcessingException;
import com.lmello.titer.storage.properties.ImageProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@Order(20)
@Component
@RequiredArgsConstructor
public class ImageCompressionProcessor implements FileProcessor {

    private final ImageProperties imageProperties;

    @Override
    public boolean supports(StoreFileCommand command) {
        return command.isImage();
    }

    @Override
    public ProcessingContext process(ProcessingContext ctx) throws ProcessingException {
        try {
            log.debug("Compressing image '{}' ({} bytes)", ctx.command().filename(), ctx.data().length);

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            int targetWidth = Math.min(ctx.mediaMetadata().width(), imageProperties.maxWidth());
            int targetHeight = Math.min(ctx.mediaMetadata().height(), imageProperties.maxHeight());

            Thumbnails.of(new ByteArrayInputStream(ctx.data()))
                    .size(targetWidth, targetHeight)
                    .keepAspectRatio(true)
                    .outputQuality(imageProperties.outputQuality())
                    .allowOverwrite(true)
                    .toOutputStream(out);

            byte[] compressed = out.toByteArray();
            log.debug("Compressed image: {} -> {} bytes ({}% reduction)",
                    ctx.data().length, compressed.length,
                    String.format("%.1f", (1.0 - (double) compressed.length / ctx.data().length) * 100)
            );

            return ctx.withData(compressed);
        } catch (IOException e) {
            throw new ProcessingException("Image compression failed for: " + ctx.command().filename(), e);
        }
    }
}
