package com.sporekart.ai.providers.metadata;

import com.sporekart.ai.providers.ProviderStatus;
import com.sporekart.ai.providers.ProviderType;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record ProviderMetadata(
    String providerId,
    String providerName,
    ProviderType type,
    String version,
    String description,
    String vendor,
    String website,
    String documentationUrl,
    List<String> supportedModels,
    List<String> regions,
    Map<String, String> pricingTiers,
    long contextWindow,
    int rateLimitPerMinute,
    int rateLimitPerDay,
    boolean streamingSupported,
    Instant registeredAt,
    Instant lastUpdatedAt,
    ProviderStatus status
) {}
