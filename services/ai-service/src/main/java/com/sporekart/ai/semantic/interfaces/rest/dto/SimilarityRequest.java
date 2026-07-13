package com.sporekart.ai.semantic.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record SimilarityRequest(
        @NotBlank String embeddingId,
        int limit,
        double threshold,
        String algorithm) {

    public SimilarityRequest {
        if (limit <= 0) limit = 10;
        if (threshold <= 0) threshold = 0.7;
        if (algorithm == null) algorithm = "COSINE";
    }
}
