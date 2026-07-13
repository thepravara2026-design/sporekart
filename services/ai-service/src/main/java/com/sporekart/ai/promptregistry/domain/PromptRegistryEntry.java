package com.sporekart.ai.promptregistry.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PromptRegistryEntry {

    private String promptId;
    private String promptName;
    private String description;
    private String promptText;
    private int version;
    private String owner;
    private List<String> tags = new ArrayList<>();
    private PromptStatus status;
    private String previousVersionId;
    private Map<String, String> metadata = new HashMap<>();
    private Instant createdAt;
    private Instant updatedAt;

    public PromptRegistryEntry() {
    }

    public PromptRegistryEntry(String promptId, String promptName, String description, String promptText,
                               int version, String owner, List<String> tags, PromptStatus status,
                               String previousVersionId, Map<String, String> metadata,
                               Instant createdAt, Instant updatedAt) {
        this.promptId = promptId;
        this.promptName = promptName;
        this.description = description;
        this.promptText = promptText;
        this.version = version;
        this.owner = owner;
        this.tags = tags != null ? tags : new ArrayList<>();
        this.status = status;
        this.previousVersionId = previousVersionId;
        this.metadata = metadata != null ? metadata : new HashMap<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getPromptId() {
        return promptId;
    }

    public void setPromptId(String promptId) {
        this.promptId = promptId;
    }

    public String getPromptName() {
        return promptName;
    }

    public void setPromptName(String promptName) {
        this.promptName = promptName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPromptText() {
        return promptText;
    }

    public void setPromptText(String promptText) {
        this.promptText = promptText;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags != null ? tags : new ArrayList<>();
    }

    public PromptStatus getStatus() {
        return status;
    }

    public void setStatus(PromptStatus status) {
        this.status = status;
    }

    public String getPreviousVersionId() {
        return previousVersionId;
    }

    public void setPreviousVersionId(String previousVersionId) {
        this.previousVersionId = previousVersionId;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata != null ? metadata : new HashMap<>();
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
