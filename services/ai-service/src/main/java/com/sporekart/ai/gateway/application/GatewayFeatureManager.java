package com.sporekart.ai.gateway.application;

import com.sporekart.ai.core.application.exception.FeatureDisabledException;
import com.sporekart.ai.core.application.featureflag.FeatureFlagName;
import com.sporekart.ai.core.application.featureflag.FeatureFlagService;
import com.sporekart.ai.core.domain.AiModule;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GatewayFeatureManager {
    private final FeatureFlagService featureFlagService;

    public GatewayFeatureManager(FeatureFlagService featureFlagService) {
        this.featureFlagService = featureFlagService;
    }

    public void requirePlatformEnabled() {
        if (!featureFlagService.isEnabled(FeatureFlagName.AI_PLATFORM_ENABLED)) {
            throw new FeatureDisabledException("AI Platform");
        }
    }

    public void requireGatewayEnabled() {
        requirePlatformEnabled();
        if (!featureFlagService.isEnabled(FeatureFlagName.AI_GATEWAY_ENABLED)) {
            throw new FeatureDisabledException("AI Gateway");
        }
    }

    public boolean isModuleAvailable(AiModule module) {
        return featureFlagService.isModuleEnabled(module);
    }

    public boolean isFeatureEnabled(FeatureFlagName flag) {
        return featureFlagService.isEnabled(flag);
    }

    public Map<FeatureFlagName, Boolean> getAllFlags() {
        return featureFlagService.getAllFlags();
    }
}
