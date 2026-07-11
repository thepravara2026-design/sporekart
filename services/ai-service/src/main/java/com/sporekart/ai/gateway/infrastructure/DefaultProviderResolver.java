package com.sporekart.ai.gateway.infrastructure;

import com.sporekart.ai.core.api.ProviderResolver;
import com.sporekart.ai.core.application.featureflag.FeatureFlagName;
import com.sporekart.ai.core.application.featureflag.FeatureFlagService;
import com.sporekart.ai.core.domain.AiProviderType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DefaultProviderResolver implements ProviderResolver {
    private final FeatureFlagService featureFlagService;

    public DefaultProviderResolver(FeatureFlagService featureFlagService) {
        this.featureFlagService = featureFlagService;
    }

    @Override
    public Optional<AiProviderType> resolve(String module, String preferredProvider) {
        if (preferredProvider != null) {
            try {
                AiProviderType type = AiProviderType.valueOf(preferredProvider.toUpperCase());
                if (featureFlagService.isProviderEnabled(type.name())) {
                    return Optional.of(type);
                }
            } catch (IllegalArgumentException e) {
            }
        }
        return getAvailableProviders().stream().findFirst();
    }

    @Override
    public List<AiProviderType> getAvailableProviders() {
        List<AiProviderType> available = new ArrayList<>();
        if (featureFlagService.isEnabled(FeatureFlagName.AI_PROVIDER_GEMINI)) {
            available.add(AiProviderType.GEMINI);
        }
        if (featureFlagService.isEnabled(FeatureFlagName.AI_PROVIDER_OPENAI)) {
            available.add(AiProviderType.OPENAI);
        }
        if (featureFlagService.isEnabled(FeatureFlagName.AI_PROVIDER_CLAUDE)) {
            available.add(AiProviderType.CLAUDE);
        }
        available.add(AiProviderType.MOCK);
        return available;
    }

    @Override
    public boolean isProviderAvailable(AiProviderType providerType) {
        return featureFlagService.isProviderEnabled(providerType.name());
    }
}
