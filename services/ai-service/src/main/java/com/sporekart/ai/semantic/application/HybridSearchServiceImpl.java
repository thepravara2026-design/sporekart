package com.sporekart.ai.semantic.application;

import com.sporekart.ai.semantic.api.HybridSearchService;
import com.sporekart.ai.semantic.domain.SearchType;
import com.sporekart.ai.semantic.domain.SemanticSearchResult;
import com.sporekart.ai.semantic.infrastructure.SemanticKafkaEventPublisher;
import com.sporekart.ai.semantic.infrastructure.SemanticRedisCacheService;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticEmbeddingRepository;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticSearchHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class HybridSearchServiceImpl extends SemanticSearchServiceImpl implements HybridSearchService {

    private static final Logger log = LoggerFactory.getLogger(HybridSearchServiceImpl.class);

    public HybridSearchServiceImpl(SemanticEmbeddingRepository embeddingRepository,
                                   SemanticSearchHistoryRepository searchHistoryRepository,
                                   SemanticRedisCacheService cacheService,
                                   SemanticKafkaEventPublisher kafkaPublisher) {
        super(embeddingRepository, searchHistoryRepository, cacheService, kafkaPublisher);
    }

    @Override
    public List<SemanticSearchResult> aggregateResults(List<SemanticSearchResult> vectorResults,
                                                        List<SemanticSearchResult> keywordResults,
                                                        Map<String, Double> weights) {
        double vectorWeight = weights.getOrDefault("vector", 0.7);
        double keywordWeight = weights.getOrDefault("keyword", 0.3);

        Map<String, SemanticSearchResult> merged = new LinkedHashMap<>();

        for (SemanticSearchResult result : vectorResults) {
            String key = result.documentId();
            merged.put(key, new SemanticSearchResult(
                    result.documentId(),
                    result.content(),
                    result.score() * vectorWeight,
                    result.rank(),
                    result.metadata()));
        }

        for (SemanticSearchResult result : keywordResults) {
            String key = result.documentId();
            if (merged.containsKey(key)) {
                SemanticSearchResult existing = merged.get(key);
                merged.put(key, new SemanticSearchResult(
                        existing.documentId(),
                        existing.content(),
                        existing.score() + (result.score() * keywordWeight),
                        existing.rank(),
                        existing.metadata()));
            } else {
                merged.put(key, new SemanticSearchResult(
                        result.documentId(),
                        result.content(),
                        result.score() * keywordWeight,
                        result.rank(),
                        result.metadata()));
            }
        }

        return merged.values().stream()
                .sorted(Comparator.comparingDouble(SemanticSearchResult::score).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<SemanticSearchResult> fallbackSearch(String query, Map<String, String> filters) {
        List<SemanticSearchResult> results = search(query, SearchType.KEYWORD, filters, 10, 0.0);
        log.info("Fallback search executed: query={}, results={}", query, results.size());
        return results;
    }
}
