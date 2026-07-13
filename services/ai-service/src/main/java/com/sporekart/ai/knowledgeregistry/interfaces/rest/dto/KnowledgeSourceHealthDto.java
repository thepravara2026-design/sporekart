package com.sporekart.ai.knowledgeregistry.interfaces.rest.dto;

import com.sporekart.ai.knowledgeregistry.domain.SourceHealthStatus;
import com.sporekart.ai.knowledgeregistry.infrastructure.persistence.KnowledgeSourceHealthEntity;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class KnowledgeSourceHealthDto {

    private Long id;
    private String sourceId;

    @NotNull
    private SourceHealthStatus status;

    private Instant checkedAt;

    public KnowledgeSourceHealthDto() {
    }

    public static KnowledgeSourceHealthDto from(KnowledgeSourceHealthEntity entity) {
        KnowledgeSourceHealthDto dto = new KnowledgeSourceHealthDto();
        dto.setId(entity.getId());
        dto.setSourceId(entity.getSourceId());
        dto.setStatus(entity.getStatus());
        dto.setCheckedAt(entity.getCheckedAt());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public SourceHealthStatus getStatus() {
        return status;
    }

    public void setStatus(SourceHealthStatus status) {
        this.status = status;
    }

    public Instant getCheckedAt() {
        return checkedAt;
    }

    public void setCheckedAt(Instant checkedAt) {
        this.checkedAt = checkedAt;
    }
}
