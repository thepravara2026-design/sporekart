package com.sporekart.ai.content.infrastructure.monitoring;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContentMonitoringServiceTest {

    private SimpleMeterRegistry meterRegistry;
    private ContentMonitoringService monitoringService;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        monitoringService = new ContentMonitoringService(meterRegistry);
    }

    @Test
    void shouldRecordGenerationLatency() {
        var result = monitoringService.recordGenerationLatency(() -> "generated");
        assertEquals("generated", result);
    }

    @Test
    void shouldRecordSummaryLatency() {
        var result = monitoringService.recordSummaryLatency(() -> 42);
        assertEquals(42, result);
    }

    @Test
    void shouldRecordTranslationLatency() {
        var result = monitoringService.recordTranslationLatency(() -> "translated");
        assertEquals("translated", result);
    }

    @Test
    void shouldRecordClassificationLatency() {
        var result = monitoringService.recordClassificationLatency(() -> "classified");
        assertEquals("classified", result);
    }

    @Test
    void shouldRecordModerationLatency() {
        var result = monitoringService.recordModerationLatency(() -> "moderated");
        assertEquals("moderated", result);
    }

    @Test
    void shouldRecordSEOLatency() {
        var result = monitoringService.recordSEOLatency(() -> "seo");
        assertEquals("seo", result);
    }

    @Test
    void shouldRecordCacheHit() {
        monitoringService.recordCacheHit();
        assertEquals(1.0, meterRegistry.counter("content.cache.hits").count());
    }

    @Test
    void shouldRecordCacheMiss() {
        monitoringService.recordCacheMiss();
        assertEquals(1.0, meterRegistry.counter("content.cache.misses").count());
    }

    @Test
    void shouldReturnZeroCacheHitRatioWhenNoData() {
        var ratio = monitoringService.getCacheHitRatio();
        assertEquals(0.0, ratio);
    }

    @Test
    void shouldReturnCacheHitRatio() {
        monitoringService.recordCacheHit();
        monitoringService.recordCacheHit();
        monitoringService.recordCacheMiss();
        var ratio = monitoringService.getCacheHitRatio();
        assertEquals(2.0 / 3.0, ratio, 0.001);
    }

    @Test
    void shouldReturnPerfectCacheHitRatio() {
        monitoringService.recordCacheHit();
        var ratio = monitoringService.getCacheHitRatio();
        assertEquals(1.0, ratio, 0.001);
    }

    @Test
    void shouldReturnZeroCacheHitRatioWhenOnlyMisses() {
        monitoringService.recordCacheMiss();
        monitoringService.recordCacheMiss();
        var ratio = monitoringService.getCacheHitRatio();
        assertEquals(0.0, ratio);
    }

    @Test
    void shouldReturnUpHealth() {
        var health = monitoringService.checkHealth();
        assertEquals("UP", health.status());
    }
}
