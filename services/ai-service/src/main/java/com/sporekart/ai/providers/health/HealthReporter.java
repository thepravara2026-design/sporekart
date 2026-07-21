package com.sporekart.ai.providers.health;

public interface HealthReporter {
    HealthReport generateReport(String providerId);
    HealthReport generateAggregateReport();
}

record HealthReport(String providerId, HealthStatus status, String summary, long timestamp) {}
