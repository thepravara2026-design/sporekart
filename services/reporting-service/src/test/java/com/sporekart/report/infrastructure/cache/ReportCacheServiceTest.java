package com.sporekart.report.infrastructure.cache;

import com.sporekart.report.domain.model.ReportCache;
import com.sporekart.report.domain.repository.ReportRepositoryPort;
import com.sporekart.report.infrastructure.persistence.InMemoryReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class ReportCacheServiceTest {
    private ReportCacheService cacheService;
    private ReportRepositoryPort repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryReportRepository();
        cacheService = new ReportCacheService(repository);
    }

    @Test
    void shouldPutAndGet() {
        cacheService.put("reports", "all-reports", "data", 300);
        Optional<Object> result = cacheService.get("reports", "all-reports");
        assertTrue(result.isPresent());
        assertEquals("data", result.get());
    }

    @Test
    void shouldReturnEmptyForMissingKey() {
        assertTrue(cacheService.get("reports", "missing").isEmpty());
    }

    @Test
    void shouldInvalidateKey() {
        cacheService.put("reports", "key", "data", 300);
        cacheService.invalidate("key");
        assertTrue(cacheService.get("reports", "key").isEmpty());
    }

    @Test
    void shouldClearAll() {
        cacheService.put("reports", "k1", "d1", 300);
        cacheService.put("reports", "k2", "d2", 300);
        cacheService.clear();
        assertEquals(0, cacheService.getTotalEntryCount());
    }

    @Test
    void shouldTrackHitCount() {
        cacheService.put("reports", "key", "data", 300);
        cacheService.get("reports", "key");
        cacheService.get("reports", "key");
        ReportCache entry = repository.findCacheByKey("key").orElseThrow();
        assertEquals(2, entry.hitCount());
    }

    @Test
    void shouldCountActiveEntries() {
        cacheService.put("reports", "k1", "d1", 300);
        assertEquals(1, cacheService.getActiveEntryCount());
    }

    @Test
    void shouldNotReturnExpiredEntries() {
        ReportCache expired = new ReportCache("id", "expired-key", "type", "data", 0, 0,
            java.time.Instant.now().minusSeconds(10), java.time.Instant.now().minusSeconds(1));
        repository.saveCache(expired);
        assertTrue(cacheService.get("type", "expired-key").isEmpty());
    }

    @Test
    void shouldReturnAllEntries() {
        cacheService.put("reports", "k1", "d1", 300);
        cacheService.put("reports", "k2", "d2", 300);
        assertEquals(2, cacheService.getAllEntries().size());
    }
}
