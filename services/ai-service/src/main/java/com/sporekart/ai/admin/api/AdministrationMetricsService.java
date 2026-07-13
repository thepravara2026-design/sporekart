package com.sporekart.ai.admin.api;

import java.util.Map;

public interface AdministrationMetricsService {
    long getTotalOperations();
    long getConfigurationChanges();
    long getRollbackCount();
    long getFeatureFlagChanges();
    Map<String, Long> getOperationDistribution();
    Map<String, Object> getStatistics();
}
