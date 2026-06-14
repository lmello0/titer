package com.lmello.titer.storage.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "app.storage.image")
public record ImageProperties(
        @DefaultValue("0.85") double outputQuality,
        @DefaultValue("2048") int maxWidth,
        @DefaultValue("2048") int maxHeight
) {
}
