package com.sporekart.ai.providers.recovery;

import java.util.List;
import java.util.Optional;

public interface RecoveryManager {
    boolean attemptRecovery(String providerId);
    RecoveryStrategy getStrategy(String providerId);
    void setStrategy(String providerId, RecoveryStrategy strategy);
    List<RecoveryAttempt> getRecoveryHistory(String providerId);
    boolean isRecovering(String providerId);
}

enum RecoveryStrategy {
    AUTOMATIC,
    MANUAL,
    SCHEDULED,
    GRACEFUL,
    EMERGENCY
}

record RecoveryAttempt(String providerId, RecoveryStrategy strategy, boolean success, String error, long timestamp) {}
