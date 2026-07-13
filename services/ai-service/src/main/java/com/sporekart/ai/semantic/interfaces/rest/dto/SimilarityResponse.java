package com.sporekart.ai.semantic.interfaces.rest.dto;

import java.util.List;

public record SimilarityResponse(
        List<SimilarityResultItem> results,
        String sourceId,
        String algorithm,
        long latencyMs) {}
