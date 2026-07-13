package com.sporekart.ai.knowledgeregistry.interfaces.rest.dto;

import com.sporekart.ai.knowledgeregistry.domain.KnowledgeSourceType;
import com.sporekart.ai.knowledgeregistry.domain.RefreshPolicy;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.Map;

public class KnowledgeSourceRequestDto {

    private String sourceId;

    @NotBlank
    private String sourceName;

    @NotNull
    private KnowledgeSourceType sourceType;

    private String description;
    private String owner;
    private Map<String, String> metadata = new HashMap<>();
    private RefreshPolicy refreshPolicy;
    private String refreshCron;

    public KnowledgeSourceRequestDto() {
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public KnowledgeSourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(KnowledgeSourceType sourceType) {
        this.sourceType = sourceType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public RefreshPolicy getRefreshPolicy() {
        return refreshPolicy;
    }

    public void setRefreshPolicy(RefreshPolicy refreshPolicy) {
        this.refreshPolicy = refreshPolicy;
    }

    public String getRefreshCron() {
        return refreshCron;
    }

    public void setRefreshCron(String refreshCron) {
        this.refreshCron = refreshCron;
    }
}
