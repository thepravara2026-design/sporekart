package com.sporekart.ai.core.api;

import java.time.OffsetDateTime;

public record ProviderHealth(
        String providerName,
        boolean healthy,
        boolean degraded,
        long latencyMs,
        OffsetDateTime lastChecked,
        String details) {
    public static ProviderHealth healthy(String providerName) {
        return new ProviderHealth(providerName, true, false, 0, OffsetDateTime.now(), "Operational");
    }

    public static ProviderHealth unhealthy(String providerName, String details) {
        return new ProviderHealth(providerName, false, false, 0, OffsetDateTime.now(), details);
    }

    public static ProviderHealth degraded(String providerName, long latencyMs, String details) {
        return new ProviderHealth(providerName, true, true, latencyMs, OffsetDateTime.now(), details);
    }
}
