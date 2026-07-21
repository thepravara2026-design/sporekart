package com.sporekart.ai.configuration.config;

import com.sporekart.ai.configuration.model.provider.*;

import java.util.List;

public record ProviderConfigurationProperties(
    OpenAiConfig openai,
    GeminiConfig gemini,
    ClaudeConfig claude,
    AzureOpenAiConfig azure,
    BedrockConfig bedrock,
    OllamaConfig ollama,
    OpenRouterConfig openrouter,
    List<String> providerOrder,
    String defaultProvider,
    boolean failoverEnabled,
    int healthCheckIntervalSeconds
) {
    public ProviderConfigurationProperties() {
        this(
            OpenAiConfig.defaults(),
            GeminiConfig.defaults(),
            ClaudeConfig.defaults(),
            AzureOpenAiConfig.defaults(),
            BedrockConfig.defaults(),
            OllamaConfig.defaults(),
            OpenRouterConfig.defaults(),
            List.of("openai", "gemini", "claude", "azure", "bedrock", "ollama", "openrouter"),
            "openai",
            true,
            30
        );
    }
}
