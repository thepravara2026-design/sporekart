package com.sporekart.ai.configuration.domain;

import java.time.Duration;

public record MonitoringConfiguration(
    boolean enabled,
    boolean metricsEnabled,
    boolean tracingEnabled,
    boolean healthCheckEnabled,
    boolean loggingEnabled,
    boolean auditEnabled,
    boolean alertingEnabled,
    String metricsProvider,
    String tracingProvider,
    String loggingProvider,
    String alertingProvider,
    Duration metricsExportInterval,
    Duration traceExportInterval,
    Duration healthCheckInterval,
    boolean sentryEnabled,
    boolean prometheusEnabled,
    boolean grafanaEnabled,
    boolean openTelemetryEnabled
) {
    public static MonitoringConfiguration defaults() {
        return new MonitoringConfiguration(
            true, true, true, true,
            true, true, true,
            "prometheus", "jaeger", "logback", "pagerduty",
            Duration.ofSeconds(30), Duration.ofSeconds(60), Duration.ofSeconds(30),
            true, true, true, true
        );
    }
}
