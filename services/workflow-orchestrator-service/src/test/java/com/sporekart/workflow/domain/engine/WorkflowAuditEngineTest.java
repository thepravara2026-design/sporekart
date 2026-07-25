package com.sporekart.workflow.domain.engine;

import com.sporekart.workflow.domain.model.WorkflowAudit;
import com.sporekart.workflow.domain.repository.WorkflowRepositoryPort;
import com.sporekart.workflow.infrastructure.persistence.InMemoryWorkflowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WorkflowAuditEngineTest {
    private WorkflowAuditEngine auditEngine;
    private WorkflowRepositoryPort repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryWorkflowRepository();
        auditEngine = new WorkflowAuditEngine(repository);
    }

    @Test
    void shouldGetAllAudits() {
        repository.saveAudit(WorkflowAudit.create("inst-1", "TEST", "user", "test audit", "SUCCESS", Map.of()));
        var audits = auditEngine.getAllAudits();
        assertFalse(audits.isEmpty());
    }

    @Test
    void shouldGetAuditTrail() {
        repository.saveAudit(WorkflowAudit.create("inst-1", "ACTION_1", "user", "first", "SUCCESS", Map.of()));
        repository.saveAudit(WorkflowAudit.create("inst-1", "ACTION_2", "user", "second", "SUCCESS", Map.of()));
        var trail = auditEngine.getAuditTrail("inst-1");
        assertEquals(2, trail.size());
    }

    @Test
    void shouldReturnEmptyTrailForUnknownInstance() {
        var trail = auditEngine.getAuditTrail("unknown");
        assertTrue(trail.isEmpty());
    }

    @Test
    void shouldGetAuditSummary() {
        repository.saveAudit(WorkflowAudit.create("inst-1", "TEST", "user", "test", "SUCCESS", Map.of()));
        var summary = auditEngine.getAuditSummary();
        assertEquals(1, summary.get("totalAudits"));
        assertEquals(1L, summary.get("successful"));
    }
}
