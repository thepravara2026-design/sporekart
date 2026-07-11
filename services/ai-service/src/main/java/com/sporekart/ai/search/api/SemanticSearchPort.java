package com.sporekart.ai.search.api;

import com.sporekart.ai.search.domain.SearchQuery;
import com.sporekart.ai.search.domain.SearchResult;
import java.util.List;

public interface SemanticSearchPort {
    List<SearchResult> search(SearchQuery query);
    List<Float> generateEmbedding(String text);
}
