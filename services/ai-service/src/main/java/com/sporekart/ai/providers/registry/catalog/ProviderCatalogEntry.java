package com.sporekart.ai.providers.registry.catalog;

import com.sporekart.ai.providers.ProviderStatus;
import com.sporekart.ai.providers.ProviderType;
import com.sporekart.ai.providers.capability.ProviderCapability;

import java.time.Instant;
import java.util.List;

public record ProviderCatalogEntry(
    String providerId,
    String providerName,
    String displayName,
    ProviderType type,
    String version,
    List<ProviderCapability> capabilities,
    List<String> supportedModels,
    ProviderStatus status,
    int priority,
    int weight,
    String region,
    boolean available,
    long latency,
    String pricingTier,
    long contextWindow,
    int maximumTokens,
    boolean streamingSupported,
    boolean embeddingSupported,
    boolean visionSupported,
    boolean reasoningSupported,
    boolean toolCallingSupported,
    boolean jsonModeSupported,
    String complianceLevel,
    Instant registeredAt,
    Instant lastUpdatedAt
) {}
