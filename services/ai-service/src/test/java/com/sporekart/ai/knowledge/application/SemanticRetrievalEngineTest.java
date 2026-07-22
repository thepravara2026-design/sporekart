package com.sporekart.ai.knowledge.application;

import com.sporekart.ai.knowledge.api.KnowledgeChunkRepository;
import com.sporekart.ai.knowledge.domain.*;
import com.sporekart.ai.knowledge.infrastructure.observability.KnowledgeMetricsService;
import com.sporekart.ai.knowledge.infrastructure.persistence.InMemoryKnowledgeChunkRepository;
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
class SemanticRetrievalEngineTest {

    @Mock
    private VectorIndexManager vectorIndex;
    @Mock
    private EmbeddingPipeline embeddingPipeline;

    private KnowledgeChunkRepository chunkRepository;
    private KnowledgeMetricsService metricsService;
    private SemanticRetrievalEngine engine;

    @BeforeEach
    void setUp() {
        chunkRepository = new InMemoryKnowledgeChunkRepository();
        metricsService = new KnowledgeMetricsService();
        engine = new SemanticRetrievalEngine(vectorIndex, embeddingPipeline, chunkRepository, metricsService);
    }

    @Test
    void shouldPerformVectorSearch() {
        var chunk1 = new KnowledgeChunk(KnowledgeChunkId.random(), KnowledgeDocumentId.random(),
                "The capital of France is Paris", 0, 0, 5, null, null, null,
                List.of(0.1f, 0.2f), Instant.now());
        var chunk2 = new KnowledgeChunk(KnowledgeChunkId.random(), KnowledgeDocumentId.random(),
                "Berlin is the capital of Germany", 0, 0, 5, null, null, null,
                List.of(0.3f, 0.4f), Instant.now());

        var queryEmbedding = List.of(0.1f, 0.2f);
        when(embeddingPipeline.embed(eq("France"), any())).thenReturn(queryEmbedding);
        when(vectorIndex.findSimilar(eq(queryEmbedding), eq(10), eq("ws-1"), any()))
                .thenReturn(List.of(chunk1, chunk2));

        var results = engine.search("France", 10, RetrievalStrategy.VECTOR_SIMILARITY, "ws-1", Map.of());

        assertEquals(2, results.size());
        assertTrue(results.get(0).score() >= results.get(1).score());
        assertNotNull(results.get(0).chunk());
    }

    @Test
    void shouldPerformKeywordSearch() {
        var chunk = new KnowledgeChunk(KnowledgeChunkId.random(), KnowledgeDocumentId.random(),
                "keyword match content", 0, 0, 3, null, null, null, null, Instant.now());
        chunkRepository.save(chunk);

        when(vectorIndex.keywordSearch(eq("keyword"), eq(10))).thenReturn(List.of(chunk));

        var results = engine.search("keyword", 10, RetrievalStrategy.KEYWORD_BM25, "ws-1", Map.of());

        assertFalse(results.isEmpty());
        assertTrue(results.get(0).chunk().content().contains("keyword"));
    }

    @Test
    void shouldPerformHybridSearch() {
        var chunk = new KnowledgeChunk(KnowledgeChunkId.random(), KnowledgeDocumentId.random(),
                "Hybrid result", 0, 0, 2, null, null, null, null, Instant.now());

        var queryEmbedding = List.of(0.5f, 0.5f);
        when(embeddingPipeline.embed(eq("test query"), any())).thenReturn(queryEmbedding);
        when(vectorIndex.hybridSearch(eq(queryEmbedding), eq("test query"), eq(10), eq(0.5)))
                .thenReturn(List.of(chunk));

        var results = engine.hybridSearch("test query", 10, "ws-1", 0.5, Map.of());

        assertFalse(results.isEmpty());
        assertEquals("HYBRID", results.get(0).retrievalStrategy());
    }

    @Test
    void shouldRespectTopK() {
        var chunk1 = new KnowledgeChunk(KnowledgeChunkId.random(), KnowledgeDocumentId.random(),
                "R1", 0, 0, 1, null, null, null, null, Instant.now());
        var chunk2 = new KnowledgeChunk(KnowledgeChunkId.random(), KnowledgeDocumentId.random(),
                "R2", 0, 0, 1, null, null, null, null, Instant.now());

        var queryEmbedding = List.of(0.1f, 0.2f);
        when(embeddingPipeline.embed(anyString(), any())).thenReturn(queryEmbedding);
        when(vectorIndex.findSimilar(eq(queryEmbedding), eq(2), anyString(), any()))
                .thenReturn(List.of(chunk1, chunk2));

        var results = engine.search("query", 2, RetrievalStrategy.VECTOR_SIMILARITY, "ws-1", Map.of());

        assertTrue(results.size() <= 2);
    }

    @Test
    void shouldReturnEmptyForNoMatch() {
        var queryEmbedding = List.of(0.1f, 0.2f);
        when(embeddingPipeline.embed(anyString(), any())).thenReturn(queryEmbedding);
        when(vectorIndex.findSimilar(any(), anyInt(), anyString(), any())).thenReturn(List.of());

        var results = engine.search("xyznonexistent123", 10, RetrievalStrategy.VECTOR_SIMILARITY, "ws-1", Map.of());

        assertTrue(results.isEmpty());
    }
}
