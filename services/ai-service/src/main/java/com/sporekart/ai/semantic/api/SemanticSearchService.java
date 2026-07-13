package com.sporekart.ai.semantic.api;

import com.sporekart.ai.semantic.domain.SearchType;
import com.sporekart.ai.semantic.domain.SemanticSearchResult;

import java.util.List;
import java.util.Map;

public interface SemanticSearchService {
    List<SemanticSearchResult> search(String query, SearchType type, Map<String, String> filters, int limit, double threshold);
    List<SemanticSearchResult> similaritySearch(String embeddingId, int limit, double threshold);
    List<SemanticSearchResult> hybridSearch(String query, Map<String, String> filters, int limit, double threshold);
    List<SemanticSearchResult> contextSearch(String query, Map<String, String> filters);
}
