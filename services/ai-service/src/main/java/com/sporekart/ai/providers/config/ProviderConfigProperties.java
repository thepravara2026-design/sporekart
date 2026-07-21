package com.sporekart.ai.providers.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "sporekart.ai.provider")
public record ProviderConfigProperties(
    RegistryConfig registry,
    HealthConfig health,
    SelectionConfig selection,
    FactoryConfig factory,
    MonitoringConfig monitoring,
    CircuitBreakerConfig circuitBreaker,
    Map<String, ProviderDefinition> providers
) {
    public record RegistryConfig(
        boolean autoRegister,
        boolean autoDiscover,
        boolean validateOnRegister,
        List<String> defaultProviders
    ) {}

    public record HealthConfig(
        boolean enabled,
        Duration heartbeatInterval,
        Duration timeout,
        int failureThreshold
    ) {}

    public record SelectionConfig(
        String defaultStrategy,
        List<String> strategies,
        Map<String, Integer> strategyPriority,
        boolean fallbackEnabled,
        List<String> fallbackOrder
    ) {}

    public record FactoryConfig(
        boolean lazyInitialization,
        boolean cacheInstances,
        Duration instanceTTL
    ) {}

    public record MonitoringConfig(
        boolean metricsEnabled,
        boolean healthEnabled,
        boolean costTrackingEnabled,
        boolean performanceTrackingEnabled
    ) {}

    public record CircuitBreakerConfig(
        boolean enabled,
        int failureThreshold,
        Duration timeout,
        int halfOpenMaxCalls
    ) {}

    public record ProviderDefinition(
        String type,
        String model,
        String endpoint,
        int priority,
        boolean enabled,
        Map<String, String> credentials,
        Map<String, Object> options
    ) {}
}
