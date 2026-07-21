package com.sporekart.ai.gateway.health;

import java.time.Instant;
import java.util.Map;

public record HealthCheckResult(
    String status,
    Map<String, Object> details,
    Instant timestamp,
    long uptime
) {
    public static HealthCheckResult up(Map<String, Object> details) {
        return new HealthCheckResult("UP", details, Instant.now(), 0);
    }

    public static HealthCheckResult down(String reason) {
        return new HealthCheckResult("DOWN", Map.of("reason", reason), Instant.now(), 0);
    }

    public static HealthCheckResult degraded(String reason) {
        return new HealthCheckResult("DEGRADED", Map.of("reason", reason), Instant.now(), 0);
    }
}
