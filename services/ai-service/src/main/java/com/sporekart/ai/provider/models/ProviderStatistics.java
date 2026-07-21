package com.sporekart.ai.provider.models;

import java.time.Duration;
import java.time.Instant;

public record ProviderStatistics(
        String providerId,
        long totalCalls,
        long successfulCalls,
        long failedCalls,
        long timeoutCalls,
        long rateLimitedCalls,
        double averageResponseTimeMs,
        double minResponseTimeMs,
        double maxResponseTimeMs,
        double totalCost,
        Instant lastInteraction,
        Duration totalUptime) {

    public double successRate() {
        if (totalCalls == 0) return 1.0;
        return (double) successfulCalls / totalCalls;
    }

    public ProviderStatistics recordCall(boolean success, Duration responseTime, double cost) {
        return new ProviderStatistics(
                providerId, totalCalls + 1,
                successfulCalls + (success ? 1 : 0),
                failedCalls + (success ? 0 : 1),
                timeoutCalls, rateLimitedCalls,
                recalculateAverage(responseTime.toMillis()),
                Math.min(minResponseTimeMs, responseTime.toMillis()),
                Math.max(maxResponseTimeMs, responseTime.toMillis()),
                totalCost + cost, Instant.now(), totalUptime);
    }

    private double recalculateAverage(double newValue) {
        if (totalCalls == 0) return newValue;
        return ((averageResponseTimeMs * totalCalls) + newValue) / (totalCalls + 1);
    }
}
