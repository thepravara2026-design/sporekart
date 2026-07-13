package com.sporekart.ai.compliance.api;

import java.util.Map;

public interface ComplianceConfigurationService {
    Object getConfig(String key);
    void setConfig(String key, Object value);
    Map<String, Object> getAllConfigs();
    void reloadConfig();
    boolean isFeatureEnabled(String feature);
}
