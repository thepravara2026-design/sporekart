package com.sporekart.ai.content.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ContentClassificationResult(
    UUID id,
    UUID requestId,
    Map<String, Double> classifications,
    String primaryCategory,
    double confidenceScore,
    List<String> keywords,
    OffsetDateTime generatedAt) {}
