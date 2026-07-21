package com.sporekart.ai.configuration.domain;

import java.time.Duration;

public record SemanticConfiguration(
    boolean enabled,
    String defaultSearchType,
    int defaultLimit,
    int rrfConstant,
    double semanticWeight,
    double keywordWeight,
    boolean hybridSearchEnabled,
    boolean crossEncoderEnabled,
    boolean multiVectorEnabled,
    boolean contextualSearchEnabled,
    boolean cachingEnabled,
    Duration searchCacheTtl,
    Duration embeddingCacheTtl,
    int rateLimitPerMinute,
    boolean auditEnabled
) {
    public static SemanticConfiguration defaults() {
        return new SemanticConfiguration(
            true, "HYBRID", 10, 60,
            0.7, 0.3,
            true, false, false, true,
            true, Duration.ofMinutes(5), Duration.ofHours(24),
            100, true
        );
    }
}
