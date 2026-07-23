package com.sporekart.bi.copilot.dto;

import java.util.List;
import java.util.Map;

import com.sporekart.bi.copilot.domain.Suggestion;

public record ChatResponse(
    String sessionId,
    String message,
    List<Suggestion> suggestions,
    Map<String, Object> context,
    boolean streaming
) {}
