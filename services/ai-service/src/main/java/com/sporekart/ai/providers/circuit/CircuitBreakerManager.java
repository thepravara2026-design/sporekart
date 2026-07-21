package com.sporekart.ai.providers.circuit;

import java.util.List;

public interface CircuitBreakerManager {
    CircuitState getState(String providerId);
    boolean isOpen(String providerId);
    boolean isClosed(String providerId);
    boolean isHalfOpen(String providerId);
    void recordSuccess(String providerId);
    void recordFailure(String providerId);
    void reset(String providerId);
    void manualOpen(String providerId, String reason);
    void manualClose(String providerId);
    List<String> getOpenProviders();
    List<String> getClosedProviders();
    List<String> getHalfOpenProviders();
}
