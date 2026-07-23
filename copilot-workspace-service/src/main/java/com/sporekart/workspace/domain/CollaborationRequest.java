package com.sporekart.workspace.domain;

import java.time.OffsetDateTime;
import java.util.List;

public record CollaborationRequest(
    String collaborationId,
    String primaryCopilotId,
    List<String> collaboratingCopilotIds,
    String query,
    List<String> partialResponses,
    String status,
    OffsetDateTime createdAt
) {
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_FAILED = "FAILED";
}
