package com.sporekart.grower.copilot.dto;

import java.util.List;
import java.util.Map;

public record ChatResponse(
    String sessionId,
    String message,
    List<Suggestion> suggestions,
    Map<String, Object> context,
    boolean streaming
) {}
