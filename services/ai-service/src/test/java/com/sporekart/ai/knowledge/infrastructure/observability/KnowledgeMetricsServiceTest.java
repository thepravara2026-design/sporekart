package com.sporekart.ai.knowledge.infrastructure.observability;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class KnowledgeMetricsServiceTest {

    private KnowledgeMetricsService metricsService;

    @BeforeEach
    void setUp() {
        metricsService = new KnowledgeMetricsService();
    }

    @Test
    void shouldRecordDocumentIngested() {
        metricsService.recordDocumentIngested();
        metricsService.recordDocumentIngested();

        var metrics = metricsService.getMetrics();
        assertEquals(2L, metrics.get("documentsIngested"));
    }

    @Test
    void shouldRecordChunkCreated() {
        metricsService.recordChunkCreated();
        metricsService.recordChunkCreated();
        metricsService.recordChunkCreated();

        var metrics = metricsService.getMetrics();
        assertEquals(3L, metrics.get("chunksCreated"));
    }

    @Test
    void shouldRecordRetrievalLatency() {
        metricsService.recordRetrieval(100);
        metricsService.recordRetrieval(200);

        var metrics = metricsService.getMetrics();
        assertEquals(2L, metrics.get("retrievalsPerformed"));
        assertEquals(300L, metrics.get("totalRetrievalLatencyMs"));
        assertEquals(150L, metrics.get("averageRetrievalLatencyMs"));
    }

    @Test
    void shouldReset() {
        metricsService.recordDocumentIngested();
        metricsService.recordChunkCreated();
        metricsService.recordRetrieval(50);

        metricsService.reset();

        var metrics = metricsService.getMetrics();
        assertEquals(0L, metrics.get("documentsIngested"));
        assertEquals(0L, metrics.get("chunksCreated"));
        assertEquals(0L, metrics.get("retrievalsPerformed"));
        assertEquals(0L, metrics.get("totalRetrievalLatencyMs"));
    }

    @Test
    void shouldReturnAllMetrics() {
        metricsService.recordDocumentIngested();
        metricsService.recordRetrieval(100);

        var metrics = metricsService.getMetrics();

        assertTrue(metrics.containsKey("documentsIngested"));
        assertTrue(metrics.containsKey("documentsChunked"));
        assertTrue(metrics.containsKey("chunksCreated"));
        assertTrue(metrics.containsKey("embeddingsGenerated"));
        assertTrue(metrics.containsKey("retrievalsPerformed"));
        assertTrue(metrics.containsKey("citationsGenerated"));
        assertTrue(metrics.containsKey("contextBuilds"));
        assertTrue(metrics.containsKey("totalRetrievalLatencyMs"));
        assertTrue(metrics.containsKey("totalEmbeddingLatencyMs"));
        assertTrue(metrics.containsKey("totalChunkingLatencyMs"));
        assertTrue(metrics.containsKey("totalIndexSize"));
        assertTrue(metrics.containsKey("averageRetrievalLatencyMs"));
        assertTrue(metrics.containsKey("averageEmbeddingLatencyMs"));
        assertTrue(metrics.containsKey("averageChunkingLatencyMs"));
    }
}
