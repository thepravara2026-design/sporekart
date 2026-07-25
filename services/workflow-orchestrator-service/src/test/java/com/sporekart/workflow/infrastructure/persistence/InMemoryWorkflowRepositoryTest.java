package com.sporekart.workflow.infrastructure.persistence;

import com.sporekart.workflow.domain.model.*;
import com.sporekart.workflow.domain.repository.WorkflowRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryWorkflowRepositoryTest {
    private WorkflowRepositoryPort repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryWorkflowRepository();
    }

    @Test
    void shouldSaveAndFindDefinition() {
        var def = WorkflowDefinition.create("Test", "Desc", WorkflowType.ORDER, "Domain", "owner", "1.0.0", List.of(), Map.of(), Map.of());
        repository.saveDefinition(def);
        assertTrue(repository.findDefinitionById(def.id()).isPresent());
    }

    @Test
    void shouldReturnEmptyForMissingDefinition() {
        assertTrue(repository.findDefinitionById("missing").isEmpty());
    }

    @Test
    void shouldFindDefinitionsByType() {
        repository.saveDefinition(WorkflowDefinition.create("O1", "", WorkflowType.ORDER, "D", "o", "1", List.of(), Map.of(), Map.of()));
        repository.saveDefinition(WorkflowDefinition.create("O2", "", WorkflowType.ORDER, "D", "o", "1", List.of(), Map.of(), Map.of()));
        repository.saveDefinition(WorkflowDefinition.create("T1", "", WorkflowType.TRAINING, "D", "o", "1", List.of(), Map.of(), Map.of()));
        assertEquals(2, repository.findDefinitionsByType(WorkflowType.ORDER).size());
    }

    @Test
    void shouldFindActiveDefinitions() {
        var active = WorkflowDefinition.create("A1", "", WorkflowType.ORDER, "D", "o", "1", List.of(), Map.of(), Map.of());
        var inactive = WorkflowDefinition.create("I1", "", WorkflowType.ORDER, "D", "o", "1", List.of(), Map.of(), Map.of()).withStatus(WorkflowStatus.INACTIVE);
        repository.saveDefinition(active);
        repository.saveDefinition(inactive);
        assertEquals(1, repository.findActiveDefinitions().size());
    }

    @Test
    void shouldSaveAndFindInstance() {
        var instance = WorkflowInstance.create("def-1", "Test", WorkflowType.ORDER, "D", "o", "sys", false, false, 3);
        repository.saveInstance(instance);
        assertTrue(repository.findInstanceById(instance.id()).isPresent());
    }

    @Test
    void shouldFindInstancesByState() {
        var running = WorkflowInstance.create("def-1", "R1", WorkflowType.ORDER, "D", "o", "sys", false, false, 3).withState(WorkflowState.RUNNING);
        var paused = WorkflowInstance.create("def-1", "P1", WorkflowType.ORDER, "D", "o", "sys", false, false, 3).withState(WorkflowState.PAUSED);
        repository.saveInstance(running);
        repository.saveInstance(paused);
        assertEquals(1, repository.findInstancesByState(WorkflowState.RUNNING).size());
    }

    @Test
    void shouldSaveAndFindQueueItem() {
        var queue = WorkflowQueue.create("inst-1", "Test", WorkflowType.ORDER, 5, "STANDARD", Map.of());
        repository.saveQueueItem(queue);
        assertTrue(repository.findQueueItemById(queue.id()).isPresent());
    }

    @Test
    void shouldFindQueueByType() {
        repository.saveQueueItem(WorkflowQueue.create("i1", "T1", WorkflowType.ORDER, 5, "STANDARD", Map.of()));
        repository.saveQueueItem(WorkflowQueue.create("i2", "T2", WorkflowType.ORDER, 3, "PRIORITY", Map.of()));
        assertEquals(1, repository.findQueueByType("PRIORITY").size());
    }

    @Test
    void shouldSaveAndFindSimulation() {
        var sim = WorkflowSimulation.create("inst-1", "Test", WorkflowType.ORDER, true, false, true, List.of(), List.of(), List.of(), Map.of());
        repository.saveSimulation(sim);
        assertTrue(repository.findSimulationById(sim.id()).isPresent());
    }

    @Test
    void shouldSaveAndFindAudit() {
        var audit = WorkflowAudit.create("inst-1", "TEST", "user", "test", "SUCCESS", Map.of());
        repository.saveAudit(audit);
        assertTrue(repository.findAuditById(audit.id()).isPresent());
    }

    @Test
    void shouldFindAuditsByInstanceId() {
        repository.saveAudit(WorkflowAudit.create("inst-1", "A1", "u", "d", "SUCCESS", Map.of()));
        repository.saveAudit(WorkflowAudit.create("inst-1", "A2", "u", "d", "SUCCESS", Map.of()));
        repository.saveAudit(WorkflowAudit.create("inst-2", "A3", "u", "d", "SUCCESS", Map.of()));
        assertEquals(2, repository.findAuditsByInstanceId("inst-1").size());
    }

    @Test
    void shouldSaveAndFindApproval() {
        var approval = WorkflowApproval.create("inst-1", "Test", "user1", "user2", Map.of());
        repository.saveApproval(approval);
        assertTrue(repository.findApprovalById(approval.id()).isPresent());
    }

    @Test
    void shouldFindApprovalsByStatus() {
        var pending = WorkflowApproval.create("i1", "T1", "u1", "u2", Map.of());
        repository.saveApproval(pending);
        assertEquals(1, repository.findApprovalsByStatus("PENDING").size());
    }

    @Test
    void shouldDeleteDefinition() {
        var def = WorkflowDefinition.create("Test", "Desc", WorkflowType.ORDER, "D", "o", "1", List.of(), Map.of(), Map.of());
        repository.saveDefinition(def);
        repository.deleteDefinition(def.id());
        assertTrue(repository.findDefinitionById(def.id()).isEmpty());
    }

    @Test
    void shouldReturnHealth() {
        var health = repository.getHealthMetrics();
        assertNotNull(health);
        assertEquals("HEALTHY", health.get("status"));
    }
}
