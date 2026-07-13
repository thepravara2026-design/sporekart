package com.sporekart.ai.semantic.infrastructure.monitoring;

import com.sporekart.ai.semantic.application.SemanticIndexService;
import com.sporekart.ai.semantic.application.SemanticEmbeddingService;
import com.sporekart.ai.semantic.infrastructure.SemanticRedisCacheService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SemanticMonitoringServiceTest {

    @Mock private SemanticIndexService indexService;
    @Mock private SemanticEmbeddingService embeddingService;
    @Mock private SemanticRedisCacheService cacheService;

    private MeterRegistry meterRegistry;
    private SemanticMonitoringService monitoringService;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        monitoringService = new SemanticMonitoringService(meterRegistry, indexService, embeddingService, cacheService);
    }

    @Test
    void testRecordEmbedLatency() {
        String result = monitoringService.recordEmbedLatency(() -> "done");
        assertEquals("done", result);
    }

    @Test
    void testRecordSearchLatency() {
        int result = monitoringService.recordSearchLatency(() -> 42);
        assertEquals(42, result);
    }

    @Test
    void testRecordCacheHit() {
        monitoringService.recordCacheHit("embeddings");
        assertEquals(1.0, meterRegistry.counter("semantic.cache.hit", "cache", "embeddings").count());
    }

    @Test
    void testRecordCacheMiss() {
        monitoringService.recordCacheMiss("search");
        assertEquals(1.0, meterRegistry.counter("semantic.cache.miss", "cache", "search").count());
    }

    @Test
    void testRecordEmbeddingCreated() {
        monitoringService.recordEmbeddingCreated("OPENAI");
        assertEquals(1.0, meterRegistry.counter("semantic.embedding.created", "provider", "OPENAI").count());
    }

    @Test
    void testRecordSearchExecuted() {
        monitoringService.recordSearchExecuted("SEMANTIC");
        assertEquals(1.0, meterRegistry.counter("semantic.search.executed", "type", "SEMANTIC").count());
    }

    @Test
    void testGetCacheHitRatio() {
        monitoringService.recordCacheHit("test");
        monitoringService.recordCacheMiss("test");
        double ratio = monitoringService.getCacheHitRatio("test");
        assertEquals(0.5, ratio, 0.001);
    }

    @Test
    void testCheckHealthUp() {
        when(embeddingService.listEmbeddings()).thenReturn(List.of());
        when(indexService.listIndexes()).thenReturn(List.of());

        SemanticMonitoringService.HealthStatus health = monitoringService.checkHealth();

        assertEquals("UP", health.status());
        assertTrue(health.cacheAvailable());
    }
}
