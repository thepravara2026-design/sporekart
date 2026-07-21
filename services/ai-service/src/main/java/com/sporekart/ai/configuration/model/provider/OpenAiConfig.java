package com.sporekart.ai.configuration.model.provider;

import java.time.Duration;
import java.util.List;

public record OpenAiConfig(
    boolean enabled,
    String apiUrl,
    String apiVersion,
    String organizationId,
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
    public static OpenAiConfig defaults() {
        return new OpenAiConfig(
            true,
            "https://api.openai.com/v1",
            "2024-02-01",
            "",
            List.of(
                new ProviderModelConfig("gpt-4o", true, 4096, 0.7, 0.9, 3, true, true, true, true, null),
                new ProviderModelConfig("gpt-4o-mini", true, 4096, 0.7, 0.9, 3, true, true, false, true, null),
                new ProviderModelConfig("gpt-4-turbo", true, 4096, 0.7, 0.9, 3, true, true, true, true, null),
                new ProviderModelConfig("gpt-4", true, 4096, 0.7, 0.9, 3, true, false, false, true, null),
                new ProviderModelConfig("gpt-3.5-turbo", true, 2048, 0.7, 0.9, 3, true, false, false, true, null),
                new ProviderModelConfig("text-embedding-3-small", true, 8191, 0.0, 0.0, 3, false, false, false, false, null),
                new ProviderModelConfig("text-embedding-3-large", true, 8191, 0.0, 0.0, 3, false, false, false, false, null),
                new ProviderModelConfig("dall-e-3", true, 0, 0.0, 0.0, 3, false, true, false, false, null),
                new ProviderModelConfig("whisper-1", true, 0, 0.0, 0.0, 3, false, false, false, false, null),
                new ProviderModelConfig("tts-1", true, 0, 0.0, 0.0, 3, false, false, false, false, null)
            ),
            Duration.ofSeconds(60),
            3,
            100,
            500,
            true,
            true,
            true,
            true,
            true
        );
    }
}
