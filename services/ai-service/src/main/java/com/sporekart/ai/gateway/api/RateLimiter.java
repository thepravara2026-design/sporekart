package com.sporekart.ai.gateway.api;

public interface RateLimiter {
    boolean tryAcquire(String module);
    int getRemainingTokens(String module);
    long getResetTimeSeconds(String module);
}
