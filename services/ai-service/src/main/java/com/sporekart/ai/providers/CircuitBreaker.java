package com.sporekart.ai.providers;

import java.util.List;

public interface CircuitBreaker {
    boolean isOpen(String providerId);
    boolean isHalfOpen(String providerId);
    boolean isClosed(String providerId);
    void recordSuccess(String providerId);
    void recordFailure(String providerId);
    int getFailureCount(String providerId);
    void reset(String providerId);
    List<String> getOpenProviders();
}
