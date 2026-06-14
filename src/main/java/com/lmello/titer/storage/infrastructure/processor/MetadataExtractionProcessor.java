package com.lmello.titer.storage.infrastructure.processor;

import com.lmello.titer.storage.api.FileProcessor;
import com.lmello.titer.storage.api.command.StoreFileCommand;
import com.lmello.titer.storage.api.representation.FileRepresentation;
import com.lmello.titer.storage.exceptions.ProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Slf4j
@Order(10)
@Component
public class MetadataExtractionProcessor implements FileProcessor {


    @Override
    public boolean supports(StoreFileCommand command) {
        return command.isImage() || command.isVideo();
    }

    @Override
    public ProcessingContext process(ProcessingContext ctx) throws ProcessingException {
        if (ctx.command().isImage()) {
            return extractImageMetadata(ctx);
        }

        if (ctx.command().isVideo()) {
            return extractVideoMetadata(ctx);
        }

        return ctx;
    }

    private ProcessingContext extractImageMetadata(ProcessingContext ctx) throws ProcessingException {
        try {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(ctx.data()));

            if (img == null) {
                log.warn("ImageIO could not decode '{}', skipping metadata extraction", ctx.command().filename());
                return ctx;
            }

            boolean hasAlpha = img.getColorModel().hasAlpha();
            String colorSpace = img.getColorModel().getColorSpace().getType() == ColorSpace.TYPE_GRAY ? "GRAY" : "RGB";

            FileRepresentation.MediaMetadata meta = new FileRepresentation.MediaMetadata(
                    img.getWidth(),
                    img.getHeight(),
                    null,
                    null,
                    colorSpace,
                    hasAlpha
            );

            log.debug("Extracted image metadata: {}x{} hasAlpha={} colorSpace={}",
                    img.getWidth(), img.getHeight(), hasAlpha, colorSpace
            );

            return ctx.withMediaMetadata(meta);
        } catch (IOException e) {
            throw new ProcessingException("Failed to extract image metadata", e);
        }
    }

    private ProcessingContext extractVideoMetadata(ProcessingContext ctx) throws ProcessingException {
        log.debug("Video metadata extraction not yet implemented for '{}'", ctx.command().filename());
        return ctx;
    }
}
