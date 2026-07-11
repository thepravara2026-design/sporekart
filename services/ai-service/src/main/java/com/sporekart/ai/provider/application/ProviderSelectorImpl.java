package com.sporekart.ai.provider.application;

import com.sporekart.ai.core.api.ProviderSelector;
import com.sporekart.ai.core.application.featureflag.FeatureFlagName;
import com.sporekart.ai.core.application.featureflag.FeatureFlagService;
import com.sporekart.ai.core.domain.AiProviderType;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProviderSelectorImpl implements ProviderSelector {
    private final FeatureFlagService featureFlagService;
    private final Map<String, List<AiProviderType>> modulePreferences = new LinkedHashMap<>();

    public ProviderSelectorImpl(FeatureFlagService featureFlagService) {
        this.featureFlagService = featureFlagService;
        initializeModulePreferences();
    }

    private void initializeModulePreferences() {
        modulePreferences.put("chat", List.of(AiProviderType.GEMINI, AiProviderType.OPENAI, AiProviderType.CLAUDE));
        modulePreferences.put("search", List.of(AiProviderType.OPENAI, AiProviderType.CLAUDE));
        modulePreferences.put("content", List.of(AiProviderType.CLAUDE, AiProviderType.GEMINI));
        modulePreferences.put("support", List.of(AiProviderType.GEMINI, AiProviderType.OPENAI));
        modulePreferences.put("workflow", List.of(AiProviderType.GEMINI, AiProviderType.OPENAI, AiProviderType.CLAUDE));
    }

    @Override
    public AiProviderType select(String module, String preferredProvider) {
        if (preferredProvider != null) {
            try {
                AiProviderType preferred = AiProviderType.valueOf(preferredProvider.toUpperCase());
                if (isProviderFeatureEnabled(preferred)) {
                    return preferred;
                }
            } catch (IllegalArgumentException e) {
            }
        }

        List<AiProviderType> preferences = modulePreferences.getOrDefault(module, List.of());
        for (AiProviderType type : preferences) {
            if (isProviderFeatureEnabled(type)) {
                return type;
            }
        }

        List<AiProviderType> available = getAvailableProviders();
        if (!available.isEmpty()) {
            return available.get(0);
        }

        return AiProviderType.MOCK;
    }

    @Override
    public Optional<AiProviderType> selectFallback(String module, AiProviderType failedProvider) {
        return getAvailableProviders().stream()
                .filter(p -> p != failedProvider)
                .findFirst();
    }

    public List<AiProviderType> getAvailableProviders() {
        return Arrays.stream(AiProviderType.values())
                .filter(this::isProviderFeatureEnabled)
                .collect(Collectors.toList());
    }

    private boolean isProviderFeatureEnabled(AiProviderType type) {
        return switch (type) {
            case GEMINI -> featureFlagService.isEnabled(FeatureFlagName.AI_PROVIDER_GEMINI);
            case OPENAI -> featureFlagService.isEnabled(FeatureFlagName.AI_PROVIDER_OPENAI);
            case CLAUDE -> featureFlagService.isEnabled(FeatureFlagName.AI_PROVIDER_CLAUDE);
            case AZURE_OPENAI -> featureFlagService.isEnabled(FeatureFlagName.AI_PROVIDER_AZURE_OPENAI);
            case LOCAL -> featureFlagService.isEnabled(FeatureFlagName.AI_PROVIDER_LOCAL_LLM);
            case MOCK -> true;
        };
    }
}
