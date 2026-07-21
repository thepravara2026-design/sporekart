package com.sporekart.ai.configuration.model.provider;

import java.time.Duration;
import java.util.List;

public record OllamaConfig(
    boolean enabled,
    String baseUrl,
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
    public static OllamaConfig defaults() {
        return new OllamaConfig(
            true,
            "http://localhost:11434",
            List.of(
                new ProviderModelConfig("llama3", true, 2048, 0.7, 0.9, 3, true, false, false, false, null),
                new ProviderModelConfig("mistral", true, 2048, 0.7, 0.9, 3, true, false, false, false, null),
                new ProviderModelConfig("mixtral", true, 2048, 0.7, 0.9, 3, true, false, false, false, null),
                new ProviderModelConfig("codellama", true, 2048, 0.7, 0.9, 3, true, false, false, false, null),
                new ProviderModelConfig("nomic-embed-text", true, 8191, 0.0, 0.0, 3, false, false, false, false, null)
            ),
            Duration.ofSeconds(120),
            3,
            10,
            30,
            true,
            true,
            false,
            false,
            true
        );
    }
}
