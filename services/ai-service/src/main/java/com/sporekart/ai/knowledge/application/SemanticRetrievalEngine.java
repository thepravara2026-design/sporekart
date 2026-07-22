package com.sporekart.ai.knowledge.application;

import com.sporekart.ai.knowledge.api.KnowledgeChunkRepository;
import com.sporekart.ai.knowledge.api.SemanticRetrievalService;
import com.sporekart.ai.knowledge.domain.*;
import com.sporekart.ai.knowledge.infrastructure.observability.KnowledgeMetricsService;
import java.time.Instant;
import java.util.*;

public class SemanticRetrievalEngine implements SemanticRetrievalService {
    private final VectorIndexManager vectorIndex;
    private final EmbeddingPipeline embeddingPipeline;
    private final KnowledgeChunkRepository chunkRepository;
    private final KnowledgeMetricsService metricsService;

    public SemanticRetrievalEngine(VectorIndexManager vectorIndex,
                                   EmbeddingPipeline embeddingPipeline,
                                   KnowledgeChunkRepository chunkRepository,
                                   KnowledgeMetricsService metricsService) {
        this.vectorIndex = vectorIndex;
        this.embeddingPipeline = embeddingPipeline;
        this.chunkRepository = chunkRepository;
        this.metricsService = metricsService;
    }

    @Override
    public List<RetrievalResult> search(String query, int topK, RetrievalStrategy strategy,
                                        String workspaceId, Map<String, String> filters) {
        long start = System.currentTimeMillis();
        var results = switch (strategy) {
            case VECTOR_SIMILARITY -> semanticSearch(query, topK, workspaceId, filters);
            case KEYWORD_BM25 -> keywordSearch(query, topK, workspaceId, filters);
            case HYBRID -> hybridSearch(query, topK, workspaceId, 0.7, filters);
            case SEMANTIC -> semanticSearch(query, topK, workspaceId, filters);
        };
        metricsService.recordRetrieval(System.currentTimeMillis() - start);
        return results;
    }

    @Override
    public List<RetrievalResult> semanticSearch(String query, int topK, String workspaceId,
                                                Map<String, String> filters) {
        var queryEmbedding = embeddingPipeline.embed(query, EmbeddingProvider.OPENAI);
        var chunks = vectorIndex.findSimilar(queryEmbedding, topK, workspaceId, filters);
        return toResults(chunks, queryEmbedding, "VECTOR");
    }

    @Override
    public List<RetrievalResult> hybridSearch(String query, int topK, String workspaceId,
                                              double vectorWeight, Map<String, String> filters) {
        var queryEmbedding = embeddingPipeline.embed(query, EmbeddingProvider.OPENAI);
        var chunks = vectorIndex.hybridSearch(queryEmbedding, query, topK, vectorWeight);
        return toResults(chunks, queryEmbedding, "HYBRID");
    }

    private List<RetrievalResult> keywordSearch(String query, int topK, String workspaceId,
                                                Map<String, String> filters) {
        var chunks = vectorIndex.keywordSearch(query, topK);
        return chunks.stream()
                .map(c -> new RetrievalResult(c,
                        new Citation(c.documentId(), c.id(), "keyword_search", null, null,
                                "1.0", workspaceId != null ? workspaceId : "",
                                0.5, 0.5,
                                c.content().substring(0, Math.min(200, c.content().length())),
                                Instant.now()),
                        0.5, "KEYWORD"))
                .toList();
    }

    private List<RetrievalResult> toResults(List<KnowledgeChunk> chunks,
                                            List<Float> queryEmbedding, String strategy) {
        return chunks.stream()
                .map(c -> {
                    var score = c.embedding() != null && !c.embedding().isEmpty()
                            ? embeddingPipeline.cosineSimilarity(queryEmbedding, c.embedding())
                            : 0.5;
                    var citation = new Citation(c.documentId(), c.id(), strategy, null,
                            c.heading(), "1.0", "", score, score,
                            c.content().substring(0, Math.min(200, c.content().length())),
                            Instant.now());
                    return new RetrievalResult(c, citation, score, strategy);
                })
                .sorted((a, b) -> Double.compare(b.score(), a.score()))
                .toList();
    }
}
