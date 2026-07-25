package com.sporekart.workflow.application.service;

import com.sporekart.workflow.domain.repository.WorkflowRepositoryPort;
import com.sporekart.workflow.infrastructure.persistence.InMemoryWorkflowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorkflowTelemetryServiceTest {
    private WorkflowTelemetryService telemetry;
    private WorkflowRepositoryPort repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryWorkflowRepository();
        telemetry = new WorkflowTelemetryService(repository);
    }

    @Test
    void shouldGetTelemetry() {
        var metrics = telemetry.getTelemetry();
        assertNotNull(metrics);
        assertEquals(0, metrics.get("totalDefinitions"));
    }

    @Test
    void shouldGetWorkflowHealth() {
        var health = telemetry.getWorkflowHealth();
        assertNotNull(health.get("status"));
        assertEquals("HEALTHY", health.get("status"));
    }

    @Test
    void shouldGetTelemetryHistory() {
        telemetry.getTelemetry();
        var history = telemetry.getTelemetryHistory();
        assertNotNull(history.get("history"));
    }
}
