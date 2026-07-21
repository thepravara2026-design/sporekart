package com.sporekart.ai.providers.health;

import java.time.Duration;

public interface HealthPolicy {
    Duration getCheckInterval(String providerId);
    int getFailureThreshold(String providerId);
    int getSuccessThreshold(String providerId);
    boolean isAutoRecoveryEnabled(String providerId);
    Duration getCooldownPeriod(String providerId);
}
