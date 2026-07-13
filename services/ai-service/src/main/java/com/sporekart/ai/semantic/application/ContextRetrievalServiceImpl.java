package com.sporekart.ai.semantic.application;

import com.sporekart.ai.semantic.api.ContextRetrievalService;
import com.sporekart.ai.semantic.domain.SearchType;
import com.sporekart.ai.semantic.domain.SemanticSearchResult;
import com.sporekart.ai.semantic.infrastructure.SemanticRedisCacheService;
import com.sporekart.ai.semantic.infrastructure.persistence.SemanticEmbeddingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ContextRetrievalServiceImpl implements ContextRetrievalService {

    private static final Logger log = LoggerFactory.getLogger(ContextRetrievalServiceImpl.class);

    private final SemanticEmbeddingRepository embeddingRepository;
    private final SemanticRedisCacheService cacheService;

    public ContextRetrievalServiceImpl(SemanticEmbeddingRepository embeddingRepository,
                                       SemanticRedisCacheService cacheService) {
        this.embeddingRepository = embeddingRepository;
        this.cacheService = cacheService;
    }

    @Override
    public ContextResult retrieveContext(String query, Map<String, String> filters, int maxResults) {
        if (query == null || query.isBlank()) {
            return new ContextResult("", List.of(), 0);
        }
        String cacheKey = "ctx:" + query + ":" + maxResults;
        ContextResult cached = cacheService.getCachedContextResult(cacheKey);
        if (cached != null) {
            return cached;
        }

        List<com.sporekart.ai.semantic.infrastructure.persistence.SemanticEmbeddingEntity> embeddings =
                embeddingRepository.findByIsDeletedFalse();

        List<SemanticSearchResult> results = embeddings.stream()
                .filter(e -> e.getContent() != null && e.getStatus() != null
                        && e.getStatus().equals("COMPLETED"))
                .filter(e -> e.getContent().toLowerCase().contains(query.toLowerCase()))
                .map(e -> {
                    double score = calculateContextScore(e.getContent(), query);
                    return new SemanticSearchResult(
                            e.getId().toString(),
                            e.getContent(),
                            score,
                            1,
                            Map.of("provider", e.getProvider() != null ? e.getProvider() : ""));
                })
                .sorted((a, b) -> Double.compare(b.score(), a.score()))
                .limit(maxResults)
                .toList();

        ContextResult result = new ContextResult(query, results, results.size());
        cacheService.cacheContextResult(cacheKey, result);
        log.debug("Context retrieval completed: query={}, results={}", query, results.size());
        return result;
    }

    private double calculateContextScore(String content, String query) {
        String[] terms = query.toLowerCase().split("\\s+");
        String lowerContent = content.toLowerCase();
        long matchCount = java.util.Arrays.stream(terms).filter(lowerContent::contains).count();
        return terms.length > 0 ? (double) matchCount / terms.length : 0.0;
    }
}
