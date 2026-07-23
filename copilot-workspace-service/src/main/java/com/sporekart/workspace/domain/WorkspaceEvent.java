package com.sporekart.workspace.domain;

import java.time.OffsetDateTime;
import java.util.Map;

public record WorkspaceEvent(
    String eventId,
    String eventType,
    String source,
    String sessionId,
    String copilotId,
    Map<String, Object> payload,
    OffsetDateTime timestamp
) {
    public static final String TYPE_CONVERSATION_STARTED = "CONVERSATION_STARTED";
    public static final String TYPE_CONVERSATION_FINISHED = "CONVERSATION_FINISHED";
    public static final String TYPE_TOOL_EXECUTED = "TOOL_EXECUTED";
    public static final String TYPE_KNOWLEDGE_RETRIEVED = "KNOWLEDGE_RETRIEVED";
    public static final String TYPE_PROMPT_EXECUTED = "PROMPT_EXECUTED";
    public static final String TYPE_COPILOT_SWITCHED = "COPILOT_SWITCHED";
    public static final String TYPE_WORKSPACE_CHANGED = "WORKSPACE_CHANGED";
    public static final String TYPE_SESSION_CLOSED = "SESSION_CLOSED";
    public static final String TYPE_HANDOFF_INITIATED = "HANDOFF_INITIATED";
    public static final String TYPE_HANDOFF_COMPLETED = "HANDOFF_COMPLETED";
    public static final String TYPE_COLLABORATION_STARTED = "COLLABORATION_STARTED";
    public static final String TYPE_COLLABORATION_COMPLETED = "COLLABORATION_COMPLETED";
}
