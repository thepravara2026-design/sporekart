package com.sporekart.search.domain.model;

import java.util.Map;

public record SearchQuery(
        String query,
        Map<String, String> filters,
        int page,
        int size,
        String sortBy,
        String sortOrder) {
}