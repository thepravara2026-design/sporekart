package com.sporekart.ai.knowledge.application;

import com.sporekart.ai.knowledge.api.KnowledgeChunkRepository;
import com.sporekart.ai.knowledge.domain.*;
import com.sporekart.ai.knowledge.infrastructure.observability.KnowledgeMetricsService;
import java.util.*;
import java.util.stream.*;

public class VectorIndexManager {
    private final KnowledgeChunkRepository chunkRepository;
    private final EmbeddingPipeline embeddingPipeline;
    private final KnowledgeMetricsService metricsService;

    public VectorIndexManager(KnowledgeChunkRepository chunkRepository,
                              EmbeddingPipeline embeddingPipeline,
                              KnowledgeMetricsService metricsService) {
        this.chunkRepository = chunkRepository;
        this.embeddingPipeline = embeddingPipeline;
        this.metricsService = metricsService;
    }

    public List<KnowledgeChunk> findSimilar(List<Float> queryEmbedding, int topK,
                                            String workspaceId, Map<String, String> filters) {
        var allChunks = chunkRepository.findByEmbeddingIsNotNull();

        return allChunks.stream()
                .filter(c -> c.embedding() != null && !c.embedding().isEmpty())
                .map(c -> new AbstractMap.SimpleEntry<>(c,
                        embeddingPipeline.cosineSimilarity(queryEmbedding, c.embedding())))
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(topK)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<KnowledgeChunk> keywordSearch(String query, int topK) {
        var allChunks = chunkRepository.findAll();
        var q = query.toLowerCase();
        return allChunks.stream()
                .filter(c -> c.content().toLowerCase().contains(q))
                .limit(topK)
                .collect(Collectors.toList());
    }

    public List<KnowledgeChunk> hybridSearch(List<Float> queryEmbedding, String query,
                                             int topK, double vectorWeight) {
        var vectorResults = findSimilar(queryEmbedding, topK * 2, null, new HashMap<>());
        var keywordResults = keywordSearch(query, topK * 2);

        var scored = new HashMap<KnowledgeChunkId, Double>();
        for (var chunk : vectorResults) {
            var sim = embeddingPipeline.cosineSimilarity(queryEmbedding, chunk.embedding());
            scored.merge(chunk.id(), sim * vectorWeight, Double::max);
        }
        for (var chunk : keywordResults) {
            scored.merge(chunk.id(), (1.0 - vectorWeight), Double::sum);
        }

        return scored.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(topK)
                .map(e -> chunkRepository.findById(e.getKey()).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
