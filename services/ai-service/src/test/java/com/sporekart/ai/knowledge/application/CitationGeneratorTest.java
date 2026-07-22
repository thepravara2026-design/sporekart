package com.sporekart.ai.knowledge.application;

import com.sporekart.ai.knowledge.domain.*;
import com.sporekart.ai.knowledge.infrastructure.observability.KnowledgeMetricsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CitationGeneratorTest {

    private CitationGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new CitationGenerator(new KnowledgeMetricsService());
    }

    @Test
    void shouldGenerateCitations() {
        var chunk1 = new KnowledgeChunk(KnowledgeChunkId.random(), KnowledgeDocumentId.random(),
                "Content A", 0, 0, 2, "Intro", "section1", null, null, Instant.now());
        var chunk2 = new KnowledgeChunk(KnowledgeChunkId.random(), KnowledgeDocumentId.random(),
                "Content B", 0, 1, 2, "Details", "section2", null, null, Instant.now());

        var docId = KnowledgeDocumentId.random();
        var results = List.of(
                new RetrievalResult(chunk1, new Citation(docId, chunk1.id(), "source-1", "1",
                        "section1", "1.0", "ws-1", 0.95, 0.9, "Content A", Instant.now()), 0.95, "VECTOR_SIMILARITY"),
                new RetrievalResult(chunk2, new Citation(docId, chunk2.id(), "source-1", "2",
                        "section2", "1.0", "ws-1", 0.85, 0.8, "Content B", Instant.now()), 0.85, "VECTOR_SIMILARITY")
        );

        var citations = generator.generateCitations("test query", results);

        assertEquals(2, citations.size());
    }

    @Test
    void shouldFormatCitations() {
        var chunkId = KnowledgeChunkId.random();
        var docId = KnowledgeDocumentId.random();
        var citations = List.of(
                new Citation(docId, chunkId, "source-1", "3", "section1",
                        "1.0", "ws-1", 0.95, 0.9, "Excerpt text", Instant.now())
        );

        var formatted = generator.formatCitations(citations);

        assertNotNull(formatted);
        assertFalse(formatted.isEmpty());
        assertTrue(formatted.containsKey("citations"));
        assertEquals(1, formatted.get("totalCitations"));
    }

    @Test
    void shouldHandleEmptyResults() {
        var citations = generator.generateCitations("test query", List.of());

        assertEquals(0, citations.size());
    }
}
