package com.sporekart.ai.configuration.domain;

import java.time.Duration;

public record ProviderConfiguration(
    boolean enabled,
    String providerName,
    String providerType,
    String apiUrl,
    String defaultModel,
    Duration timeout,
    int maxRetries,
    int maxConcurrency,
    int rateLimitRequestsPerMinute,
    boolean streamingSupported,
    boolean visionSupported,
    boolean reasoningSupported,
    boolean embeddingsSupported,
    boolean functionCallingSupported,
    boolean failoverEnabled,
    String failoverProvider,
    double failoverThreshold,
    ConfigurationSource source
) {
    public static ProviderConfiguration disabled(String name) {
        return new ProviderConfiguration(false, name, "", "", "",
            Duration.ofSeconds(30), 3, 10, 60,
            false, false, false, false, false,
            false, "", 0.0, ConfigurationSource.DEFAULT);
    }
}
