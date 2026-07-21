package com.sporekart.ai.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "sporekart.ai.gateway")
public record GatewayConfigProperties(
    PipelineConfig pipeline,
    RouterConfig router,
    SecurityConfig security,
    RateLimitConfig rateLimit,
    QuotaConfig quota,
    ObservabilityConfig observability,
    HealthConfig health,
    ExecutionConfig execution
) {
    public record PipelineConfig(
        boolean enabled,
        boolean parallelStages,
        List<String> enabledStages,
        List<String> disabledStages,
        Duration stageTimeout,
        int maxRetries
    ) {}

    public record RouterConfig(
        String defaultStrategy,
        List<String> strategies,
        Map<String, Integer> strategyPriority,
        boolean fallbackEnabled,
        List<String> fallbackProviders,
        Duration providerTimeout
    ) {}

    public record SecurityConfig(
        boolean enabled,
        List<String> enabledHooks,
        boolean apiKeyRequired,
        boolean bearerTokenRequired,
        boolean tenantValidationRequired
    ) {}

    public record RateLimitConfig(
        boolean enabled,
        int defaultLimit,
        long windowSeconds,
        Map<String, Integer> endpointLimits
    ) {}

    public record QuotaConfig(
        boolean enabled,
        int defaultTokenLimit,
        String defaultPeriod,
        Map<String, Integer> tenantQuotas
    ) {}

    public record ObservabilityConfig(
        boolean metricsEnabled,
        boolean tracingEnabled,
        boolean auditEnabled,
        String metricsExporter,
        String tracingExporter
    ) {}

    public record HealthConfig(
        boolean enabled,
        List<String> components,
        Duration checkInterval
    ) {}

    public record ExecutionConfig(
        Duration defaultTimeout,
        int maxRetries,
        boolean streamingSupported,
        int maxPayloadSize
    ) {}
}
