package com.sporekart.ai.search.api;

import com.sporekart.ai.search.domain.SearchQuery;
import com.sporekart.ai.search.domain.SearchResult;
import java.util.List;

public interface SearchService {
    List<SearchResult> search(SearchQuery query);
    List<SearchResult> semanticSearch(SearchQuery query);
    List<SearchResult> hybridSearch(SearchQuery query);
}
