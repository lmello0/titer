package com.lmello.titer.shared.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "aws")
public record AwsProperties(
        String region,
        String endpointUrl,
        @DefaultValue("false") boolean pathStyleAccessEnabled
) {
}
