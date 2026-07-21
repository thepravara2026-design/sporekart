package com.sporekart.ai.providers.recovery;

import java.time.Instant;
import java.util.Map;

public record RecoveryContext(
    String providerId,
    String failureReason,
    RecoveryStrategy strategy,
    int attemptNumber,
    int maxRetries,
    Instant startedAt,
    Map<String, Object> metadata
) {
    public boolean isRetryAvailable() {
        return attemptNumber < maxRetries;
    }
}
