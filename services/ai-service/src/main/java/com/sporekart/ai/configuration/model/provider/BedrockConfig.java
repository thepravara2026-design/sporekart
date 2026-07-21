package com.sporekart.ai.configuration.model.provider;

import java.time.Duration;
import java.util.List;

public record BedrockConfig(
    boolean enabled,
    String region,
    String endpoint,
    List<ProviderModelConfig> models,
    Duration timeout,
    int maxRetries,
    int maxConcurrency,
    int rateLimitRequestsPerMinute,
    boolean streamingEnabled,
    boolean visionEnabled,
    boolean reasoningEnabled,
    boolean functionCallingEnabled,
    boolean embeddingsEnabled
) {
    public static BedrockConfig defaults() {
        return new BedrockConfig(
            true,
            "us-east-1",
            "https://bedrock-runtime.us-east-1.amazonaws.com",
            List.of(
                new ProviderModelConfig("claude-3.5-sonnet", true, 4096, 0.7, 0.9, 3, true, true, true, true, null),
                new ProviderModelConfig("claude-3-opus", true, 4096, 0.7, 0.9, 3, true, true, true, true, null),
                new ProviderModelConfig("llama3-70b", true, 2048, 0.7, 0.9, 3, true, false, false, false, null),
                new ProviderModelConfig("llama3-8b", true, 2048, 0.7, 0.9, 3, true, false, false, false, null),
                new ProviderModelConfig("titan-embedding-v2", true, 8191, 0.0, 0.0, 3, false, false, false, false, null)
            ),
            Duration.ofSeconds(60),
            3,
            50,
            100,
            true,
            true,
            true,
            false,
            true
        );
    }
}
