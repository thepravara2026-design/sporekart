package com.sporekart.ai.providers.metrics;

import java.time.Instant;

public record MetricsModel(
    String providerId,
    double averageLatency,
    double p95Latency,
    double p99Latency,
    double errorRate,
    double successRate,
    double availabilityPercentage,
    double uptimePercentage,
    double averageRecoveryTime,
    int circuitOpens,
    int circuitCloses,
    double healthScore,
    double heartbeatDelay,
    double queueTime,
    double processingTime,
    Instant timestamp
) {}
