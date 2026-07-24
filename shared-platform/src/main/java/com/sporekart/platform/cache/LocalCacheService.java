package com.sporekart.platform.cache;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LocalCacheService implements CacheService {

    private static final Logger log = LoggerFactory.getLogger(LocalCacheService.class);
    private final ConcurrentMap<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private final ScheduledExecutorService cleanup = Executors.newSingleThreadScheduledExecutor();

    public LocalCacheService() {
        cleanup.scheduleAtFixedRate(this::evictExpired, 1, 1, TimeUnit.MINUTES);
    }

    @Override
    public <T> Optional<T> get(String key, Class<T> type) {
        CacheEntry entry = cache.get(key);
        if (entry == null) {
            return Optional.empty();
        }
        if (entry.isExpired()) {
            cache.remove(key);
            return Optional.empty();
        }
        entry.recordAccess();
        return Optional.of(type.cast(entry.value));
    }

    @Override
    public <T> void put(String key, T value) {
        put(key, value, 300);
    }

    @Override
    public <T> void put(String key, T value, long ttlSeconds) {
        cache.put(key, new CacheEntry(value, ttlSeconds));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> computeIfAbsent(String key, Class<T> type, Callable<T> loader) {
        CacheEntry entry = cache.get(key);
        if (entry != null && !entry.isExpired()) {
            entry.recordAccess();
            return Optional.of((T) entry.value);
        }
        try {
            T value = loader.call();
            if (value != null) {
                put(key, value);
            }
            return Optional.ofNullable(value);
        } catch (Exception e) {
            log.warn("Cache loader failed for key: {}", key, e);
            return Optional.empty();
        }
    }

    @Override
    public void evict(String key) {
        cache.remove(key);
    }

    @Override
    public void evictByPrefix(String prefix) {
        cache.keySet().removeIf(k -> k.startsWith(prefix));
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public boolean containsKey(String key) {
        CacheEntry entry = cache.get(key);
        return entry != null && !entry.isExpired();
    }

    @Override
    public long size() {
        evictExpired();
        return cache.size();
    }

    private void evictExpired() {
        cache.values().removeIf(CacheEntry::isExpired);
    }

    private static class CacheEntry {
        final Object value;
        final long createdAt;
        final long ttlMillis;
        long lastAccess;

        CacheEntry(Object value, long ttlSeconds) {
            this.value = value;
            this.createdAt = System.currentTimeMillis();
            this.ttlMillis = ttlSeconds * 1000;
            this.lastAccess = this.createdAt;
        }

        boolean isExpired() {
            return System.currentTimeMillis() - createdAt > ttlMillis;
        }

        void recordAccess() {
            this.lastAccess = System.currentTimeMillis();
        }
    }
}
