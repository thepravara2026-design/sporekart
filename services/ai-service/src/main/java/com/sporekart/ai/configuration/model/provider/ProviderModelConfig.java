package com.sporekart.ai.configuration.model.provider;

public record ProviderModelConfig(
    String name,
    boolean enabled,
    int maxTokens,
    double temperature,
    double topP,
    int maxRetries,
    boolean streamingSupported,
    boolean visionSupported,
    boolean reasoningSupported,
    boolean functionCallingSupported,
    Double stopSequence
) {
    public static ProviderModelConfig of(String name) {
        return new ProviderModelConfig(name, true, 2048, 0.7, 0.9, 3,
            true, false, false, false, null);
    }
}
