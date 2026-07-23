package com.sporekart.marketplace.domain;

import java.time.Instant;
import java.util.Map;

public record PluginHealthStatus(
    String pluginId,
    String status,
    Instant lastCheckedAt,
    long responseTimeMs,
    Map<String, Object> details,
    int consecutiveFailures
) {
    public boolean isHealthy() { return "HEALTHY".equals(status); }
    public boolean isUnhealthy() { return "UNHEALTHY".equals(status); }
}