package com.sporekart.ai.providers.registry.cache;

import java.util.Optional;

public interface RegistryCache {
    <T> void put(String key, T value, CacheEntry<T> entry);
    <T> Optional<T> get(String key);
    void invalidate(String key);
    void invalidateAll();
    void refresh(String key);
    boolean contains(String key);
    int size();
}
