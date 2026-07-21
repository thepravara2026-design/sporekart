package com.sporekart.ai.configuration.domain;

import java.time.Duration;

public record GatewayConfiguration(
    boolean enabled,
    String basePath,
    Duration requestTimeout,
    int maxRequestSize,
    boolean rateLimiterEnabled,
    int rateLimitRequestsPerMinute,
    int rateLimitBurst,
    boolean circuitBreakerEnabled,
    int circuitBreakerThreshold,
    Duration circuitBreakerResetTimeout,
    boolean auditEnabled,
    boolean cachingEnabled,
    Duration cacheTtl,
    boolean streamingEnabled,
    int maxStreamingDuration
) {
    public static GatewayConfiguration defaults() {
        return new GatewayConfiguration(
            true, "/api/v1/ai", Duration.ofSeconds(60), 32000,
            true, 100, 20,
            true, 5, Duration.ofSeconds(30),
            true, true, Duration.ofMinutes(5),
            true, 120
        );
    }
}
