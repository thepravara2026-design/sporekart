package com.sporekart.ai.providers.heartbeat;

import java.time.Instant;
import java.util.List;

public interface HeartbeatHistory {
    void record(String providerId, Instant timestamp, boolean success);
    List<HeartbeatRecord> getHistory(String providerId);
    List<HeartbeatRecord> getHistoryByTimeRange(String providerId, Instant from, Instant to);
    int getMissedCount(String providerId);
    double getSuccessRate(String providerId);
}

record HeartbeatRecord(String providerId, Instant timestamp, boolean success, long latency) {}
