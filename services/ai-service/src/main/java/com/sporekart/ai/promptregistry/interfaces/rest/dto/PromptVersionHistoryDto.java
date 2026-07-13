package com.sporekart.ai.promptregistry.interfaces.rest.dto;

import com.sporekart.ai.promptregistry.domain.PromptStatus;

import java.time.Instant;

public class PromptVersionHistoryDto {

    private String id;
    private String promptId;
    private int version;
    private PromptStatus status;
    private String changeSummary;
    private Instant createdAt;

    public PromptVersionHistoryDto() {
    }

    public PromptVersionHistoryDto(String id, String promptId, int version, PromptStatus status,
                                   String changeSummary, Instant createdAt) {
        this.id = id;
        this.promptId = promptId;
        this.version = version;
        this.status = status;
        this.changeSummary = changeSummary;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPromptId() {
        return promptId;
    }

    public void setPromptId(String promptId) {
        this.promptId = promptId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public PromptStatus getStatus() {
        return status;
    }

    public void setStatus(PromptStatus status) {
        this.status = status;
    }

    public String getChangeSummary() {
        return changeSummary;
    }

    public void setChangeSummary(String changeSummary) {
        this.changeSummary = changeSummary;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
