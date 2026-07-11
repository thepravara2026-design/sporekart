package com.sporekart.ai.provider.infrastructure;

import com.sporekart.ai.core.api.AIProvider;
import com.sporekart.ai.core.api.ProviderCapabilities;
import com.sporekart.ai.core.domain.AiRequest;
import com.sporekart.ai.core.domain.AiResponse;
import com.sporekart.ai.provider.api.ProviderPort;
import com.sporekart.ai.provider.domain.Provider;
import com.sporekart.ai.provider.domain.ProviderCapability;
import java.util.Set;

public class LocalLLMAdapter implements ProviderPort, AIProvider {
    private static final String PROVIDER_NAME = "LOCAL_LLM";

    @Override
    public boolean supports(String s) {
        return PROVIDER_NAME.equals(s) || "LOCAL_LLM".equalsIgnoreCase(s)
                || "LOCAL".equalsIgnoreCase(s) || s != null;
    }

    @Override
    public AiResponse generate(AiRequest request, Provider provider) {
        return new AiResponse("[Local LLM stub] " + request.prompt(), PROVIDER_NAME, "local-model");
    }

    @Override
    public Set<ProviderCapability> capabilities() {
        return Set.of(ProviderCapability.TEXT_GENERATION, ProviderCapability.CHAT_COMPLETION);
    }

    @Override
    public boolean isAvailable() { return true; }

    @Override
    public AiResponse generate(AiRequest request) {
        return generate(request, new Provider("local-1", PROVIDER_NAME, PROVIDER_NAME));
    }

    @Override
    public String getProviderName() { return PROVIDER_NAME; }

    @Override
    public ProviderCapabilities getCapabilities() {
        return new LocalLLMCapabilities();
    }

    private static class LocalLLMCapabilities implements ProviderCapabilities {
        @Override
        public String getProviderName() { return PROVIDER_NAME; }
        @Override
        public Set<String> supportedModels() { return Set.of("local-model", "local-chat"); }
        @Override
        public int getMaxTokens(String model) { return 2048; }
        @Override
        public boolean supportsStreaming(String model) { return false; }
        @Override
        public boolean supportsEmbedding(String model) { return false; }
        @Override
        public boolean supportsVision(String model) { return false; }
        @Override
        public boolean supportsModeration(String model) { return false; }
        @Override
        public boolean supportsFunctionCalling(String model) { return false; }
        @Override
        public java.util.List<String> getCapabilities() {
            return java.util.List.of("TEXT_GENERATION", "CHAT_COMPLETION");
        }
    }
}
