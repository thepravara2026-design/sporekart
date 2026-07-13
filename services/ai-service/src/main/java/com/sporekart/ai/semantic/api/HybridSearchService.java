package com.sporekart.ai.semantic.api;

import com.sporekart.ai.semantic.domain.SemanticSearchResult;

import java.util.List;
import java.util.Map;

public interface HybridSearchService extends SemanticSearchService {
    List<SemanticSearchResult> aggregateResults(List<SemanticSearchResult> vectorResults,
                                                 List<SemanticSearchResult> keywordResults,
                                                 Map<String, Double> weights);
    List<SemanticSearchResult> fallbackSearch(String query, Map<String, String> filters);
}
