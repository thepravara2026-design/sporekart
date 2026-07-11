package com.sporekart.ai.core.application.featureflag;

import com.sporekart.ai.core.domain.AiModule;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service("coreFeatureFlagService")
public class FeatureFlagService {
    private final Map<FeatureFlagName, Boolean> flags;

    public FeatureFlagService(AiFeatureFlagProperties properties) {
        this.flags = new ConcurrentHashMap<>(properties.toMap());
    }

    public boolean isEnabled(FeatureFlagName flag) {
        return flags.getOrDefault(flag, false);
    }

    public boolean isModuleEnabled(AiModule module) {
        return switch (module) {
            case CORE -> true;
            case GATEWAY -> isEnabled(FeatureFlagName.AI_PLATFORM_ENABLED) && isEnabled(FeatureFlagName.AI_GATEWAY_ENABLED);
            case PROVIDER -> isEnabled(FeatureFlagName.AI_PLATFORM_ENABLED);
            case PROMPT -> isEnabled(FeatureFlagName.AI_PLATFORM_ENABLED);
            case CHAT -> isEnabled(FeatureFlagName.AI_CHAT_ENABLED) && isEnabled(FeatureFlagName.AI_PLATFORM_ENABLED);
            case RAG -> isEnabled(FeatureFlagName.AI_RAG_ENABLED) && isEnabled(FeatureFlagName.AI_PLATFORM_ENABLED);
            case SEARCH -> isEnabled(FeatureFlagName.AI_SEARCH_ENABLED) && isEnabled(FeatureFlagName.AI_PLATFORM_ENABLED);
            case CONTENT -> isEnabled(FeatureFlagName.AI_CONTENT_ENABLED) && isEnabled(FeatureFlagName.AI_PLATFORM_ENABLED);
            case WORKFLOW -> isEnabled(FeatureFlagName.AI_WORKFLOW_ENABLED) && isEnabled(FeatureFlagName.AI_PLATFORM_ENABLED);
            case MONITORING -> isEnabled(FeatureFlagName.AI_MONITORING_ENABLED) && isEnabled(FeatureFlagName.AI_PLATFORM_ENABLED);
        };
    }

    public boolean isProviderEnabled(String providerName) {
        return switch (providerName.toUpperCase()) {
            case "GEMINI" -> isEnabled(FeatureFlagName.AI_PROVIDER_GEMINI);
            case "OPENAI" -> isEnabled(FeatureFlagName.AI_PROVIDER_OPENAI);
            case "CLAUDE" -> isEnabled(FeatureFlagName.AI_PROVIDER_CLAUDE);
            case "AZURE_OPENAI" -> isEnabled(FeatureFlagName.AI_PROVIDER_AZURE_OPENAI);
            case "BEDROCK" -> isEnabled(FeatureFlagName.AI_PROVIDER_BEDROCK);
            case "OLLAMA" -> isEnabled(FeatureFlagName.AI_PROVIDER_OLLAMA);
            case "MISTRAL" -> isEnabled(FeatureFlagName.AI_PROVIDER_MISTRAL);
            case "LOCAL_LLM", "LOCAL" -> isEnabled(FeatureFlagName.AI_PROVIDER_LOCAL_LLM);
            case "MOCK" -> true;
            default -> false;
        };
    }

    public void setFlag(FeatureFlagName flag, boolean enabled) {
        flags.put(flag, enabled);
    }

    public Map<FeatureFlagName, Boolean> getAllFlags() {
        return Map.copyOf(flags);
    }
}
