package com.sporekart.ai.providers.circuit.recovery;

import com.sporekart.ai.providers.circuit.CircuitState;

public interface CircuitRecovery {
    boolean attemptRecovery(String providerId);
    CircuitState recover(String providerId);
    boolean isRecoveryPossible(String providerId);
    int getRecoveryAttempts(String providerId);
    void resetRecoveryAttempts(String providerId);
}
