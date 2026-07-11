package com.sporekart.ai.provider.domain;

public record ProviderModel(
        String id,
        String providerType,
        String name,
        int maxTokens,
        boolean supportsStreaming,
        boolean supportsEmbedding,
        boolean supportsVision,
        boolean supportsFunctionCalling,
        double costPerInputToken,
        double costPerOutputToken) {
}
