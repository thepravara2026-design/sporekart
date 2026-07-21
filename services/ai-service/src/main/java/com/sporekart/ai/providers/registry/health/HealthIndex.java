package com.sporekart.ai.providers.registry.health;

import java.time.Instant;

public record HealthIndex(
    String providerId,
    boolean available,
    long latency,
    int failureCount,
    Instant lastSuccess,
    Instant lastFailure,
    boolean circuitOpen,
    boolean maintenance,
    double healthScore
) {}
