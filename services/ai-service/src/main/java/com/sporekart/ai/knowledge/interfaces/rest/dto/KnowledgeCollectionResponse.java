package com.sporekart.ai.knowledge.interfaces.rest.dto;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class KnowledgeCollectionResponse {
    private String id;
    private String name;
    private String description;
    private String workspaceId;
    private String owner;
    private String defaultPermission;
    private List<String> tags;
    private Map<String, String> policy;
    private Instant createdAt;
    private Instant updatedAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getWorkspaceId() { return workspaceId; }
    public void setWorkspaceId(String workspaceId) { this.workspaceId = workspaceId; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public String getDefaultPermission() { return defaultPermission; }
    public void setDefaultPermission(String defaultPermission) { this.defaultPermission = defaultPermission; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    public Map<String, String> getPolicy() { return policy; }
    public void setPolicy(Map<String, String> policy) { this.policy = policy; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
