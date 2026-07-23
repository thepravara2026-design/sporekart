package com.sporekart.workspace.dto;

import java.util.Map;

public record HandoffResponse(
    String handoffId,
    String sessionId,
    String fromCopilotId,
    String toCopilotId,
    String status,
    String message,
    Map<String, Object> transferredContext
) {}
