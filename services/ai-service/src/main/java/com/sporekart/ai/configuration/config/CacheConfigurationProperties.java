package com.sporekart.ai.configuration.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Map;

@ConfigurationProperties(prefix = "sporekart.ai.cache")
public record CacheConfigurationProperties(
    boolean enabled,
    Duration defaultTtl,
    String keyPrefix,
    boolean versioningEnabled,
    String refreshStrategy,
    Map<String, Duration> ttlOverrides
) {
    public CacheConfigurationProperties() {
        this(true, Duration.ofMinutes(30), "ai:", true, "CACHE_ASIDE", Map.of());
    }
}
