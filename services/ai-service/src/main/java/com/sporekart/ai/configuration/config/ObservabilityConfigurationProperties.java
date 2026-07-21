package com.sporekart.ai.configuration.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "sporekart.ai.observability")
public record ObservabilityConfigurationProperties(
    boolean loggingEnabled,
    boolean metricsEnabled,
    boolean tracingEnabled,
    boolean healthEnabled,
    boolean auditEnabled,
    boolean sentryEnabled,
    boolean prometheusEnabled,
    boolean grafanaEnabled,
    boolean openTelemetryEnabled,
    Duration metricsExportInterval,
    Duration traceExportInterval,
    Duration healthCheckInterval,
    String loggingLevel,
    String metricsBackend,
    String tracingBackend
) {
    public ObservabilityConfigurationProperties() {
        this(true, true, true, true, true,
            true, true, true, true,
            Duration.ofSeconds(30), Duration.ofSeconds(60), Duration.ofSeconds(30),
            "INFO", "prometheus", "jaeger");
    }
}
