package com.sporekart.ai.provider.application;

import com.sporekart.ai.core.api.ProviderConfiguration;
import com.sporekart.ai.core.application.featureflag.FeatureFlagName;
import com.sporekart.ai.core.application.featureflag.FeatureFlagService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ProviderConfigurationService implements ProviderConfiguration {
    private final FeatureFlagService featureFlagService;
    private final Map<String, Map<String, String>> providerConfigs = new ConcurrentHashMap<>();

    public ProviderConfigurationService(FeatureFlagService featureFlagService) {
        this.featureFlagService = featureFlagService;
        initializeDefaults();
    }

    private void initializeDefaults() {
        setProperty("GEMINI", "endpoint", "https://generativelanguage.googleapis.com");
        setProperty("GEMINI", "model", "gemini-pro");
        setProperty("GEMINI", "maxTokens", "2048");
        setProperty("GEMINI", "temperature", "0.7");

        setProperty("OPENAI", "endpoint", "https://api.openai.com");
        setProperty("OPENAI", "model", "gpt-4");
        setProperty("OPENAI", "maxTokens", "2048");
        setProperty("OPENAI", "temperature", "0.7");

        setProperty("CLAUDE", "endpoint", "https://api.anthropic.com");
        setProperty("CLAUDE", "model", "claude-3-sonnet");
        setProperty("CLAUDE", "maxTokens", "2048");
        setProperty("CLAUDE", "temperature", "0.7");

        setProperty("AZURE_OPENAI", "endpoint", "https://your-resource.openai.azure.com");
        setProperty("AZURE_OPENAI", "model", "gpt-4");
        setProperty("AZURE_OPENAI", "apiVersion", "2024-02-15-preview");

        setProperty("BEDROCK", "endpoint", "https://bedrock-runtime.us-east-1.amazonaws.com");
        setProperty("BEDROCK", "model", "anthropic.claude-v2");

        setProperty("OLLAMA", "endpoint", "http://localhost:11434");
        setProperty("OLLAMA", "model", "llama3");

        setProperty("MISTRAL", "endpoint", "https://api.mistral.ai");
        setProperty("MISTRAL", "model", "mistral-large-latest");

        setProperty("LOCAL_LLM", "endpoint", "http://localhost:8080");
        setProperty("LOCAL_LLM", "model", "local-model");
    }

    @Override
    public Optional<String> getProperty(String provider, String key) {
        Map<String, String> props = providerConfigs.get(provider.toUpperCase());
        if (props != null) {
            return Optional.ofNullable(props.get(key));
        }
        return Optional.empty();
    }

    @Override
    public Map<String, String> getAllProperties(String provider) {
        return Map.copyOf(providerConfigs.getOrDefault(provider.toUpperCase(), Map.of()));
    }

    @Override
    public boolean isProviderEnabled(String provider) {
        return switch (provider.toUpperCase()) {
            case "GEMINI" -> featureFlagService.isEnabled(FeatureFlagName.AI_PROVIDER_GEMINI);
            case "OPENAI" -> featureFlagService.isEnabled(FeatureFlagName.AI_PROVIDER_OPENAI);
            case "CLAUDE" -> featureFlagService.isEnabled(FeatureFlagName.AI_PROVIDER_CLAUDE);
            case "AZURE_OPENAI" -> featureFlagService.isEnabled(FeatureFlagName.AI_PROVIDER_AZURE_OPENAI);
            case "BEDROCK" -> featureFlagService.isEnabled(FeatureFlagName.AI_PROVIDER_BEDROCK);
            case "OLLAMA" -> featureFlagService.isEnabled(FeatureFlagName.AI_PROVIDER_OLLAMA);
            case "MISTRAL" -> featureFlagService.isEnabled(FeatureFlagName.AI_PROVIDER_MISTRAL);
            case "LOCAL_LLM" -> featureFlagService.isEnabled(FeatureFlagName.AI_PROVIDER_LOCAL_LLM);
            default -> false;
        };
    }

    @Override
    public void setProperty(String provider, String key, String value) {
        providerConfigs.computeIfAbsent(provider.toUpperCase(), k -> new ConcurrentHashMap<>())
                .put(key, value);
    }

    @Override
    public void reload() {
        providerConfigs.clear();
        initializeDefaults();
    }

    public void updateProviderConfig(String provider, Map<String, String> properties) {
        providerConfigs.put(provider.toUpperCase(), new ConcurrentHashMap<>(properties));
    }
}
