package com.sporekart.ai.provider.infrastructure;

import com.sporekart.ai.core.api.AIProvider;
import com.sporekart.ai.core.api.ProviderCapabilities;
import com.sporekart.ai.core.domain.AiRequest;
import com.sporekart.ai.core.domain.AiResponse;
import com.sporekart.ai.provider.api.ProviderPort;
import com.sporekart.ai.provider.domain.Provider;
import com.sporekart.ai.provider.domain.ProviderCapability;
import java.util.Set;

public class OpenAIAdapter implements ProviderPort, AIProvider {
    private static final String PROVIDER_NAME = "OPENAI";

    @Override
    public boolean supports(String s) {
        return PROVIDER_NAME.equals(s) || "OPENAI".equalsIgnoreCase(s)
                || (s != null && (s.startsWith("gpt") || s.startsWith("text-")));
    }

    @Override
    public AiResponse generate(AiRequest request, Provider provider) {
        return new AiResponse("[OpenAI stub] " + request.prompt(), PROVIDER_NAME, "gpt-4");
    }

    @Override
    public Set<ProviderCapability> capabilities() {
        return Set.of(ProviderCapability.TEXT_GENERATION, ProviderCapability.CHAT_COMPLETION,
                ProviderCapability.EMBEDDINGS, ProviderCapability.STREAMING,
                ProviderCapability.FUNCTION_CALLING, ProviderCapability.CODE_GENERATION);
    }

    @Override
    public boolean isAvailable() { return true; }

    @Override
    public AiResponse generate(AiRequest request) {
        return generate(request, new Provider("openai-1", PROVIDER_NAME, PROVIDER_NAME));
    }

    @Override
    public String getProviderName() { return PROVIDER_NAME; }

    @Override
    public ProviderCapabilities getCapabilities() {
        return new OpenAICapabilities();
    }

    private static class OpenAICapabilities implements ProviderCapabilities {
        @Override
        public String getProviderName() { return PROVIDER_NAME; }
        @Override
        public Set<String> supportedModels() { return Set.of("gpt-4", "gpt-4-turbo", "gpt-3.5-turbo", "text-embedding-3"); }
        @Override
        public int getMaxTokens(String model) { return model != null && model.contains("gpt-4") ? 8192 : 4096; }
        @Override
        public boolean supportsStreaming(String model) { return true; }
        @Override
        public boolean supportsEmbedding(String model) { return model != null && model.contains("embedding"); }
        @Override
        public boolean supportsVision(String model) { return model != null && model.contains("turbo"); }
        @Override
        public boolean supportsModeration(String model) { return true; }
        @Override
        public boolean supportsFunctionCalling(String model) { return true; }
        @Override
        public java.util.List<String> getCapabilities() {
            return java.util.List.of("TEXT_GENERATION", "CHAT_COMPLETION", "EMBEDDINGS", "STREAMING", "FUNCTION_CALLING", "CODE_GENERATION");
        }
    }
}
