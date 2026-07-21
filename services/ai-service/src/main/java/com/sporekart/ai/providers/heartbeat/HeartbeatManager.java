package com.sporekart.ai.providers.heartbeat;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface HeartbeatManager {
    void recordHeartbeat(String providerId);
    boolean isAlive(String providerId);
    Optional<Instant> getLastHeartbeat(String providerId);
    long getHeartbeatAge(String providerId);
    boolean isHeartbeatExpired(String providerId);
    List<String> getAliveProviders();
    List<String> getDeadProviders();
}
