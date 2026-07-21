package com.sporekart.ai.providers.registry.cache;

import java.time.Duration;

public record CacheConfig(
    boolean enabled,
    Duration metadataTtl,
    Duration capabilityTtl,
    Duration healthTtl,
    Duration discoveryTtl,
    Duration selectionTtl,
    Duration versionTtl,
    int maxEntries,
    boolean automaticRefresh,
    Duration refreshInterval
) {
    public static CacheConfig defaults() {
        return new CacheConfig(
            true,
            Duration.ofMinutes(5),
            Duration.ofMinutes(5),
            Duration.ofSeconds(30),
            Duration.ofMinutes(10),
            Duration.ofMinutes(1),
            Duration.ofMinutes(30),
            1000,
            true,
            Duration.ofMinutes(1)
        );
    }
}
