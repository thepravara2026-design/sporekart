package com.sporekart.ai.providers.heartbeat;

public interface HeartbeatMetrics {
    void recordHeartbeat(String providerId, long latency);
    void recordMissedHeartbeat(String providerId);
    long getTotalHeartbeats(String providerId);
    long getMissedHeartbeats(String providerId);
    double getHeartbeatSuccessRate(String providerId);
    long getAverageLatency(String providerId);
}
