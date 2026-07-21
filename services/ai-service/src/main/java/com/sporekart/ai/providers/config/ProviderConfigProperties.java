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
    CacheConfig cache,
    DiscoveryConfig discovery,
    AuditConfig audit,
    SyncConfig sync,
    Map<String, ProviderDefinition> providers
) {
    public record RegistryConfig(
        boolean autoRegister,
        boolean autoDiscover,
        boolean validateOnRegister,
        boolean catalogEnabled,
        boolean discoveryEnabled,
        boolean activationEnabled,
        boolean versioningEnabled,
        boolean auditEnabled,
        boolean cacheEnabled,
        boolean syncEnabled,
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

    public record CacheConfig(
        boolean enabled,
        Duration metadataTtl,
        Duration capabilityTtl,
        Duration healthTtl,
        Duration discoveryTtl,
        int maxEntries
    ) {}

    public record DiscoveryConfig(
        boolean automaticDiscovery,
        boolean environmentDiscovery,
        boolean cloudDiscovery,
        boolean pluginDiscovery,
        boolean localDiscovery,
        boolean dynamicDiscovery
    ) {}

    public record AuditConfig(
        boolean enabled,
        boolean logRegistration,
        boolean logActivation,
        boolean logConfiguration,
        boolean logHealth,
        int maxEntries
    ) {}

    public record SyncConfig(
        boolean enabled,
        boolean clusterSync,
        boolean cacheSync,
        boolean metadataSync,
        boolean configurationSync,
        Duration syncInterval
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
