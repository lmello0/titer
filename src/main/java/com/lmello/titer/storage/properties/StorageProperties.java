package com.lmello.titer.storage.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "app.storage")
public record StorageProperties(
        @DefaultValue ProviderProperties provider,
        @DefaultValue LocalProperties local,
        @DefaultValue S3Properties s3
) {
    public record ProviderProperties(@DefaultValue("local") String defaultProvider) {
    }

    public record LocalProperties(
            @DefaultValue("true") boolean enabled,
            @DefaultValue("./uploads") String rootPath,
            @DefaultValue("http://localhost:8080/files") String baseUrl
    ) {
    }

    public record S3Properties(
            boolean enabled,
            String bucket,
            String baseUrl,
            String region
    ) {
    }

    public String defaultProviderName() {
        return provider.defaultProvider();
    }
}
