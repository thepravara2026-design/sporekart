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
    LifecycleConfig lifecycle,
    HeartbeatConfig heartbeat,
    RecoveryConfig recovery,
    MaintenanceConfig maintenance,
    AvailabilityConfig availability,
    MetricsConfig metrics,
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
        int failureThreshold,
        int successThreshold,
        boolean autoRecovery,
        Duration cooldownPeriod,
        boolean readinessEnabled,
        boolean livenessEnabled,
        boolean diagnosticsEnabled
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
        boolean performanceTrackingEnabled,
        boolean lifecycleEnabled,
        boolean availabilityEnabled,
        boolean heartbeatEnabled
    ) {}

    public record CircuitBreakerConfig(
        boolean enabled,
        int failureThreshold,
        int successThreshold,
        Duration timeout,
        Duration halfOpenMaxDuration,
        int halfOpenMaxCalls,
        boolean automaticReset,
        Duration cooldownPeriod
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
        boolean logLifecycle,
        boolean logAvailability,
        boolean logRecovery,
        boolean logCircuit,
        boolean logMaintenance,
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

    public record LifecycleConfig(
        boolean enabled,
        boolean stateMachineEnabled,
        boolean transitionValidationEnabled,
        boolean autoTransition,
        boolean historyEnabled
    ) {}

    public record HeartbeatConfig(
        boolean enabled,
        Duration interval,
        Duration timeout,
        Duration expiryThreshold,
        int missedThreshold
    ) {}

    public record RecoveryConfig(
        boolean enabled,
        boolean autoRecovery,
        boolean manualRecovery,
        int maxRetries,
        Duration retryInterval,
        Duration backoffMultiplier
    ) {}

    public record MaintenanceConfig(
        boolean enabled,
        boolean scheduledMaintenance,
        boolean emergencyMaintenance,
        Duration maxDuration,
        boolean requiresApproval
    ) {}

    public record AvailabilityConfig(
        boolean enabled,
        boolean slaTracking,
        double slaTarget,
        boolean eventPublishing
    ) {}

    public record MetricsConfig(
        boolean enabled,
        boolean latencyTracking,
        boolean errorTracking,
        boolean percentileTracking,
        boolean healthScoreTracking
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
