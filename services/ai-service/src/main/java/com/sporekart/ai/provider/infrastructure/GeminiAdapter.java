package com.sporekart.ai.provider.infrastructure;

import com.sporekart.ai.core.api.AIProvider;
import com.sporekart.ai.core.api.ProviderCapabilities;
import com.sporekart.ai.core.domain.AiRequest;
import com.sporekart.ai.core.domain.AiResponse;
import com.sporekart.ai.provider.api.ProviderPort;
import com.sporekart.ai.provider.domain.Provider;
import com.sporekart.ai.provider.domain.ProviderCapability;
import java.util.LinkedHashSet;
import java.util.Set;

public class GeminiAdapter implements ProviderPort, AIProvider {
    private static final String PROVIDER_NAME = "GEMINI";

    @Override
    public boolean supports(String s) {
        return PROVIDER_NAME.equals(s) || "GEMINI".equalsIgnoreCase(s)
                || (s != null && (s.startsWith("gemini") || s.equals(PROVIDER_NAME)));
    }

    @Override
    public AiResponse generate(AiRequest request, Provider provider) {
        return new AiResponse("[Gemini stub] " + request.prompt(), PROVIDER_NAME, "gemini-pro");
    }

    @Override
    public Set<ProviderCapability> capabilities() {
        return Set.of(ProviderCapability.TEXT_GENERATION, ProviderCapability.CHAT_COMPLETION,
                ProviderCapability.STREAMING, ProviderCapability.IMAGE_ANALYSIS);
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public AiResponse generate(AiRequest request) {
        return generate(request, new Provider("gemini-1", PROVIDER_NAME, PROVIDER_NAME));
    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
    public ProviderCapabilities getCapabilities() {
        return new GeminiCapabilities();
    }

    private static class GeminiCapabilities implements ProviderCapabilities {
        @Override
        public String getProviderName() { return PROVIDER_NAME; }
        @Override
        public Set<String> supportedModels() { return Set.of("gemini-pro", "gemini-pro-vision", "gemini-ultra"); }
        @Override
        public int getMaxTokens(String model) { return 8192; }
        @Override
        public boolean supportsStreaming(String model) { return true; }
        @Override
        public boolean supportsEmbedding(String model) { return "gemini-pro".equals(model); }
        @Override
        public boolean supportsVision(String model) { return model != null && model.contains("vision"); }
        @Override
        public boolean supportsModeration(String model) { return true; }
        @Override
        public boolean supportsFunctionCalling(String model) { return true; }
        @Override
        public java.util.List<String> getCapabilities() {
            return java.util.List.of("TEXT_GENERATION", "CHAT_COMPLETION", "STREAMING", "VISION", "FUNCTION_CALLING");
        }
    }
}
