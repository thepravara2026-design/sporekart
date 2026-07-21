package com.sporekart.ai.providers.recovery;

public interface RecoveryMetrics {
    void recordRecoveryAttempt(String providerId, boolean success);
    void recordRecoverySuccess(String providerId);
    void recordRecoveryFailure(String providerId);
    int getTotalRecoveryAttempts(String providerId);
    int getSuccessfulRecoveries(String providerId);
    int getFailedRecoveries(String providerId);
    double getRecoverySuccessRate(String providerId);
    long getAverageRecoveryTime(String providerId);
}
