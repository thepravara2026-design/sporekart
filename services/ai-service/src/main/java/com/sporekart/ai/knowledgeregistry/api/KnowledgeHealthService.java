package com.sporekart.ai.knowledgeregistry.api;

import com.sporekart.ai.knowledgeregistry.domain.SourceHealthStatus;
import com.sporekart.ai.knowledgeregistry.infrastructure.persistence.KnowledgeSourceHealthEntity;

import java.util.List;

public interface KnowledgeHealthService {

    KnowledgeSourceHealthEntity recordHealth(String sourceId, SourceHealthStatus status);

    SourceHealthStatus getHealth(String sourceId);

    List<KnowledgeSourceHealthEntity> getHealthHistory(String sourceId);
}
