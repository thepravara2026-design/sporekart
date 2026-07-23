package com.sporekart.workspace.dto;

import java.util.Map;

public record SwitchCopilotResponse(
    String sessionId,
    String previousCopilotId,
    String currentCopilotId,
    String status,
    String message,
    Map<String, Object> context
) {}
