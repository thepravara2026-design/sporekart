package com.sporekart.ai.provider.configuration;

import java.util.Map;
import java.util.Optional;

public record ProviderConfig(
        String providerId,
        String providerType,
        String apiKey,
        String endpoint,
        int timeoutMs,
        int maxRetries,
        Map<String, String> headers,
        String organization,
        String projectId,
        String deploymentName,
        String modelOverride,
        int rateLimitRpm,
        int rateLimitTpm,
        boolean enabled) {

    public ProviderConfig {
        headers = headers == null ? Map.of() : Map.copyOf(headers);
    }

    public Optional<String> getApiKey() {
        return Optional.ofNullable(apiKey).filter(s -> !s.isBlank());
    }

    public Optional<String> getEndpoint() {
        return Optional.ofNullable(endpoint).filter(s -> !s.isBlank());
    }

    public Optional<String> getOrganization() {
        return Optional.ofNullable(organization).filter(s -> !s.isBlank());
    }

    public Optional<String> getProjectId() {
        return Optional.ofNullable(projectId).filter(s -> !s.isBlank());
    }

    public Optional<String> getDeploymentName() {
        return Optional.ofNullable(deploymentName).filter(s -> !s.isBlank());
    }

    public Optional<String> getModelOverride() {
        return Optional.ofNullable(modelOverride).filter(s -> !s.isBlank());
    }

    public String resolveEndpoint() {
        return getEndpoint().orElseThrow(() -> new IllegalStateException(
                "Endpoint not configured for provider: " + providerType));
    }
}
