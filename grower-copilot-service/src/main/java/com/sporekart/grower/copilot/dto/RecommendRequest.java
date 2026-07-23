package com.sporekart.grower.copilot.dto;

public record RecommendRequest(
    String query,
    String mushroomType,
    String category,
    String difficulty,
    String farmLocation,
    String experienceLevel
) {}
