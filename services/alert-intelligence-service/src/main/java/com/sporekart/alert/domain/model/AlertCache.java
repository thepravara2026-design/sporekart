package com.sporekart.alert.domain.model;

import java.time.Instant;
import java.util.UUID;

public class AlertCache {
    private final String id;
    private final String cacheKey;
    private final String cacheType;
    private final Object cachedData;
    private final Instant createdAt;
    private final Instant expiresAt;
    private final int hitCount;

    public AlertCache(String id, String cacheKey, String cacheType, Object cachedData,
                      Instant createdAt, Instant expiresAt, int hitCount) {
        this.id = id; this.cacheKey = cacheKey; this.cacheType = cacheType;
        this.cachedData = cachedData; this.createdAt = createdAt;
        this.expiresAt = expiresAt; this.hitCount = hitCount;
    }

    public static AlertCache create(String cacheKey, String cacheType, Object data, int ttlSeconds) {
        return new AlertCache(UUID.randomUUID().toString(), cacheKey, cacheType, data,
                Instant.now(), Instant.now().plusSeconds(ttlSeconds), 0);
    }

    public boolean isExpired() { return Instant.now().isAfter(expiresAt); }

    public AlertCache withHit() { return new AlertCache(id, cacheKey, cacheType, cachedData,
            createdAt, expiresAt, hitCount + 1); }

    public String id() { return id; }
    public String cacheKey() { return cacheKey; }
    public String cacheType() { return cacheType; }
    public Object cachedData() { return cachedData; }
    public Instant createdAt() { return createdAt; }
    public Instant expiresAt() { return expiresAt; }
    public int hitCount() { return hitCount; }
}
