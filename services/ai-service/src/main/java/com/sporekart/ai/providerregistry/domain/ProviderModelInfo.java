package com.sporekart.ai.providerregistry.domain;

public record ProviderModelInfo(
        String modelId,
        String modelName,
        int contextWindow,
        int maxTokens,
        boolean streamingSupported,
        boolean toolCallingSupported,
        boolean embeddingsSupported,
        boolean imageSupported,
        boolean audioSupported
) {}
