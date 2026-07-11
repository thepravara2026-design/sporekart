package com.sporekart.ai.provider.infrastructure;

import com.sporekart.ai.core.api.AIProvider;
import com.sporekart.ai.core.api.ProviderCapabilities;
import com.sporekart.ai.core.domain.AiRequest;
import com.sporekart.ai.core.domain.AiResponse;
import com.sporekart.ai.provider.api.ProviderPort;
import com.sporekart.ai.provider.domain.Provider;
import com.sporekart.ai.provider.domain.ProviderCapability;
import java.util.Set;

public class AzureOpenAIAdapter implements ProviderPort, AIProvider {
    private static final String PROVIDER_NAME = "AZURE_OPENAI";

    @Override
    public boolean supports(String s) {
        return PROVIDER_NAME.equals(s) || "AZURE_OPENAI".equalsIgnoreCase(s)
                || (s != null && (s.startsWith("gpt") || s.contains("azure")));
    }

    @Override
    public AiResponse generate(AiRequest request, Provider provider) {
        return new AiResponse("[Azure OpenAI stub] " + request.prompt(), PROVIDER_NAME, "gpt-4");
    }

    @Override
    public Set<ProviderCapability> capabilities() {
        return Set.of(ProviderCapability.TEXT_GENERATION, ProviderCapability.CHAT_COMPLETION,
                ProviderCapability.EMBEDDINGS, ProviderCapability.STREAMING);
    }

    @Override
    public boolean isAvailable() { return true; }

    @Override
    public AiResponse generate(AiRequest request) {
        return generate(request, new Provider("azure-1", PROVIDER_NAME, PROVIDER_NAME));
    }

    @Override
    public String getProviderName() { return PROVIDER_NAME; }

    @Override
    public ProviderCapabilities getCapabilities() {
        return new AzureOpenAICapabilities();
    }

    private static class AzureOpenAICapabilities implements ProviderCapabilities {
        @Override
        public String getProviderName() { return PROVIDER_NAME; }
        @Override
        public Set<String> supportedModels() { return Set.of("gpt-4", "gpt-4-turbo", "gpt-3.5-turbo", "text-embedding-ada-002"); }
        @Override
        public int getMaxTokens(String model) { return 8192; }
        @Override
        public boolean supportsStreaming(String model) { return true; }
        @Override
        public boolean supportsEmbedding(String model) { return model != null && model.contains("embedding"); }
        @Override
        public boolean supportsVision(String model) { return false; }
        @Override
        public boolean supportsModeration(String model) { return false; }
        @Override
        public boolean supportsFunctionCalling(String model) { return true; }
        @Override
        public java.util.List<String> getCapabilities() {
            return java.util.List.of("TEXT_GENERATION", "CHAT_COMPLETION", "EMBEDDINGS", "STREAMING");
        }
    }
}
