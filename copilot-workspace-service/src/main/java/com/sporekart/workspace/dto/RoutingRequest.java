package com.sporekart.workspace.dto;

import java.util.List;
import java.util.Map;

public record RoutingRequest(
    String message,
    String sessionId,
    List<String> availableCopilotIds,
    Map<String, Object> context
) {}
