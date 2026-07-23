package com.sporekart.workspace.dto;

public record HandoffRequest(
    String sessionId,
    String fromCopilotId,
    String toCopilotId,
    String reason,
    String contextSummary
) {}
