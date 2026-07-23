package com.sporekart.customer.copilot.dto;

import com.sporekart.copilot.Suggestion;

import java.util.List;
import java.util.Map;

public record ChatResponse(
    String sessionId,
    String message,
    List<Suggestion> suggestions,
    Map<String, Object> context,
    boolean streaming
) {
    public ChatResponse {
        if (sessionId == null) {
            sessionId = "";
        }
        if (message == null) {
            message = "";
        }
        if (suggestions == null) {
            suggestions = List.of();
        }
        if (context == null) {
            context = Map.of();
        }
    }
}
