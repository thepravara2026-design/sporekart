package com.sporekart.ai.providers.heartbeat;

import java.time.Duration;

public record HeartbeatTimeout(
    String providerId,
    Duration timeout,
    Duration expiryThreshold,
    int missedThreshold
) {
    public boolean isExpired(long ageMillis) {
        return ageMillis > expiryThreshold.toMillis();
    }
}
