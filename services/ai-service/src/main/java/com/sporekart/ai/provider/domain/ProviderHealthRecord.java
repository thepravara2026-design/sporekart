package com.sporekart.ai.provider.domain;

import java.time.OffsetDateTime;

public record ProviderHealthRecord(
        String providerType,
        boolean healthy,
        boolean degraded,
        long latencyMs,
        OffsetDateTime lastChecked,
        OffsetDateTime lastFailure,
        int consecutiveFailures,
        String details) {
}
