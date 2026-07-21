package com.sporekart.ai.providers.health;

import java.time.Duration;
import java.util.Map;

public record HealthConfiguration(
    boolean enabled,
    Duration checkInterval,
    Duration timeout,
    int failureThreshold,
    int successThreshold,
    boolean autoRecovery,
    Duration cooldownPeriod,
    Map<String, String> providerOverrides
) {
    public static HealthConfiguration defaults() {
        return new HealthConfiguration(
            true, Duration.ofSeconds(30), Duration.ofSeconds(5),
            3, 2, true, Duration.ofSeconds(60), Map.of()
        );
    }
}
