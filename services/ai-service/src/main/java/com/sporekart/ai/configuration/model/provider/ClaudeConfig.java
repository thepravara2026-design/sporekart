package com.sporekart.ai.configuration.model.provider;

import java.time.Duration;
import java.util.List;

public record ClaudeConfig(
    boolean enabled,
    String apiUrl,
    String apiVersion,
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
    public static ClaudeConfig defaults() {
        return new ClaudeConfig(
            true,
            "https://api.anthropic.com/v1",
            "2023-06-01",
            List.of(
                new ProviderModelConfig("claude-opus-4", true, 4096, 0.7, 0.9, 3, true, true, true, true, null),
                new ProviderModelConfig("claude-sonnet-4", true, 4096, 0.7, 0.9, 3, true, true, true, true, null),
                new ProviderModelConfig("claude-3.5-sonnet", true, 4096, 0.7, 0.9, 3, true, true, true, true, null),
                new ProviderModelConfig("claude-3.5-haiku", true, 4096, 0.7, 0.9, 3, true, true, false, true, null)
            ),
            Duration.ofSeconds(60),
            3,
            100,
            200,
            true,
            true,
            true,
            true,
            false
        );
    }
}
