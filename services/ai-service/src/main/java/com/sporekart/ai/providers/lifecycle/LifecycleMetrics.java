package com.sporekart.ai.providers.lifecycle;

public interface LifecycleMetrics {
    void recordTransition();
    void recordActivation();
    void recordDeactivation();
    void recordFailure();
    void recordRecovery();
    int getTransitionCount();
    int getActivationCount();
    int getDeactivationCount();
    int getFailureCount();
    int getRecoveryCount();
}
