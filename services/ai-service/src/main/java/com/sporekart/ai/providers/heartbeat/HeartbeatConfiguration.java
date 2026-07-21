package com.sporekart.ai.providers.heartbeat;

import java.time.Duration;
import java.util.Map;

public record HeartbeatConfiguration(
    boolean enabled,
    Duration interval,
    Duration timeout,
    Duration expiryThreshold,
    Map<String, Duration> providerOverrides
) {
    public static HeartbeatConfiguration defaults() {
        return new HeartbeatConfiguration(
            true, Duration.ofSeconds(10), Duration.ofSeconds(5),
            Duration.ofSeconds(30), Map.of()
        );
    }
}
