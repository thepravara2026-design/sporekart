package com.sporekart.ai.providers.health;

public interface HealthMetrics {
    void recordCheck(String providerId, boolean healthy);
    void recordFailure(String providerId);
    void recordRecovery(String providerId);
    void recordTimeout(String providerId);
    int getCheckCount(String providerId);
    int getFailureCount(String providerId);
    int getRecoveryCount(String providerId);
    double getHealthScore(String providerId);
}
