package com.sporekart.ai.admin.api;

import com.sporekart.ai.admin.domain.FeatureFlag;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface FeatureFlagService {
    FeatureFlag getFeatureFlag(String key);
    FeatureFlag setFeatureFlag(String key, boolean enabled, String environment, String module, Map<String, Object> metadata, UUID updatedBy);
    List<FeatureFlag> getAllFeatureFlags();
    List<FeatureFlag> getFeatureFlagsByModule(String module);
    List<FeatureFlag> getFeatureFlagsByEnvironment(String environment);
    boolean isFeatureEnabled(String key);
}
