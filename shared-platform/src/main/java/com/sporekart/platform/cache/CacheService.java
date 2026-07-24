package com.sporekart.platform.cache;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Function;

public interface CacheService {

    <T> Optional<T> get(String key, Class<T> type);

    <T> void put(String key, T value);

    <T> void put(String key, T value, long ttlSeconds);

    <T> Optional<T> computeIfAbsent(String key, Class<T> type, Callable<T> loader);

    void evict(String key);

    void evictByPrefix(String prefix);

    void clear();

    boolean containsKey(String key);

    long size();

    default <T> T getOrCompute(String key, Class<T> type, Callable<T> loader) {
        return computeIfAbsent(key, type, loader).orElse(null);
    }
}
