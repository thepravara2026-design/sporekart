package com.sporekart.ai.providers.registry.cache;

import java.time.Instant;

public record CacheEntry<T>(
    T value,
    Instant created,
    Instant expires,
    int accessCount
) {
    public boolean isExpired() {
        return Instant.now().isAfter(expires);
    }

    public CacheEntry<T> recordAccess() {
        return new CacheEntry<>(value, created, expires, accessCount + 1);
    }
}
