package com.sporekart.search.domain.model;

import java.util.List;

public record SearchResult(
        List<SearchDocument> documents,
        long totalResults,
        int page,
        int size,
        int totalPages) {
}