package com.sporekart.platform.cache;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocalCacheServiceTest {

    private LocalCacheService cache;

    @BeforeEach
    void setUp() {
        cache = new LocalCacheService();
    }

    @Test
    void shouldStoreAndRetrieveValue() {
        cache.put("test-key", "test-value");
        Optional<String> result = cache.get("test-key", String.class);
        assertTrue(result.isPresent());
        assertEquals("test-value", result.get());
    }

    @Test
    void shouldReturnEmptyForMissingKey() {
        Optional<String> result = cache.get("missing", String.class);
        assertFalse(result.isPresent());
    }

    @Test
    void shouldEvictByKey() {
        cache.put("evict-me", "value");
        assertTrue(cache.containsKey("evict-me"));
        cache.evict("evict-me");
        assertFalse(cache.containsKey("evict-me"));
    }

    @Test
    void shouldComputeIfAbsent() {
        AtomicInteger counter = new AtomicInteger();
        String result1 = cache.getOrCompute("compute-key", String.class, () -> "computed-" + counter.incrementAndGet());
        assertEquals("computed-1", result1);
        String result2 = cache.getOrCompute("compute-key", String.class, () -> "computed-" + counter.incrementAndGet());
        assertEquals("computed-1", result2);
    }

    @Test
    void shouldEvictByPrefix() {
        cache.put("user:1:profile", "profile1");
        cache.put("user:2:profile", "profile2");
        cache.put("product:1", "product1");
        assertEquals(3, cache.size());
        cache.evictByPrefix("user:");
        assertEquals(1, cache.size());
    }

    @Test
    void shouldClearAll() {
        cache.put("a", 1);
        cache.put("b", 2);
        cache.put("c", 3);
        assertEquals(3, cache.size());
        cache.clear();
        assertEquals(0, cache.size());
    }

    @Test
    void shouldHandleNullLoaderResult() {
        Optional<String> result = cache.computeIfAbsent("null-key", String.class, () -> null);
        assertFalse(result.isPresent());
    }

    @Test
    void shouldRespectTTL() throws InterruptedException {
        cache.put("ttl-key", "ttl-value", 1);
        assertTrue(cache.containsKey("ttl-key"));
        Thread.sleep(1100);
        assertFalse(cache.containsKey("ttl-key"));
    }

    @Test
    void shouldStoreMultipleTypes() {
        cache.put("string-key", "hello");
        cache.put("int-key", 42);
        cache.put("double-key", 3.14);
        assertEquals("hello", cache.get("string-key", String.class).get());
        assertEquals(42, cache.get("int-key", Integer.class).get());
        assertEquals(3.14, cache.get("double-key", Double.class).get());
    }
}
