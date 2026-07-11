package com.sporekart.ai.prompt.infrastructure.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PromptExecutionLogRepository extends JpaRepository<PromptExecutionLogEntity, UUID> {
    List<PromptExecutionLogEntity> findByTemplateIdOrderByExecutedAtDesc(UUID templateId);
    Page<PromptExecutionLogEntity> findAllByOrderByExecutedAtDesc(Pageable pageable);
    long countBySuccessTrue();
    long countBySuccessFalse();
}
