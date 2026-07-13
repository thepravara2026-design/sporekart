package com.sporekart.ai.automation.infrastructure.persistence;

import com.sporekart.ai.automation.domain.WorkflowHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WorkflowHistoryRepository extends JpaRepository<WorkflowHistory, UUID> {
    List<WorkflowHistory> findByExecutionId(UUID executionId);
}
