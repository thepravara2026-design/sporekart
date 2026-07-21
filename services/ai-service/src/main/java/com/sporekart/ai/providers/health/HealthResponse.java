package com.sporekart.ai.providers.health;

import java.time.Instant;

public record HealthResponse(
    String providerId,
    HealthStatus status,
    boolean available,
    long latency,
    Instant timestamp
) {
    public static HealthResponse healthy(String providerId) {
        return new HealthResponse(providerId, HealthStatus.HEALTHY, true, 0, Instant.now());
    }

    public static HealthResponse unhealthy(String providerId) {
        return new HealthResponse(providerId, HealthStatus.UNHEALTHY, false, 0, Instant.now());
    }
}
