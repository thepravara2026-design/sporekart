package com.sporekart.ai.knowledge.interfaces.rest.dto;

public class RegisterSourceRequest {
    private String name;
    private String description;
    private String workspaceId;
    private String owner;
    private String sourceType;
    private String sourceUrl;
    private String language;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getWorkspaceId() { return workspaceId; }
    public void setWorkspaceId(String workspaceId) { this.workspaceId = workspaceId; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }
    public String getSourceUrl() { return sourceUrl; }
    public void setSourceUrl(String sourceUrl) { this.sourceUrl = sourceUrl; }
    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
}
