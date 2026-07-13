package com.sporekart.ai.analytics.api;

import java.util.Map;

public interface AnalyticsConfigurationService {
    Object getConfig(String key);
    void setConfig(String key, Object value);
    Map<String, Object> getAllConfigs();
    void reloadConfig();
    boolean isFeatureEnabled(String feature);
}
