package com.sporekart.workspace.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public record Workspace(
    String workspaceId,
    String name,
    String ownerId,
    String organizationId,
    String status,
    List<String> enabledCopilotIds,
    List<String> activeSessionIds,
    Map<String, Object> settings,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_INACTIVE = "INACTIVE";
    public static final String STATUS_ARCHIVED = "ARCHIVED";
}
