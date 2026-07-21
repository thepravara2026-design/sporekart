package com.sporekart.ai.configuration.domain;

import java.time.Duration;

public record AIConfiguration(
    boolean enabled,
    String defaultProvider,
    Duration globalTimeout,
    int maxRetries,
    int maxConcurrentRequests,
    boolean streamingEnabled,
    boolean cachingEnabled,
    boolean auditEnabled,
    boolean metricsEnabled
) {
    public static AIConfiguration defaults() {
        return new AIConfiguration(
            true,
            "openai",
            Duration.ofSeconds(30),
            3,
            100,
            true,
            true,
            true,
            true
        );
    }
}
