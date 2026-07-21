package com.sporekart.ai.provider.models;

import java.time.Duration;
import java.time.Instant;

public record ProviderHealth(
        String providerId,
        String providerName,
        HealthStatus status,
        Duration latency,
        Instant lastChecked,
        Instant lastFailure,
        int consecutiveFailures,
        long uptimeSeconds,
        String details) {

    public enum HealthStatus {
        HEALTHY,
        DEGRADED,
        UNHEALTHY,
        OFFLINE,
        UNKNOWN
    }

    public static ProviderHealth healthy(String providerId, String providerName) {
        return new ProviderHealth(providerId, providerName, HealthStatus.HEALTHY,
                Duration.ZERO, Instant.now(), null, 0, 0, "Operational");
    }

    public static ProviderHealth degraded(String providerId, String providerName, Duration latency, String details) {
        return new ProviderHealth(providerId, providerName, HealthStatus.DEGRADED,
                latency, Instant.now(), null, 0, 0, details);
    }

    public static ProviderHealth unhealthy(String providerId, String providerName, String details) {
        return new ProviderHealth(providerId, providerName, HealthStatus.UNHEALTHY,
                Duration.ZERO, Instant.now(), Instant.now(), 1, 0, details);
    }

    public static ProviderHealth offline(String providerId, String providerName) {
        return new ProviderHealth(providerId, providerName, HealthStatus.OFFLINE,
                Duration.ZERO, Instant.now(), Instant.now(), 0, 0, "Provider is offline");
    }

    public boolean isHealthy() {
        return status == HealthStatus.HEALTHY;
    }

    public boolean isDegraded() {
        return status == HealthStatus.DEGRADED;
    }

    public boolean isAvailable() {
        return status == HealthStatus.HEALTHY || status == HealthStatus.DEGRADED;
    }
}
