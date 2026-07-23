package com.sporekart.workspace.dto;

import java.util.List;
import java.util.Map;

public record WorkspaceStatusResponse(
    String workspaceId,
    String name,
    String status,
    int activeSessions,
    int totalSessions,
    List<CopilotInfo> availableCopilots,
    Map<String, Object> analytics
) {}
