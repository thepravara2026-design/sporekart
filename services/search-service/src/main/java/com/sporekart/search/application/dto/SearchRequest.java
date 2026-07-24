package com.sporekart.search.application.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.util.Map;

public record SearchRequest(
        @Size(min = 2, message = "Search query must be at least 2 characters") String query,
        Map<String, String> filters,
        @Min(0) int page,
        @Min(1) @Max(100) int size,
        String sortBy,
        String sortOrder) {

    public SearchRequest {
        if (size == 0) {
            size = 20;
        }
        if (sortOrder == null) {
            sortOrder = "desc";
        }
        if (sortBy == null) {
            sortBy = "score";
        }
    }
}