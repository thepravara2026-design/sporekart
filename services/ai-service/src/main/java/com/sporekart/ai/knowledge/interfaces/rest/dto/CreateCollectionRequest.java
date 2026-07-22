package com.sporekart.ai.knowledge.interfaces.rest.dto;

public class CreateCollectionRequest {
    private String name;
    private String description;
    private String workspaceId;
    private String owner;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getWorkspaceId() { return workspaceId; }
    public void setWorkspaceId(String workspaceId) { this.workspaceId = workspaceId; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
}
