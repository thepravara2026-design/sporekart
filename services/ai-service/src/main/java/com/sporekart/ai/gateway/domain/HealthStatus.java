package com.sporekart.ai.gateway.domain;

import java.time.Instant;
import java.util.Map;

public record HealthStatus(
    String component,
    boolean healthy,
    String status,
    Instant lastChecked,
    Duration responseTime,
    Map<String, Object> details
) {
    public record Duration(long millis) {
        public static Duration of(long millis) { return new Duration(millis); }
    }
}
