package com.sporekart.ai.knowledge.application;

import com.sporekart.ai.knowledge.api.ContextBuilderService;
import com.sporekart.ai.knowledge.api.SemanticRetrievalService;
import com.sporekart.ai.knowledge.domain.*;
import com.sporekart.ai.knowledge.infrastructure.observability.KnowledgeMetricsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RagPipelineTest {

    @Mock
    private SemanticRetrievalService retrievalService;
    @Mock
    private ContextBuilderService contextBuilder;

    private CitationGenerator citationGenerator;
    private KnowledgeMetricsService metricsService;
    private RagPipeline pipeline;

    @BeforeEach
    void setUp() {
        metricsService = new KnowledgeMetricsService();
        citationGenerator = new CitationGenerator(metricsService);
        pipeline = new RagPipeline(retrievalService, contextBuilder, citationGenerator, metricsService);
    }

    @Test
    void shouldExecuteFullRagQuery() {
        var chunk = new KnowledgeChunk(KnowledgeChunkId.random(), KnowledgeDocumentId.random(),
                "Paris is the capital of France", 0, 0, 6, null, null, null, null, Instant.now());
        var results = List.of(new RetrievalResult(chunk, null, 0.95, "VECTOR_SIMILARITY"));
        var ragContext = new RagContext("What is the capital of France?", results,
                "Context: Paris is the capital of France", 20, 100, List.of("source-1"));

        when(retrievalService.search(anyString(), anyInt(), any(), anyString(), any()))
                .thenReturn(results);
        when(contextBuilder.buildContext(anyString(), anyList(), anyInt()))
                .thenReturn(ragContext);

        var context = pipeline.executeQuery("What is the capital of France?", "ws-1", 5,
                RetrievalStrategy.VECTOR_SIMILARITY, 100, EmbeddingProvider.LOCAL, Map.of());

        assertNotNull(context);
        assertTrue(context.assembledContext().contains("Paris"));
    }

    @Test
    void shouldGetCitationsForResult() {
        var docId = KnowledgeDocumentId.random();
        var chunkId = KnowledgeChunkId.random();
        var chunk = new KnowledgeChunk(chunkId, docId, "Content", 0, 0, 2, null, null, null, null, Instant.now());
        var result = new RetrievalResult(chunk,
                new Citation(docId, chunkId, "source-1", "1", "section1",
                        "1.0", "ws-1", 0.95, 0.9, "Content", Instant.now()),
                0.95, "VECTOR_SIMILARITY");

        var citations = pipeline.getCitations("query", result);

        assertFalse(citations.isEmpty());
        assertEquals(1, citations.size());
    }

    @Test
    void shouldHandleEmptyQueryResults() {
        var ragContext = new RagContext("gibberish_12345", List.of(), "", 0, 100, List.of());

        when(retrievalService.search(eq("gibberish_12345"), anyInt(), any(), anyString(), any()))
                .thenReturn(List.of());
        when(contextBuilder.buildContext(eq("gibberish_12345"), anyList(), anyInt()))
                .thenReturn(ragContext);

        var context = pipeline.executeQuery("gibberish_12345", "ws-1", 5,
                RetrievalStrategy.VECTOR_SIMILARITY, 100, EmbeddingProvider.LOCAL, Map.of());

        assertNotNull(context);
        assertTrue(context.results().isEmpty());
    }
}
