package com.sporekart.ai.decision.infrastructure.decision;

import static org.junit.jupiter.api.Assertions.*;

import com.sporekart.ai.decision.infrastructure.monitoring.DecisionMonitoringService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DecisionMonitoringServiceTest {

    private MeterRegistry registry;
    private DecisionMonitoringService service;

    @BeforeEach
    void setUp() {
        registry = new SimpleMeterRegistry();
        service = new DecisionMonitoringService(registry);
    }

    @Test
    void recordDecisionIncrementsTotal() {
        service.recordDecision(100L);
        assertEquals(1.0, registry.counter("decision.total").count(), 0.001);
        assertEquals(1.0, registry.timer("decision.evaluation.time").count(), 0.001);
    }

    @Test
    void recordAllowedIncrementsAllowed() {
        service.recordAllowed();
        assertEquals(1.0, registry.counter("decision.allowed").count(), 0.001);
    }

    @Test
    void recordDeniedIncrementsDenied() {
        service.recordDenied();
        assertEquals(1.0, registry.counter("decision.denied").count(), 0.001);
    }

    @Test
    void recordEscalatedIncrementsEscalated() {
        service.recordEscalated();
        assertEquals(1.0, registry.counter("decision.escalated").count(), 0.001);
    }

    @Test
    void recordApprovalIncrementsApprovals() {
        service.recordApproval();
        assertEquals(1.0, registry.counter("decision.approvals").count(), 0.001);
    }

    @Test
    void recordConflictIncrementsConflicts() {
        service.recordConflict();
        assertEquals(1.0, registry.counter("decision.conflicts").count(), 0.001);
    }

    @Test
    void recordReplayIncrementsReplays() {
        service.recordReplay();
        assertEquals(1.0, registry.counter("decision.replays").count(), 0.001);
    }
}
