package com.sporekart.ai.decision.api;

import com.sporekart.ai.decision.domain.*;
import java.util.List;
import java.util.Optional;

public interface DecisionConfigurationService {
    Optional<String> getConfig(String key);
    void setConfig(String key, String value, String description);
    List<DecisionConfig> getAllConfigs();
    void reloadConfig();
    boolean isFeatureEnabled(String feature);
}
