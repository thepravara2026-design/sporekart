package com.sporekart.ai.content.interfaces.rest.dto;

import java.util.Map;
import java.util.UUID;

public record ContentRecommendRequest(
        String contextId,
        UUID userId,
        String recommendationType,
        String contentType,
        int maxResults,
        Map<String, String> filters
) {}
