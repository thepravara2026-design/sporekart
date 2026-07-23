package com.sporekart.marketing.copilot.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ContentGenerationResponse(
    String contentId,
    String title,
    String body,
    String contentType,
    String status,
    List<String> seoRecommendations,
    LocalDateTime createdAt
) {}
