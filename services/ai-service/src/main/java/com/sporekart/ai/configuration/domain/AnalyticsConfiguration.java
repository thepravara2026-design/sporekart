package com.sporekart.ai.configuration.domain;

import java.time.Duration;

public record AnalyticsConfiguration(
    boolean enabled,
    boolean metricsEnabled,
    boolean tracingEnabled,
    boolean auditLoggingEnabled,
    boolean usageTrackingEnabled,
    boolean costTrackingEnabled,
    Duration metricsExportInterval,
    Duration traceExportInterval,
    boolean anomalyDetectionEnabled,
    boolean dashboardEnabled,
    boolean realtimeEnabled,
    int dataRetentionDays,
    String metricsBackend,
    String tracingBackend
) {
    public static AnalyticsConfiguration defaults() {
        return new AnalyticsConfiguration(
            true, true, true, true,
            true, true,
            Duration.ofSeconds(30), Duration.ofSeconds(60),
            false, true, false,
            90, "prometheus", "jaeger"
        );
    }
}
