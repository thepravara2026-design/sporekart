package com.sporekart.ai.policy.api;

import com.sporekart.ai.policy.domain.*;
import java.util.List;
import java.util.Optional;

public interface PolicyConfigurationService {
    Optional<String> getConfig(String key);
    void setConfig(String key, String value, String description);
    List<PolicyConfiguration> getAllConfigs();
    void reloadConfig();
    boolean isFeatureEnabled(String feature);
}
