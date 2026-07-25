package com.sporekart.workflow.application.sdk;

import com.sporekart.workflow.application.service.WorkflowTelemetryService;
import com.sporekart.workflow.domain.repository.WorkflowRepositoryPort;
import com.sporekart.workflow.infrastructure.persistence.InMemoryWorkflowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorkflowTelemetryTest {
    private WorkflowTelemetry telemetry;

    @BeforeEach
    void setUp() {
        var repository = new InMemoryWorkflowRepository();
        var telemetryService = new WorkflowTelemetryService(repository);
        telemetry = new WorkflowTelemetry(telemetryService);
    }

    @Test
    void shouldGetMetrics() {
        var metrics = telemetry.getMetrics();
        assertNotNull(metrics);
    }

    @Test
    void shouldGetHealth() {
        var health = telemetry.getHealth();
        assertNotNull(health.get("status"));
    }

    @Test
    void shouldGetHistory() {
        telemetry.getMetrics();
        var history = telemetry.getHistory();
        assertNotNull(history.get("history"));
    }
}
