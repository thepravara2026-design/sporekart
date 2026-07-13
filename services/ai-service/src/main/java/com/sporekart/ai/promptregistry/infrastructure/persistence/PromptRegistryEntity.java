package com.sporekart.ai.promptregistry.infrastructure.persistence;

import com.sporekart.ai.promptregistry.domain.PromptStatus;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "pr_prompt_registry")
public class PromptRegistryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "prompt_id", nullable = false, unique = true, length = 255)
    private String promptId;

    @Column(name = "prompt_name", nullable = false, length = 255)
    private String promptName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "prompt_text", nullable = false, columnDefinition = "TEXT")
    private String promptText;

    @Column(nullable = false)
    private int version = 1;

    @Column(length = 255)
    private String owner;

    @ElementCollection(fetch = jakarta.persistence.FetchType.EAGER)
    @CollectionTable(name = "pr_prompt_registry_tags",
            joinColumns = @JoinColumn(name = "prompt_registry_id"))
    @Column(name = "tag", length = 255)
    private List<String> tags = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private PromptStatus status = PromptStatus.DRAFT;

    @Column(name = "previous_version_id", length = 255)
    private String previousVersionId;

    @ElementCollection(fetch = jakarta.persistence.FetchType.EAGER)
    @CollectionTable(name = "pr_prompt_registry_metadata",
            joinColumns = @JoinColumn(name = "prompt_registry_id"))
    @MapKeyColumn(name = "meta_key", length = 255)
    @Column(name = "meta_value", columnDefinition = "TEXT")
    private Map<String, String> metadata = new HashMap<>();

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    public PromptRegistryEntity() {
    }

    public PromptRegistryEntity(UUID id, String promptId, String promptName, String description,
                                String promptText, int version, String owner, List<String> tags,
                                PromptStatus status, String previousVersionId,
                                Map<String, String> metadata, Instant createdAt, Instant updatedAt) {
        this.id = id;
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
