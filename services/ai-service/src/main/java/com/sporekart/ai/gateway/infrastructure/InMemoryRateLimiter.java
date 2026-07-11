package com.sporekart.ai.gateway.infrastructure;

import com.sporekart.ai.gateway.api.RateLimiter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryRateLimiter implements RateLimiter {
    private static final int DEFAULT_LIMIT = 100;
    private final Map<String, AtomicInteger> counters = new ConcurrentHashMap<>();
    private final int limit;

    public InMemoryRateLimiter() {
        this(DEFAULT_LIMIT);
    }

    public InMemoryRateLimiter(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean tryAcquire(String module) {
        return counters.computeIfAbsent(module, k -> new AtomicInteger(0)).incrementAndGet() <= limit;
    }

    @Override
    public int getRemainingTokens(String module) {
        return Math.max(0, limit - counters.getOrDefault(module, new AtomicInteger(0)).get());
    }

    @Override
    public long getResetTimeSeconds(String module) {
        return 60;
    }

    public void reset(String module) {
        counters.remove(module);
    }
}
