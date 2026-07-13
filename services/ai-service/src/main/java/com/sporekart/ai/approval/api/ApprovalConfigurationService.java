package com.sporekart.ai.approval.api;

import com.sporekart.ai.approval.domain.*;
import java.util.List;
import java.util.Optional;

public interface ApprovalConfigurationService {
    Optional<String> getConfig(String key);
    void setConfig(String key, String value, String description);
    List<DomainConfig> getAllConfigs();
    void reloadConfig();
    boolean isFeatureEnabled(String feature);
}
