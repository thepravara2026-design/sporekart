package com.sporekart.report.domain.model;

import java.time.Instant;
import java.util.UUID;

public record ReportCache(
    String id,
    String cacheKey,
    String cacheType,
    Object cachedData,
    int ttlSeconds,
    long hitCount,
    Instant createdAt,
    Instant expiresAt
) {
    public static ReportCache create(String cacheType, String cacheKey, Object data, int ttlSeconds) {
        Instant now = Instant.now();
        return new ReportCache(
            UUID.randomUUID().toString(),
            cacheKey, cacheType, data, ttlSeconds, 0L, now, now.plusSeconds(ttlSeconds)
        );
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiresAt);
    }

    public ReportCache withHit() {
        return new ReportCache(id, cacheKey, cacheType, cachedData, ttlSeconds, hitCount + 1, createdAt, expiresAt);
    }
}
