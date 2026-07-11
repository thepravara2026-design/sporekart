package com.sporekart.ai.provider.domain;

import java.util.Set;

public record ProviderCapabilityInfo(
        String providerType,
        Set<String> supportedModels,
        int defaultMaxTokens,
        boolean streamingSupported,
        boolean embeddingSupported,
        boolean visionSupported,
        boolean moderationSupported,
        boolean functionCallingSupported) {
}
