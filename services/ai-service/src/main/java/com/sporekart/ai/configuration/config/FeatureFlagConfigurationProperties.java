package com.sporekart.ai.configuration.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "sporekart.ai.features")
public record FeatureFlagConfigurationProperties(
    boolean aiEnabled,
    boolean gatewayEnabled,
    boolean promptRegistryEnabled,
    boolean conversationEnabled,
    boolean knowledgeEnabled,
    boolean semanticSearchEnabled,
    boolean embeddingsEnabled,
    boolean copilotsEnabled,
    boolean analyticsEnabled,
    boolean monitoringEnabled,
    boolean providerFailoverEnabled,
    boolean rateLimiterEnabled,
    boolean streamingEnabled,
    boolean imageGenerationEnabled,
    boolean audioEnabled,
    boolean visionEnabled,
    boolean reasoningModelsEnabled,
    boolean memoryEnabled,
    boolean runtimeEnabled,
    Map<String, Boolean> custom
) {
    public FeatureFlagConfigurationProperties() {
        this(true, true, true, true, true, true, true, true,
            true, true, true, true, true, true, true, true,
            true, true, true, Map.of());
    }
}
