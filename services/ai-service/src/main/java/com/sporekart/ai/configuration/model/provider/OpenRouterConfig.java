package com.sporekart.ai.configuration.model.provider;

import java.time.Duration;
import java.util.List;

public record OpenRouterConfig(
    boolean enabled,
    String apiUrl,
    List<ProviderModelConfig> models,
    Duration timeout,
    int maxRetries,
    int maxConcurrency,
    int rateLimitRequestsPerMinute,
    boolean streamingEnabled,
    boolean visionEnabled,
    boolean reasoningEnabled,
    boolean functionCallingEnabled,
    boolean embeddingsEnabled,
    boolean allowFallback
) {
    public static OpenRouterConfig defaults() {
        return new OpenRouterConfig(
            true,
            "https://openrouter.ai/api/v1",
            List.of(
                new ProviderModelConfig("openrouter/auto", true, 4096, 0.7, 0.9, 3, true, true, true, true, null)
            ),
            Duration.ofSeconds(60),
            3,
            50,
            200,
            true,
            true,
            true,
            true,
            true,
            true
        );
    }
}
