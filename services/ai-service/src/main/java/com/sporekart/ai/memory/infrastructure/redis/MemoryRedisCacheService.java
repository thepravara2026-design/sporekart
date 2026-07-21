package com.sporekart.ai.memory.infrastructure.redis;

import org.springframework.stereotype.Component;

@Component
public class MemoryRedisCacheService {

    public void cacheMemoryEntry(String key, String value) {
    }

    public String getCachedMemoryEntry(String key) {
        return null;
    }

    public void evictMemoryEntry(String key) {
    }

    public void evictByPattern(String pattern) {
    }

    public void clearCache() {
    }
}
