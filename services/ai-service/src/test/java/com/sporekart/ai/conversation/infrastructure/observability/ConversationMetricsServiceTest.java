package com.sporekart.ai.conversation.infrastructure.observability;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ConversationMetricsServiceTest {

    private ConversationMetricsService metrics;

    @BeforeEach
    void setUp() {
        metrics = new ConversationMetricsService();
    }

    @Test
    void shouldTrackActiveConversations() {
        metrics.incrementActiveConversations();
        metrics.incrementActiveConversations();
        metrics.decrementActiveConversations();
        var result = metrics.getMetrics();
        assertEquals(1, result.get("activeConversations"));
    }

    @Test
    void shouldTrackMessages() {
        metrics.recordMessage();
        metrics.recordMessage();
        metrics.recordMessage();
        var result = metrics.getMetrics();
        assertEquals(3, result.get("totalMessages"));
    }

    @Test
    void shouldTrackTokens() {
        metrics.recordTokens(100);
        metrics.recordTokens(200);
        var result = metrics.getMetrics();
        assertEquals(300L, result.get("totalTokenUsage"));
    }

    @Test
    void shouldTrackMemoryRetrievalLatency() {
        metrics.recordMemoryRetrieval(50);
        metrics.recordMemoryRetrieval(150);
        var result = metrics.getMetrics();
        assertEquals(2, result.get("totalMemoryRetrievals"));
        assertEquals(100.0, (Double) result.get("averageMemoryRetrievalLatency"), 0.1);
    }

    @Test
    void shouldTrackSummarization() {
        metrics.recordSummarization(200);
        var result = metrics.getMetrics();
        assertEquals(1, result.get("totalSummarizations"));
    }

    @Test
    void shouldReturnAllMetricKeys() {
        metrics.incrementActiveConversations();
        metrics.recordMessage();
        metrics.recordTokens(50);
        var result = metrics.getMetrics();
        assertTrue(result.containsKey("activeConversations"));
        assertTrue(result.containsKey("totalMessages"));
        assertTrue(result.containsKey("totalTokenUsage"));
        assertTrue(result.containsKey("averageMemoryRetrievalLatency"));
        assertTrue(result.containsKey("totalMemoryRetrievals"));
        assertTrue(result.containsKey("totalSummarizations"));
        assertTrue(result.containsKey("averageSummarizationLatency"));
        assertTrue(result.containsKey("activeSessions"));
        assertTrue(result.containsKey("totalContextCompressions"));
    }

    @Test
    void shouldResetMetrics() {
        metrics.incrementActiveConversations();
        metrics.recordMessage();
        metrics.recordTokens(100);
        metrics.reset();
        var result = metrics.getMetrics();
        assertEquals(0, result.get("activeConversations"));
        assertEquals(0, result.get("totalMessages"));
        assertEquals(0L, result.get("totalTokenUsage"));
    }
}
