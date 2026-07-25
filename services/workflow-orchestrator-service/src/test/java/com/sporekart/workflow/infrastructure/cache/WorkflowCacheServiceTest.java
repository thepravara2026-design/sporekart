package com.sporekart.workflow.infrastructure.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorkflowCacheServiceTest {
    private WorkflowCacheService cache;

    @BeforeEach
    void setUp() {
        cache = new WorkflowCacheService(300, 100);
    }

    @Test
    void shouldStoreAndRetrieve() {
        cache.put("key1", "value1");
        var result = cache.<String>get("key1");
        assertTrue(result.isPresent());
        assertEquals("value1", result.get());
    }

    @Test
    void shouldReturnEmptyForMissingKey() {
        var result = cache.<String>get("missing");
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldRemoveKey() {
        cache.put("key1", "value1");
        cache.remove("key1");
        assertTrue(cache.<String>get("key1").isEmpty());
    }

    @Test
    void shouldClearCache() {
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.clear();
        assertTrue(cache.<String>get("key1").isEmpty());
        assertTrue(cache.<String>get("key2").isEmpty());
    }

    @Test
    void shouldReturnCacheInfo() {
        cache.put("key1", "value1");
        var info = cache.getCacheInfo();
        assertEquals(1, info.get("size"));
        assertEquals(100, info.get("maxSize"));
        assertEquals(300, info.get("ttlSeconds"));
    }

    @Test
    void shouldEvictOldestWhenFull() {
        var smallCache = new WorkflowCacheService(300, 2);
        smallCache.put("key1", "value1");
        smallCache.put("key2", "value2");
        smallCache.put("key3", "value3");
        assertTrue(smallCache.<String>get("key1").isEmpty());
        assertTrue(smallCache.<String>get("key2").isPresent());
        assertTrue(smallCache.<String>get("key3").isPresent());
    }
}
