package com.sporekart.workflow.domain.engine;

import com.sporekart.workflow.domain.model.WorkflowAudit;
import com.sporekart.workflow.domain.repository.WorkflowRepositoryPort;

import java.util.List;
import java.util.Map;

public class WorkflowAuditEngine {
    private final WorkflowRepositoryPort repository;

    public WorkflowAuditEngine(WorkflowRepositoryPort repository) {
        this.repository = repository;
    }

    public List<WorkflowAudit> getAuditTrail(String instanceId) {
        return repository.findAuditsByInstanceId(instanceId);
    }

    public List<WorkflowAudit> getAllAudits() {
        return repository.findAllAudits();
    }

    public Map<String, Object> getAuditSummary() {
        var audits = repository.findAllAudits();
        var successCount = audits.stream().filter(a -> "SUCCESS".equals(a.outcome())).count();
        var failureCount = audits.stream().filter(a -> "FAILURE".equals(a.outcome())).count();

        return Map.of(
            "totalAudits", audits.size(),
            "successful", successCount,
            "failed", failureCount,
            "uniqueWorkflows", audits.stream().map(WorkflowAudit::instanceId).distinct().count()
        );
    }
}
