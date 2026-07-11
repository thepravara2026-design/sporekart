package com.sporekart.ai.core.api;

import java.time.Duration;

public interface TimeoutStrategy {
    Duration getTimeout(String module);
    Duration getTimeout(String module, String provider);
    boolean isExpired(long startTimeNanos, Duration timeout);
}
