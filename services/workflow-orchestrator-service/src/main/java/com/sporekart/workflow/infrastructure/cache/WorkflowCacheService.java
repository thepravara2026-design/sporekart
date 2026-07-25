package com.sporekart.workflow.infrastructure.cache;

import com.sporekart.workflow.domain.model.WorkflowCache;
import com.sporekart.workflow.domain.repository.WorkflowRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WorkflowCacheService {
    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private final int ttlSeconds;
    private final int maxSize;

    public WorkflowCacheService(int ttlSeconds, int maxSize) {
        this.ttlSeconds = ttlSeconds;
        this.maxSize = maxSize;
    }

    public void put(String key, Object value) {
        if (cache.size() >= maxSize) {
            var oldest = cache.entrySet().stream()
                .min(Comparator.comparingLong(e -> e.getValue().timestamp))
                .map(Map.Entry::getKey);
            oldest.ifPresent(cache::remove);
        }
        cache.put(key, new CacheEntry(value, System.currentTimeMillis()));
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(String key) {
        var entry = cache.get(key);
        if (entry == null) return Optional.empty();

        if (System.currentTimeMillis() - entry.timestamp > ttlSeconds * 1000L) {
            cache.remove(key);
            return Optional.empty();
        }

        return Optional.ofNullable((T) entry.value);
    }

    public void remove(String key) {
        cache.remove(key);
    }

    public void clear() {
        cache.clear();
    }

    public Map<String, Object> getCacheInfo() {
        var now = System.currentTimeMillis();
        var validCount = cache.values().stream()
            .filter(e -> now - e.timestamp <= ttlSeconds * 1000L)
            .count();

        return Map.of(
            "size", cache.size(),
            "validEntries", validCount,
            "expiredEntries", cache.size() - validCount,
            "maxSize", maxSize,
            "ttlSeconds", ttlSeconds,
            "hitRate", 0.0
        );
    }

    private record CacheEntry(Object value, long timestamp) {}
}
