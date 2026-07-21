package com.sporekart.ai.configuration.model.provider;

import java.time.Duration;
import java.util.List;

public record AzureOpenAiConfig(
    boolean enabled,
    String endpoint,
    String apiVersion,
    String deploymentName,
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
    public static AzureOpenAiConfig defaults() {
        return new AzureOpenAiConfig(
            true,
            "https://{resource}.openai.azure.com",
            "2024-02-15-preview",
            "",
            List.of(
                new ProviderModelConfig("gpt-4o", true, 4096, 0.7, 0.9, 3, true, true, true, true, null),
                new ProviderModelConfig("gpt-4o-mini", true, 4096, 0.7, 0.9, 3, true, true, true, true, null),
                new ProviderModelConfig("gpt-4", true, 4096, 0.7, 0.9, 3, true, false, false, true, null),
                new ProviderModelConfig("text-embedding-3-small", true, 8191, 0.0, 0.0, 3, false, false, false, false, null)
            ),
            Duration.ofSeconds(60),
            3,
            100,
            240,
            true,
            true,
            true,
            true,
            true
        );
    }
}
