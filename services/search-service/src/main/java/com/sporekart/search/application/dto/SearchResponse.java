package com.sporekart.search.application.dto;

import com.sporekart.search.domain.model.SearchDocument;

import java.util.List;

public record SearchResponse(
        List<SearchDocument> documents,
        long totalResults,
        int page,
        int size,
        int totalPages) {
}