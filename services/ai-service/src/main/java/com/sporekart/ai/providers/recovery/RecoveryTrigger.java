package com.sporekart.ai.providers.recovery;

public interface RecoveryTrigger {
    void onFailure(String providerId, String error);
    void onHealthDegraded(String providerId);
    void onCircuitOpen(String providerId);
    void onHeartbeatLost(String providerId);
    void onUnavailability(String providerId);
}
