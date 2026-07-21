package com.sporekart.ai.provider.models;

import java.util.List;
import java.util.Optional;

public record ProviderInfo(
        String providerId,
        String providerName,
        String providerType,
        String version,
        String description,
        String website,
        String documentationUrl,
        ProviderHealth.HealthStatus healthStatus,
        ProviderCostInfo.PricingTier pricingTier,
        List<String> supportedModels,
        List<String> supportedModalities,
        int maxTokens,
        boolean streamingSupported,
        boolean visionSupported,
        boolean functionCallingSupported,
        Optional<Integer> priority,
        boolean enabled) {

    public ProviderInfo withPriority(int priority) {
        return new ProviderInfo(providerId, providerName, providerType, version, description,
                website, documentationUrl, healthStatus, pricingTier, supportedModels,
                supportedModalities, maxTokens, streamingSupported, visionSupported,
                functionCallingSupported, Optional.of(priority), enabled);
    }

    public ProviderInfo withHealth(ProviderHealth.HealthStatus status) {
        return new ProviderInfo(providerId, providerName, providerType, version, description,
                website, documentationUrl, status, pricingTier, supportedModels,
                supportedModalities, maxTokens, streamingSupported, visionSupported,
                functionCallingSupported, priority, enabled);
    }

    public ProviderInfo withEnabled(boolean enabled) {
        return new ProviderInfo(providerId, providerName, providerType, version, description,
                website, documentationUrl, healthStatus, pricingTier, supportedModels,
                supportedModalities, maxTokens, streamingSupported, visionSupported,
                functionCallingSupported, priority, enabled);
    }
}
