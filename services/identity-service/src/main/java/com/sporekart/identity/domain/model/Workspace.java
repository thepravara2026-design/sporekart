package com.sporekart.identity.domain.model;

import java.time.Instant;

public class Workspace {
    private final String workspaceId;
    private final String name;
    private final String ownerId;
    private final Instant createdAt;

    public Workspace(String workspaceId, String name, String ownerId, Instant createdAt) {
        this.workspaceId = workspaceId;
        this.name = name;
        this.ownerId = ownerId;
        this.createdAt = createdAt;
    }

    public String getWorkspaceId() { return workspaceId; }
    public String getName() { return name; }
    public String getOwnerId() { return ownerId; }
    public Instant getCreatedAt() { return createdAt; }
}
