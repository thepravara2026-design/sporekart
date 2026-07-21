package com.sporekart.ai.providers.recovery;

public interface RecoveryAudit {
    void recordRecoveryEvent(String providerId, String eventType, String details);
    void recordStrategyChange(String providerId, RecoveryStrategy oldStrategy, RecoveryStrategy newStrategy);
    void clearAudit(String providerId);
}
