package com.sporekart.ai.content.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface GenerationMetricsRepository extends JpaRepository<GenerationMetricsEntity, UUID> {
    List<GenerationMetricsEntity> findByContentType(String contentType);
    List<GenerationMetricsEntity> findByRequestId(UUID requestId);
}
