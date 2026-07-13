package com.sporekart.ai.semantic.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Map;

public record SearchRequest(
        @NotBlank String query,
        String type,
        Map<String, String> filters,
        int limit,
        double threshold) {

    public SearchRequest {
        if (type == null) type = "SEMANTIC";
        if (limit <= 0) limit = 10;
        if (threshold <= 0) threshold = 0.7;
    }
}
