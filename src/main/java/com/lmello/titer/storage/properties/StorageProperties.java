package com.lmello.titer.storage.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "app.storage")
public record StorageProperties(
        @DefaultValue("http://localhost:8080/files") String baseUrl,
        @DefaultValue ProviderProperties provider,
        @DefaultValue LocalProperties local,
        @DefaultValue S3Properties s3
) {
    public record ProviderProperties(@DefaultValue("local") String defaultProvider) {
    }

    public record LocalProperties(
            @DefaultValue("true") boolean enabled,
            @DefaultValue("./uploads") String rootPath
    ) {
    }

    public record S3Properties(
            boolean enabled,
            String bucket,
            String region
    ) {
    }

    public String defaultProviderName() {
        return provider.defaultProvider();
    }
}
