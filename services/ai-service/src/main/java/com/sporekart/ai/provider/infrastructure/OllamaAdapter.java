package com.sporekart.ai.provider.infrastructure;

import com.sporekart.ai.core.api.AIProvider;
import com.sporekart.ai.core.api.ProviderCapabilities;
import com.sporekart.ai.core.domain.AiRequest;
import com.sporekart.ai.core.domain.AiResponse;
import com.sporekart.ai.provider.api.ProviderPort;
import com.sporekart.ai.provider.domain.Provider;
import com.sporekart.ai.provider.domain.ProviderCapability;
import java.util.Set;

public class OllamaAdapter implements ProviderPort, AIProvider {
    private static final String PROVIDER_NAME = "OLLAMA";

    @Override
    public boolean supports(String s) {
        return PROVIDER_NAME.equals(s) || "OLLAMA".equalsIgnoreCase(s) || s != null;
    }

    @Override
    public AiResponse generate(AiRequest request, Provider provider) {
        return new AiResponse("[Ollama stub] " + request.prompt(), PROVIDER_NAME, "llama3");
    }

    @Override
    public Set<ProviderCapability> capabilities() {
        return Set.of(ProviderCapability.TEXT_GENERATION, ProviderCapability.CHAT_COMPLETION,
                ProviderCapability.STREAMING);
    }

    @Override
    public boolean isAvailable() { return true; }

    @Override
    public AiResponse generate(AiRequest request) {
        return generate(request, new Provider("ollama-1", PROVIDER_NAME, PROVIDER_NAME));
    }

    @Override
    public String getProviderName() { return PROVIDER_NAME; }

    @Override
    public ProviderCapabilities getCapabilities() {
        return new OllamaCapabilities();
    }

    private static class OllamaCapabilities implements ProviderCapabilities {
        @Override
        public String getProviderName() { return PROVIDER_NAME; }
        @Override
        public Set<String> supportedModels() { return Set.of("llama3", "llama3:70b", "mistral", "mixtral"); }
        @Override
        public int getMaxTokens(String model) { return 4096; }
        @Override
        public boolean supportsStreaming(String model) { return true; }
        @Override
        public boolean supportsEmbedding(String model) { return false; }
        @Override
        public boolean supportsVision(String model) { return model != null && model.contains("llava"); }
        @Override
        public boolean supportsModeration(String model) { return false; }
        @Override
        public boolean supportsFunctionCalling(String model) { return false; }
        @Override
        public java.util.List<String> getCapabilities() {
            return java.util.List.of("TEXT_GENERATION", "CHAT_COMPLETION", "STREAMING");
        }
    }
}
