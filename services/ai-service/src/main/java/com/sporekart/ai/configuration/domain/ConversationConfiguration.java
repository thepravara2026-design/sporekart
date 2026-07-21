package com.sporekart.ai.configuration.domain;

import java.time.Duration;

public record ConversationConfiguration(
    boolean enabled,
    boolean cachingEnabled,
    Duration sessionCacheTtl,
    Duration messageCacheTtl,
    Duration contextCacheTtl,
    int sessionTimeoutMinutes,
    int maxContextEntries,
    int maxMessageLength,
    boolean streamingEnabled,
    boolean memoryEnabled,
    Duration shortTermMemoryTtl,
    boolean auditEnabled,
    boolean inputSanitizationEnabled,
    boolean rateLimiterEnabled,
    int rateLimitPerMinute
) {
    public static ConversationConfiguration defaults() {
        return new ConversationConfiguration(
            true, true,
            Duration.ofMinutes(30), Duration.ofMinutes(15), Duration.ofMinutes(10),
            30, 50, 10000,
            true, true, Duration.ofHours(24),
            true, true, true, 60
        );
    }
}
