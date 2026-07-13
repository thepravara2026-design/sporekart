package com.sporekart.ai.semantic.interfaces.rest.dto;

import java.util.List;

public record SearchResponse(
        List<SearchResultItem> results,
        int totalCount,
        String searchType,
        long latencyMs) {}
