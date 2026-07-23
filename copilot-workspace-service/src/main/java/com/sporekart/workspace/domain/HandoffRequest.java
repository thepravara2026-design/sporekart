package com.sporekart.workspace.domain;

import java.time.OffsetDateTime;

public record HandoffRequest(
    String handoffId,
    String sessionId,
    String fromCopilotId,
    String toCopilotId,
    String reason,
    String contextSummary,
    String userMessage,
    String status,
    OffsetDateTime createdAt
) {
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_ACCEPTED = "ACCEPTED";
    public static final String STATUS_REJECTED = "REJECTED";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_FAILED = "FAILED";
}
