package com.sporekart.ai.providers.circuit.policy;

import com.sporekart.ai.providers.circuit.CircuitState;
import com.sporekart.ai.providers.circuit.breaker.CircuitBreakerConfig;

public interface CircuitBreakerPolicy {
    boolean shouldOpen(CircuitState current, int failureCount, int threshold);
    boolean shouldClose(CircuitState current, int successCount, int threshold);
    boolean shouldAttemptReset(CircuitState current, CircuitBreakerConfig config);
    boolean isCooldownComplete(CircuitState current, long elapsed, long cooldown);
}
