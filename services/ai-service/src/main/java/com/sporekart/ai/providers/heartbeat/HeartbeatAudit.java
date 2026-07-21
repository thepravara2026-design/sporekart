package com.sporekart.ai.providers.heartbeat;

public interface HeartbeatAudit {
    void recordHeartbeatEvent(String providerId, String eventType);
    void recordTimeout(String providerId, long timeout);
    void recordRecovery(String providerId);
    void clearAudit(String providerId);
}
