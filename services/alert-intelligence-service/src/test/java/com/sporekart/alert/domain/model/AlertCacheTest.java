package com.sporekart.alert.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AlertCacheTest {

    @Test
    void createShouldReturnAlertCacheWithGivenValues() {
        var cache = AlertCache.create("alert:001", "ALERT", "{}", 60);
        assertEquals("alert:001", cache.cacheKey());
        assertEquals("ALERT", cache.cacheType());
        assertEquals("{}", cache.cachedData());
        assertEquals(0, cache.hitCount());
        assertFalse(cache.isExpired());
        assertNotNull(cache.id());
    }

    @Test
    void isExpiredShouldReturnFalseInitially() {
        var cache = AlertCache.create("key", "TYPE", "data", 3600);
        assertFalse(cache.isExpired());
    }

    @Test
    void withHitShouldIncrementHitCount() {
        var cache = AlertCache.create("key", "TYPE", "data", 60);
        var hit = cache.withHit();
        assertEquals(1, hit.hitCount());
        assertEquals(0, cache.hitCount());
    }

    @Test
    void withHitShouldReturnNewInstance() {
        var cache = AlertCache.create("key", "TYPE", "data", 60);
        var hit = cache.withHit().withHit();
        assertNotSame(cache, hit);
        assertEquals(2, hit.hitCount());
    }
}
