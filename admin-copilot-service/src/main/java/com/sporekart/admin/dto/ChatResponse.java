package com.sporekart.admin.dto;

import com.sporekart.copilot.domain.Suggestion;

import java.util.List;
import java.util.Map;

public record ChatResponse(
    String sessionId,
    String message,
    List<Suggestion> suggestions,
    Map<String, Object> context,
    boolean streaming
) {}
