package com.sporekart.ai.gateway.infrastructure;

import com.sporekart.ai.core.api.TimeoutStrategy;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultTimeoutStrategy implements TimeoutStrategy {
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);
    private final Map<String, Duration> moduleTimeouts = new ConcurrentHashMap<>();

    public DefaultTimeoutStrategy() {
        moduleTimeouts.put("chat", Duration.ofSeconds(60));
        moduleTimeouts.put("search", Duration.ofSeconds(15));
        moduleTimeouts.put("content", Duration.ofSeconds(45));
    }

    @Override
    public Duration getTimeout(String module) {
        return moduleTimeouts.getOrDefault(module, DEFAULT_TIMEOUT);
    }

    @Override
    public Duration getTimeout(String module, String provider) {
        return getTimeout(module);
    }

    @Override
    public boolean isExpired(long startTimeNanos, Duration timeout) {
        return Duration.ofNanos(System.nanoTime() - startTimeNanos).compareTo(timeout) > 0;
    }
}
