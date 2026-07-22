package com.sporekart.ai.knowledge.application;

import com.sporekart.ai.knowledge.domain.*;
import com.sporekart.ai.knowledge.infrastructure.observability.KnowledgeMetricsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContextBuilderTest {

    private ContextBuilder builder;

    @BeforeEach
    void setUp() {
        builder = new ContextBuilder(new KnowledgeMetricsService());
    }

    @Test
    void shouldBuildContextFromResults() {
        var docId = KnowledgeDocumentId.random();
        var chunkId = KnowledgeChunkId.random();
        var chunk = new KnowledgeChunk(chunkId, docId,
                "Sample content for context", 0, 0, 4, null, null, null, null, Instant.now());
        var citation = new Citation(docId, chunkId, "source-1", "1", "section1",
                "1.0", "ws-1", 0.95, 0.9, "Sample content for context", Instant.now());
        var results = List.of(new RetrievalResult(chunk, citation, 0.95, "VECTOR_SIMILARITY"));

        var context = builder.buildContext("test query", results, 100);

        assertNotNull(context);
        assertTrue(context.assembledContext().contains("Sample content"));
        assertEquals("test query", context.query());
    }

    @Test
    void shouldRespectTokenBudget() {
        var docId = KnowledgeDocumentId.random();
        var chunkId = KnowledgeChunkId.random();
        var largeContent = "A ".repeat(500);
        var chunk = new KnowledgeChunk(chunkId, docId,
                largeContent, 0, 0, 500, null, null, null, null, Instant.now());
        var citation = new Citation(docId, chunkId, "source-1", "1", null,
                "1.0", "ws-1", 0.9, 0.9, largeContent.substring(0, 200), Instant.now());
        var results = List.of(new RetrievalResult(chunk, citation, 0.9, "VECTOR_SIMILARITY"));

        var context = builder.buildContext("query", results, 50);

        assertTrue(context.totalTokens() <= context.maxTokens());
    }

    @Test
    void shouldDeduplicateResults() {
        var docId = KnowledgeDocumentId.random();
        var chunkId = KnowledgeChunkId.random();
        var chunk = new KnowledgeChunk(chunkId, docId,
                "Duplicate content", 0, 0, 3, null, null, null, null, Instant.now());
        var citation = new Citation(docId, chunkId, "source-1", "1", null,
                "1.0", "ws-1", 0.9, 0.9, "Duplicate content", Instant.now());
        var result = new RetrievalResult(chunk, citation, 0.9, "VECTOR_SIMILARITY");

        var context = builder.buildContextWithBudget("query", List.of(result, result), 100, true);

        assertEquals(1, context.results().size());
    }

    @Test
    void shouldIncludeSources() {
        var docId = KnowledgeDocumentId.random();
        var chunkId = KnowledgeChunkId.random();
        var chunk = new KnowledgeChunk(chunkId, docId,
                "Content", 0, 0, 1, null, null, null, null, Instant.now());
        var citation = new Citation(docId, chunkId, "source-1", "1", null,
                "1.0", "ws-1", 0.9, 0.9, "Content", Instant.now());
        var results = List.of(new RetrievalResult(chunk, citation, 0.9, "VECTOR_SIMILARITY"));

        var context = builder.buildContext("query", results, 100);

        assertFalse(context.sources().isEmpty());
        assertTrue(context.sources().contains("source-1"));
    }
}
