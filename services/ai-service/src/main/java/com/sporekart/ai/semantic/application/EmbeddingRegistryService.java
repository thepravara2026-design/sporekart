package com.sporekart.ai.semantic.application;

import com.sporekart.ai.semantic.domain.EmbeddingProvider;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmbeddingRegistryService {

    private static final Logger log = LoggerFactory.getLogger(EmbeddingRegistryService.class);

    private final Map<String, ProviderConfig> providers = new ConcurrentHashMap<>();

    public record ProviderConfig(
            EmbeddingProvider provider,
            String name,
            List<String> models,
            int defaultDimensions,
            int maxBatchSize,
            boolean available) {}

    @PostConstruct
    public void initializeDefaults() {
        registerProvider(new ProviderConfig(EmbeddingProvider.OPENAI, "OpenAI",
                List.of("text-embedding-3-small", "text-embedding-3-large", "text-embedding-ada-002"),
                1536, 100, true));
        registerProvider(new ProviderConfig(EmbeddingProvider.GEMINI, "Gemini",
                List.of("text-embedding-004", "text-embedding-005"), 768, 50, true));
        registerProvider(new ProviderConfig(EmbeddingProvider.CLAUDE, "Claude",
                List.of("claude-3-embedding"), 1024, 50, true));
        registerProvider(new ProviderConfig(EmbeddingProvider.AZURE_OPENAI, "Azure OpenAI",
                List.of("text-embedding-3-small", "text-embedding-3-large"), 1536, 100, true));
        registerProvider(new ProviderConfig(EmbeddingProvider.BEDROCK, "Bedrock",
                List.of("amazon-titan-embed-text", "cohere-embed-english"), 1024, 50, true));
        registerProvider(new ProviderConfig(EmbeddingProvider.OLLAMA, "Ollama",
                List.of("nomic-embed-text", "all-minilm"), 384, 20, true));
        registerProvider(new ProviderConfig(EmbeddingProvider.MISTRAL, "Mistral",
                List.of("mistral-embed"), 1024, 50, true));
        registerProvider(new ProviderConfig(EmbeddingProvider.LOCAL, "Local",
                List.of("local-embedding"), 384, 10, true));
        registerProvider(new ProviderConfig(EmbeddingProvider.CUSTOM, "Custom",
                List.of("custom-embedding"), 768, 50, false));
        log.info("Initialized {} embedding providers", providers.size());
    }

    public void registerProvider(ProviderConfig config) {
        providers.put(config.provider().name(), config);
        log.debug("Registered embedding provider: {}", config.name());
    }

    public Optional<ProviderConfig> getProvider(String name) {
        return Optional.ofNullable(providers.get(name.toUpperCase()));
    }

    public Optional<ProviderConfig> getProvider(EmbeddingProvider provider) {
        return Optional.ofNullable(providers.get(provider.name()));
    }

    public List<ProviderConfig> listProviders() {
        return List.copyOf(providers.values());
    }

    public List<ProviderConfig> listAvailableProviders() {
        return providers.values().stream()
                .filter(ProviderConfig::available)
                .toList();
    }

    public boolean isProviderAvailable(String name) {
        return getProvider(name).map(ProviderConfig::available).orElse(false);
    }

    public int getDefaultDimensions(String providerName) {
        return getProvider(providerName)
                .map(ProviderConfig::defaultDimensions)
                .orElse(768);
    }

    public List<String> getModels(String providerName) {
        return getProvider(providerName)
                .map(ProviderConfig::models)
                .orElse(List.of());
    }

    public boolean isValidModel(String providerName, String model) {
        return getProvider(providerName)
                .map(config -> config.models().contains(model))
                .orElse(false);
    }

    public void setAvailability(String providerName, boolean available) {
        getProvider(providerName).ifPresent(config -> {
            ProviderConfig updated = new ProviderConfig(config.provider(), config.name(),
                    config.models(), config.defaultDimensions(), config.maxBatchSize(), available);
            providers.put(providerName.toUpperCase(), updated);
            log.info("Updated provider {} availability: {}", providerName, available);
        });
    }
}
