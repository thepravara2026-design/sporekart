package com.sporekart.ai.providers;

import java.time.Instant;

public record ProviderHealth(
    String providerId,
    boolean available,
    String status,
    Instant lastHeartbeat,
    long failureCount,
    double availabilityPercentage,
    boolean circuitBreakerOpen,
    boolean maintenanceMode,
    String lastError,
    Instant lastSuccess
) {
    public static ProviderHealth healthy(String providerId) {
        return new ProviderHealth(providerId, true, "HEALTHY", Instant.now(),
            0, 100.0, false, false, null, Instant.now());
    }

    public static ProviderHealth unhealthy(String providerId, String error) {
        return new ProviderHealth(providerId, false, "UNHEALTHY", Instant.now(),
            1, 0.0, false, false, error, null);
    }
}
