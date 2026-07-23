package com.sporekart.grower.copilot.dto;

import java.util.List;
import java.util.Map;

public record RecommendResponse(
    String recommendationType,
    String title,
    String content,
    List<Object> recommendations,
    Map<String, Object> metadata
) {}
