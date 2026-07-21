package com.sporekart.ai.providers.circuit.breaker;

import com.sporekart.ai.providers.circuit.CircuitState;

public interface CircuitBreaker {
    CircuitState getState();
    boolean allowRequest();
    void onSuccess();
    void onFailure();
    void reset();
    int getFailureCount();
    int getSuccessCount();
}
