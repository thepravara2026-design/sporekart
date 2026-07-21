package com.sporekart.ai.configuration.model.provider;

import java.time.Duration;
import java.util.List;

public record GeminiConfig(
    boolean enabled,
    String apiUrl,
    String apiVersion,
    String projectId,
    String region,
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
    public static GeminiConfig defaults() {
        return new GeminiConfig(
            true,
            "https://generativelanguage.googleapis.com",
            "v1beta",
            "",
            "",
            List.of(
                new ProviderModelConfig("gemini-2.0-flash", true, 8192, 0.7, 0.9, 3, true, true, true, true, null),
                new ProviderModelConfig("gemini-2.0-pro", true, 8192, 0.7, 0.9, 3, true, true, true, true, null),
                new ProviderModelConfig("gemini-1.5-pro", true, 8192, 0.7, 0.9, 3, true, true, true, true, null),
                new ProviderModelConfig("gemini-1.5-flash", true, 8192, 0.7, 0.9, 3, true, true, true, true, null),
                new ProviderModelConfig("embedding-001", true, 2048, 0.0, 0.0, 3, false, false, false, false, null)
            ),
            Duration.ofSeconds(60),
            3,
            100,
            360,
            true,
            true,
            true,
            true,
            true
        );
    }
}
