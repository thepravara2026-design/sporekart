package com.sporekart.ai.providers.health;

import java.time.Instant;
import java.util.Map;

public record HealthSnapshot(
    String snapshotId,
    Instant timestamp,
    Map<String, HealthResponse> providerHealth,
    int totalProviders,
    int healthyCount,
    int degradedCount,
    int unhealthyCount,
    int criticalCount
) {}
