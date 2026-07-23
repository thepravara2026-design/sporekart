package com.sporekart.workspace.dto;

public record SwitchCopilotRequest(
    String sessionId,
    String targetCopilotId,
    String reason,
    boolean preserveContext
) {}
