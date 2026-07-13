package com.sporekart.ai.semantic.api;

import com.sporekart.ai.semantic.domain.SemanticSearchResult;

import java.util.List;
import java.util.Map;

public interface ContextRetrievalService {
    ContextResult retrieveContext(String query, Map<String, String> filters, int maxResults);

    record ContextResult(
            String query,
            List<SemanticSearchResult> results,
            int totalResults) {}
}
