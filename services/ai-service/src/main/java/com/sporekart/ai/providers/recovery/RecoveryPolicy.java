package com.sporekart.ai.providers.recovery;

import java.time.Duration;

public interface RecoveryPolicy {
    int getMaxRetries(String providerId);
    Duration getRetryInterval(String providerId);
    Duration getBackoffMultiplier(String providerId);
    boolean isAutoRecoveryEnabled(String providerId);
    boolean isManualRecoveryAllowed(String providerId);
    RecoveryStrategy getDefaultStrategy();
}
