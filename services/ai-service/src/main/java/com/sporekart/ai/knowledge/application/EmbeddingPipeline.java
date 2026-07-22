package com.sporekart.ai.knowledge.application;

import com.sporekart.ai.knowledge.api.EmbeddingService;
import com.sporekart.ai.knowledge.api.KnowledgeChunkRepository;
import com.sporekart.ai.knowledge.api.KnowledgeDocumentRepository;
import com.sporekart.ai.knowledge.domain.*;
import com.sporekart.ai.knowledge.infrastructure.observability.KnowledgeMetricsService;
import java.util.*;

public class EmbeddingPipeline implements EmbeddingService {
    private final KnowledgeChunkRepository chunkRepository;
    private final KnowledgeDocumentRepository documentRepository;
    private final KnowledgeMetricsService metricsService;

    public EmbeddingPipeline(KnowledgeChunkRepository chunkRepository,
                             KnowledgeDocumentRepository documentRepository,
                             KnowledgeMetricsService metricsService) {
        this.chunkRepository = chunkRepository;
        this.documentRepository = documentRepository;
        this.metricsService = metricsService;
    }

    @Override
    public List<Float> embed(String text, EmbeddingProvider provider) {
        metricsService.recordEmbeddingGenerated();
        return generateMockEmbedding(text);
    }

    @Override
    public List<List<Float>> embedBatch(List<String> texts, EmbeddingProvider provider) {
        var results = new ArrayList<List<Float>>();
        for (var text : texts) {
            results.add(embed(text, provider));
        }
        return results;
    }

    @Override
    public void indexDocument(KnowledgeDocumentId documentId, EmbeddingProvider embeddingProvider,
                              VectorStoreProvider vectorStore) {
        var chunks = chunkRepository.findByDocumentIdOrderBySequence(documentId);
        for (var chunk : chunks) {
            var embedding = embed(chunk.content(), embeddingProvider);
            chunk.setEmbedding(embedding);
            chunkRepository.save(chunk);
        }
        metricsService.recordIndexSize(chunks.size());
    }

    @Override
    public void reindexDocument(KnowledgeDocumentId documentId, EmbeddingProvider embeddingProvider,
                                VectorStoreProvider vectorStore) {
        var chunks = chunkRepository.findByDocumentId(documentId);
        for (var chunk : chunks) {
            chunk.setEmbedding(null);
            chunkRepository.save(chunk);
        }
        indexDocument(documentId, embeddingProvider, vectorStore);
    }

    @Override
    public void reindexAll(EmbeddingProvider embeddingProvider, VectorStoreProvider vectorStore) {
        var allChunks = chunkRepository.findByEmbeddingIsNotNull();
        for (var chunk : allChunks) {
            chunk.setEmbedding(null);
            chunkRepository.save(chunk);
        }
        var documents = documentRepository.findAll();
        for (var doc : documents) {
            indexDocument(doc.id(), embeddingProvider, vectorStore);
        }
    }

    public double cosineSimilarity(List<Float> a, List<Float> b) {
        double dot = 0, normA = 0, normB = 0;
        int len = Math.min(a.size(), b.size());
        for (int i = 0; i < len; i++) {
            dot += a.get(i) * b.get(i);
            normA += a.get(i) * a.get(i);
            normB += b.get(i) * b.get(i);
        }
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    private List<Float> generateMockEmbedding(String text) {
        var seed = text.hashCode();
        var random = new Random(seed);
        var embedding = new ArrayList<Float>(128);
        float norm = 0f;
        for (int i = 0; i < 128; i++) {
            var val = random.nextFloat() * 2 - 1;
            embedding.add(val);
            norm += val * val;
        }
        norm = (float) Math.sqrt(norm);
        for (int i = 0; i < 128; i++) {
            embedding.set(i, embedding.get(i) / norm);
        }
        return embedding;
    }
}
