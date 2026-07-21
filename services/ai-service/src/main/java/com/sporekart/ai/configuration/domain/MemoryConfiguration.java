package com.sporekart.ai.configuration.domain;

import java.time.Duration;

public record MemoryConfiguration(
    boolean enabled,
    boolean cachingEnabled,
    Duration cacheTtl,
    boolean vectorIndexingEnabled,
    boolean consolidationEnabled,
    Duration shortTermTtl,
    Duration longTermTtl,
    int maxShortTermEntries,
    int maxLongTermEntries,
    boolean importanceScoringEnabled,
    double importanceThreshold,
    boolean pruningEnabled,
    Duration pruningInterval,
    boolean auditEnabled
) {
    public static MemoryConfiguration defaults() {
        return new MemoryConfiguration(
            true, true, Duration.ofMinutes(30),
            true, true,
            Duration.ofHours(24), Duration.ofDays(90),
            100, 10000,
            true, 0.3,
            true, Duration.ofHours(6),
            true
        );
    }
}
