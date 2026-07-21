package com.sporekart.ai.provider.models;

import java.time.Duration;
import java.time.Instant;

public record ProviderMetrics(
        String providerId,
        long totalRequests,
        long successfulRequests,
        long failedRequests,
        double averageLatencyMs,
        double p95LatencyMs,
        double p99LatencyMs,
        long totalTokensUsed,
        Instant lastRequestTime,
        Instant windowStart,
        Instant windowEnd) {

    public double successRate() {
        if (totalRequests == 0) return 1.0;
        return (double) successfulRequests / totalRequests;
    }

    public double failureRate() {
        return 1.0 - successRate();
    }

    public ProviderMetrics recordSuccess(Duration latency, int tokens) {
        return new ProviderMetrics(
                providerId, totalRequests + 1, successfulRequests + 1, failedRequests,
                recalculateAverage(latency.toMillis()), p95LatencyMs, p99LatencyMs,
                totalTokensUsed + tokens, Instant.now(), windowStart, windowEnd);
    }

    public ProviderMetrics recordFailure() {
        return new ProviderMetrics(
                providerId, totalRequests + 1, successfulRequests, failedRequests + 1,
                averageLatencyMs, p95LatencyMs, p99LatencyMs,
                totalTokensUsed, Instant.now(), windowStart, windowEnd);
    }

    private double recalculateAverage(long newLatencyMs) {
        if (totalRequests == 0) return newLatencyMs;
        return ((averageLatencyMs * totalRequests) + newLatencyMs) / (totalRequests + 1);
    }
}
