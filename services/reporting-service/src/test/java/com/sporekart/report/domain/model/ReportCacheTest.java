package com.sporekart.report.domain.model;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

class ReportCacheTest {
    @Test
    void shouldCreateCacheEntry() {
        ReportCache c = ReportCache.create("reports", "all-reports", "data", 300);
        assertNotNull(c.id());
        assertEquals("all-reports", c.cacheKey());
        assertEquals("reports", c.cacheType());
        assertEquals("data", c.cachedData());
        assertEquals(0, c.hitCount());
        assertFalse(c.isExpired());
    }

    @Test
    void shouldIncrementHitCount() {
        ReportCache c = ReportCache.create("reports", "key", "data", 300);
        ReportCache hit = c.withHit();
        assertEquals(1, hit.hitCount());
    }

    @Test
    void shouldExpire() {
        ReportCache c = new ReportCache("id", "key", "type", "data", 0, 0,
            Instant.now().minusSeconds(10), Instant.now().minusSeconds(1));
        assertTrue(c.isExpired());
    }

    @Test
    void shouldNotExpire() {
        ReportCache c = ReportCache.create("type", "key", "data", 300);
        assertFalse(c.isExpired());
    }
}
