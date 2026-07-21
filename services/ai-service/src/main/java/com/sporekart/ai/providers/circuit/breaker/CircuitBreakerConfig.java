package com.sporekart.ai.providers.circuit.breaker;

import java.time.Duration;

public record CircuitBreakerConfig(
    int failureThreshold,
    int successThreshold,
    Duration timeout,
    Duration halfOpenMaxDuration,
    int halfOpenMaxCalls,
    boolean automaticReset,
    Duration cooldownPeriod
) {
    public static CircuitBreakerConfig defaults() {
        return new CircuitBreakerConfig(5, 3, Duration.ofSeconds(30),
            Duration.ofSeconds(10), 3, true, Duration.ofSeconds(60));
    }
}
