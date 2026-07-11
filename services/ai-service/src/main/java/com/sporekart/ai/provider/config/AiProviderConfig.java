package com.sporekart.ai.provider.config;

import com.sporekart.ai.provider.application.ProviderRegistryImpl;
import com.sporekart.ai.provider.domain.Provider;
import com.sporekart.ai.provider.infrastructure.*;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class AiProviderConfig {

    private final ProviderRegistryImpl registry;

    public AiProviderConfig(ProviderRegistryImpl registry) {
        this.registry = registry;
    }

    @PostConstruct
    public void registerAdapters() {
        registry.register("GEMINI", new GeminiAdapter());
        registry.register("OPENAI", new OpenAIAdapter());
        registry.register("CLAUDE", new ClaudeAdapter());
        registry.register("AZURE_OPENAI", new AzureOpenAIAdapter());
        registry.register("BEDROCK", new BedrockAdapter());
        registry.register("OLLAMA", new OllamaAdapter());
        registry.register("MISTRAL", new MistralAdapter());
        registry.register("LOCAL_LLM", new LocalLLMAdapter());

        registry.registerProviderMetadata(new Provider("gemini-1", "Gemini", "GEMINI", true,
                Map.of("endpoint", "https://generativelanguage.googleapis.com")));
        registry.registerProviderMetadata(new Provider("openai-1", "OpenAI", "OPENAI", true,
                Map.of("endpoint", "https://api.openai.com")));
        registry.registerProviderMetadata(new Provider("claude-1", "Claude", "CLAUDE", true,
                Map.of("endpoint", "https://api.anthropic.com")));
        registry.registerProviderMetadata(new Provider("azure-1", "Azure OpenAI", "AZURE_OPENAI", true,
                Map.of("endpoint", "https://your-resource.openai.azure.com")));
        registry.registerProviderMetadata(new Provider("bedrock-1", "AWS Bedrock", "BEDROCK", true,
                Map.of("endpoint", "https://bedrock-runtime.us-east-1.amazonaws.com")));
        registry.registerProviderMetadata(new Provider("ollama-1", "Ollama", "OLLAMA", true,
                Map.of("endpoint", "http://localhost:11434")));
        registry.registerProviderMetadata(new Provider("mistral-1", "Mistral", "MISTRAL", true,
                Map.of("endpoint", "https://api.mistral.ai")));
        registry.registerProviderMetadata(new Provider("local-1", "Local LLM", "LOCAL_LLM", true,
                Map.of("endpoint", "http://localhost:8080")));
    }
}
