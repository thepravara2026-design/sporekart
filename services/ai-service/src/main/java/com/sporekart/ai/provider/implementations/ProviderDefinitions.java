package com.sporekart.ai.provider.implementations;

import com.sporekart.ai.provider.registry.ProviderRegistry;

import java.util.List;

public class ProviderDefinitions {

    public static final List<ProviderDefinition> ALL_PROVIDERS = List.of(
            new ProviderDefinition("openai-1", "OPENAI", "1.0.0"),
            new ProviderDefinition("gemini-1", "GEMINI", "1.0.0"),
            new ProviderDefinition("claude-1", "CLAUDE", "1.0.0"),
            new ProviderDefinition("azure-openai-1", "AZURE_OPENAI", "1.0.0"),
            new ProviderDefinition("ollama-1", "OLLAMA", "1.0.0"),
            new ProviderDefinition("groq-1", "GROQ", "1.0.0"),
            new ProviderDefinition("mistral-1", "MISTRAL", "1.0.0"),
            new ProviderDefinition("openrouter-1", "OPENROUTER", "1.0.0"),
            new ProviderDefinition("bedrock-1", "BEDROCK", "1.0.0"),
            new ProviderDefinition("togetherai-1", "TOGETHER_AI", "1.0.0")
    );

    public static void registerAllMockProviders(ProviderRegistry registry) {
        for (var def : ALL_PROVIDERS) {
            var provider = new MockAIProvider(def.id, def.name, def.version);
            registry.register(provider);
        }
    }

    public record ProviderDefinition(String id, String name, String version) {}
}
