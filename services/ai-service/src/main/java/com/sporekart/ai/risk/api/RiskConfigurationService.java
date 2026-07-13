package com.sporekart.ai.risk.api;

import com.sporekart.ai.risk.domain.*;
import java.util.Map;

public interface RiskConfigurationService {
    Object getConfig(String key);
    void setConfig(String key, Object value);
    Map<String, Object> getAllConfigs();
    void reloadConfig();
    boolean isFeatureEnabled(String feature);
    void setThreshold(RiskLevel level, double minScore, double maxScore, String action);
    RiskThreshold getThreshold(RiskLevel level);
}
