package com.sporekart.workspace.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public record WorkspaceSession(
    String sessionId,
    String workspaceId,
    String userId,
    String activeCopilotId,
    String status,
    List<ConversationTurn> conversationHistory,
    Map<String, Object> sharedContext,
    OffsetDateTime startedAt,
    OffsetDateTime lastActivityAt
) {
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_IDLE = "IDLE";
    public static final String STATUS_CLOSED = "CLOSED";

    public record ConversationTurn(
        String turnId,
        String copilotId,
        String role,
        String message,
        OffsetDateTime timestamp,
        Map<String, Object> metadata
    ) {}
}
