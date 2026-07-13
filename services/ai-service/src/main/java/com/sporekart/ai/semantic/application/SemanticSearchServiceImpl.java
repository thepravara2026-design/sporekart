package com.sporekart.ai.semantic.application;

import com.sporekart.ai.semantic.api.SemanticSearchService;
import com.sporekart.ai.semantic.domain.SearchType;
import com.sporekart.ai.semantic.domain.SemanticSearchResult;
import com.sporekart.ai.semantic.infrastructure.SemanticKafkaEventPublisher;
import com.sporekart.ai.semantic.infrastructure.SemanticRedisCacheService;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticEmbeddingRepository;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticSearchHistoryEntity;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticSearchHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class SemanticSearchServiceImpl implements SemanticSearchService {

    private static final Logger log = LoggerFactory.getLogger(SemanticSearchServiceImpl.class);

    private final SemanticEmbeddingRepository embeddingRepository;
    private final SemanticSearchHistoryRepository searchHistoryRepository;
    private final SemanticRedisCacheService cacheService;
    private final SemanticKafkaEventPublisher kafkaPublisher;

    public SemanticSearchServiceImpl(SemanticEmbeddingRepository embeddingRepository,
                                     SemanticSearchHistoryRepository searchHistoryRepository,
                                     SemanticRedisCacheService cacheService,
                                     SemanticKafkaEventPublisher kafkaPublisher) {
        this.embeddingRepository = embeddingRepository;
        this.searchHistoryRepository = searchHistoryRepository;
        this.cacheService = cacheService;
        this.kafkaPublisher = kafkaPublisher;
    }

    @Override
    public List<SemanticSearchResult> search(String query, SearchType type, Map<String, String> filters,
                                              int limit, double threshold) {
        long start = System.currentTimeMillis();

        String cacheKey = query + ":" + type + ":" + limit + ":" + threshold;
        List<SemanticSearchResult> cached = cacheService.getCachedSearchResults(cacheKey);
        if (cached != null) {
            recordSearchHistory(query, type.name(), filters, cached.size(), System.currentTimeMillis() - start);
            return cached;
        }

        List<SemanticSearchResult> results = performSearch(query, type, filters, limit, threshold);
        long latency = System.currentTimeMillis() - start;

        cacheService.cacheSearchResults(cacheKey, results);
        recordSearchHistory(query, type.name(), filters, results.size(), latency);
        kafkaPublisher.publishSearchExecuted(UUID.randomUUID().toString(), query, results.size(), latency);
        log.debug("Semantic search completed: query={}, type={}, results={}, latency={}ms",
                query, type, results.size(), latency);
        return results;
    }

    @Override
    public List<SemanticSearchResult> similaritySearch(String embeddingId, int limit, double threshold) {
        long start = System.currentTimeMillis();

        String cacheKey = "sim:" + embeddingId + ":" + limit + ":" + threshold;
        List<SemanticSearchResult> cached = cacheService.getCachedSimilarityResults(cacheKey);
        if (cached != null) {
            return cached;
        }

        List<SemanticSearchResult> results = performSimilaritySearch(embeddingId, limit, threshold);
        cacheService.cacheSimilarityResults(cacheKey, results);
        log.debug("Similarity search completed: embeddingId={}, results={}, latency={}ms",
                embeddingId, results.size(), System.currentTimeMillis() - start);
        return results;
    }

    @Override
    public List<SemanticSearchResult> hybridSearch(String query, Map<String, String> filters,
                                                    int limit, double threshold) {
        return search(query, SearchType.HYBRID, filters, limit, threshold);
    }

    @Override
    public List<SemanticSearchResult> contextSearch(String query, Map<String, String> filters) {
        return search(query, SearchType.CONTEXT, filters, 10, 0.5);
    }

    private List<SemanticSearchResult> performSearch(String query, SearchType type,
                                                      Map<String, String> filters,
                                                      int limit, double threshold) {
        List<SemanticSearchResult> results = new ArrayList<>();
        List<com.sporekart.ai.semantic.infrastructure.persistence.SemanticEmbeddingEntity> embeddings =
                embeddingRepository.findByIsDeletedFalse();

        int rank = 1;
        for (var emb : embeddings) {
            if (emb.getContent() != null && emb.getContent().toLowerCase().contains(query.toLowerCase())) {
                double score = calculateRelevance(emb.getContent(), query);
                if (score >= threshold) {
                    results.add(new SemanticSearchResult(
                            emb.getId().toString(),
                            emb.getContent(),
                            score,
                            rank++,
                            Map.of("provider", emb.getProvider() != null ? emb.getProvider() : "",
                                   "model", emb.getModel() != null ? emb.getModel() : "")));
                }
            }
        }

        results.sort((a, b) -> Double.compare(b.score(), a.score()));
        if (results.size() > limit) {
            results = results.subList(0, limit);
        }
        return results;
    }

    private List<SemanticSearchResult> performSimilaritySearch(String embeddingId, int limit, double threshold) {
        return List.of();
    }

    private double calculateRelevance(String content, String query) {
        double keywordScore = 0.0;
        String[] queryTerms = query.toLowerCase().split("\\s+");
        String lowerContent = content.toLowerCase();
        for (String term : queryTerms) {
            if (lowerContent.contains(term)) {
                keywordScore += 1.0 / queryTerms.length;
            }
        }
        return keywordScore;
    }

    private void recordSearchHistory(String query, String searchType, Map<String, String> filters,
                                      int resultCount, long latencyMs) {
        try {
            SemanticSearchHistoryEntity history = new SemanticSearchHistoryEntity();
            history.setQuery(query);
            history.setSearchType(searchType);
            history.setFilters(filters != null ? filters.toString() : null);
            history.setResultCount(resultCount);
            history.setLatencyMs(latencyMs);
            history.setCreatedAt(OffsetDateTime.now());
            searchHistoryRepository.save(history);
        } catch (Exception e) {
            log.warn("Failed to record search history: {}", e.getMessage());
        }
    }
}
