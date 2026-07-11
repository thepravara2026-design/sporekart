package com.sporekart.ai.provider.infrastructure;

import com.sporekart.ai.core.api.AIProvider;
import com.sporekart.ai.core.api.ProviderCapabilities;
import com.sporekart.ai.core.domain.AiRequest;
import com.sporekart.ai.core.domain.AiResponse;
import com.sporekart.ai.provider.api.ProviderPort;
import com.sporekart.ai.provider.domain.Provider;
import com.sporekart.ai.provider.domain.ProviderCapability;
import java.util.Set;

public class ClaudeAdapter implements ProviderPort, AIProvider {
    private static final String PROVIDER_NAME = "CLAUDE";

    @Override
    public boolean supports(String s) {
        return PROVIDER_NAME.equals(s) || "CLAUDE".equalsIgnoreCase(s)
                || (s != null && s.startsWith("claude"));
    }

    @Override
    public AiResponse generate(AiRequest request, Provider provider) {
        return new AiResponse("[Claude stub] " + request.prompt(), PROVIDER_NAME, "claude-3-sonnet");
    }

    @Override
    public Set<ProviderCapability> capabilities() {
        return Set.of(ProviderCapability.TEXT_GENERATION, ProviderCapability.CHAT_COMPLETION,
                ProviderCapability.CODE_GENERATION, ProviderCapability.IMAGE_ANALYSIS,
                ProviderCapability.STREAMING);
    }

    @Override
    public boolean isAvailable() { return true; }

    @Override
    public AiResponse generate(AiRequest request) {
        return generate(request, new Provider("claude-1", PROVIDER_NAME, PROVIDER_NAME));
    }

    @Override
    public String getProviderName() { return PROVIDER_NAME; }

    @Override
    public ProviderCapabilities getCapabilities() {
        return new ClaudeCapabilities();
    }

    private static class ClaudeCapabilities implements ProviderCapabilities {
        @Override
        public String getProviderName() { return PROVIDER_NAME; }
        @Override
        public Set<String> supportedModels() { return Set.of("claude-3-opus", "claude-3-sonnet", "claude-3-haiku"); }
        @Override
        public int getMaxTokens(String model) { return 8192; }
        @Override
        public boolean supportsStreaming(String model) { return true; }
        @Override
        public boolean supportsEmbedding(String model) { return false; }
        @Override
        public boolean supportsVision(String model) { return model != null && model.contains("opus"); }
        @Override
        public boolean supportsModeration(String model) { return false; }
        @Override
        public boolean supportsFunctionCalling(String model) { return model != null && model.contains("sonnet"); }
        @Override
        public java.util.List<String> getCapabilities() {
            return java.util.List.of("TEXT_GENERATION", "CHAT_COMPLETION", "CODE_GENERATION", "VISION", "STREAMING");
        }
    }
}
