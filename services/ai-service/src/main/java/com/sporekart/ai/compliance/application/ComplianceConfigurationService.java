package com.sporekart.ai.compliance.application;

import java.util.Map;
import java.util.Optional;

public interface ComplianceConfigurationService {
    Optional<String> getConfig(String key);
    void setConfig(String key, String value);
    Map<String, String> getAllConfigs();
    void reloadConfig();
    boolean isFeatureEnabled(String feature);
}
