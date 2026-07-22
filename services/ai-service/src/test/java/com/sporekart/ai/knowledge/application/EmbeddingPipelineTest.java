package com.sporekart.ai.knowledge.application;

import com.sporekart.ai.knowledge.domain.*;
import com.sporekart.ai.knowledge.api.KnowledgeChunkRepository;
import com.sporekart.ai.knowledge.api.KnowledgeDocumentRepository;
import com.sporekart.ai.knowledge.infrastructure.observability.KnowledgeMetricsService;
import com.sporekart.ai.knowledge.infrastructure.persistence.InMemoryKnowledgeChunkRepository;
import com.sporekart.ai.knowledge.infrastructure.persistence.InMemoryKnowledgeDocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmbeddingPipelineTest {

    private KnowledgeChunkRepository chunkRepository;
    private KnowledgeDocumentRepository documentRepository;
    private EmbeddingPipeline pipeline;

    @BeforeEach
    void setUp() {
        chunkRepository = new InMemoryKnowledgeChunkRepository();
        documentRepository = new InMemoryKnowledgeDocumentRepository();
        pipeline = new EmbeddingPipeline(chunkRepository, documentRepository, new KnowledgeMetricsService());
    }

    @Test
    void shouldGenerateDeterministicEmbedding() {
        var result = pipeline.embed("hello world", EmbeddingProvider.LOCAL);

        assertEquals(128, result.size());
        assertFalse(result.stream().allMatch(v -> v == 0.0f));
    }

    @Test
    void shouldGenerateDifferentEmbeddingsForDifferentText() {
        var resultA = pipeline.embed("text A", EmbeddingProvider.LOCAL);
        var resultB = pipeline.embed("text B", EmbeddingProvider.LOCAL);

        assertNotEquals(resultA, resultB);
    }

    @Test
    void shouldIndexDocument() {
        var docId = KnowledgeDocumentId.random();
        var chunk = new KnowledgeChunk(KnowledgeChunkId.random(), docId, "Content", 0, 0, 2,
                null, null, null, null, Instant.now());
        chunkRepository.save(chunk);

        pipeline.indexDocument(docId, EmbeddingProvider.LOCAL, VectorStoreProvider.FAISS);

        var updated = chunkRepository.findById(chunk.id());
        assertTrue(updated.isPresent());
        assertNotNull(updated.get().embedding());
        assertEquals(128, updated.get().embedding().size());
    }

    @Test
    void shouldReindexDocument() {
        var docId = KnowledgeDocumentId.random();
        var chunk = new KnowledgeChunk(KnowledgeChunkId.random(), docId, "Content", 0, 0, 2,
                null, null, null, List.of(1.0f, 0.0f), Instant.now());
        chunkRepository.save(chunk);

        pipeline.reindexDocument(docId, EmbeddingProvider.LOCAL, VectorStoreProvider.FAISS);

        var updated = chunkRepository.findById(chunk.id());
        assertTrue(updated.isPresent());
        assertNotNull(updated.get().embedding());
    }

    @Test
    void shouldComputeCosineSimilarity() {
        var identical = List.of(1.0f, 0.0f, 0.0f);
        var orthogonal = List.of(0.0f, 1.0f, 0.0f);

        var simIdentical = pipeline.cosineSimilarity(identical, identical);
        var simOrthogonal = pipeline.cosineSimilarity(identical, orthogonal);

        assertEquals(1.0, simIdentical, 0.0001);
        assertEquals(0.0, simOrthogonal, 0.0001);
    }
}
