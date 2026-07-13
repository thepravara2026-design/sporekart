package com.sporekart.ai.content.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ContentRecommendationResult(
    UUID id,
    UUID requestId,
    List<Map<String, Object>> recommendations,
    RecommendationType type,
    int totalResults,
    OffsetDateTime generatedAt) {}
