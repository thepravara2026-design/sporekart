package com.sporekart.ai.core.api;

public interface AIRateLimiter {
    boolean tryAcquire(String module, String userId);
    int getRemainingTokens(String module, String userId);
    long getResetTimeSeconds(String module, String userId);
}
