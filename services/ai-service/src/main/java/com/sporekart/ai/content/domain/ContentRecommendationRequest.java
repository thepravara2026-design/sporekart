package com.sporekart.ai.content.domain;

import com.sporekart.ai.core.domain.ContentType;
import java.util.Map;
import java.util.UUID;

public record ContentRecommendationRequest(
    UUID id,
    String contextId,
    UUID userId,
    RecommendationType recommendationType,
    ContentType contentType,
    int maxResults,
    Map<String, String> filters) {}
