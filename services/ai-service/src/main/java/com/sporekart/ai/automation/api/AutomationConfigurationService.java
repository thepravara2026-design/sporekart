package com.sporekart.ai.automation.api;

import java.util.Map;

public interface AutomationConfigurationService {
    Object getConfig(String key);
    void setConfig(String key, Object value);
    Map<String, Object> getAllConfigs();
    void reloadConfig();
    boolean isFeatureEnabled(String feature);
}
