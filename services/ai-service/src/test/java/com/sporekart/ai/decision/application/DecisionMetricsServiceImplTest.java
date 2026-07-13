package com.sporekart.ai.decision.application;

import static org.junit.jupiter.api.Assertions.*;

import com.sporekart.ai.decision.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DecisionMetricsServiceImplTest {

    private DecisionMetricsServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new DecisionMetricsServiceImpl();
    }

    @Test
    void recordDecisionIncrementsCounts() {
        service.recordDecision("ALLOW", DecisionConfidence.HIGH, 100L);
        service.recordDecision("DENY", DecisionConfidence.LOW, 50L);
        service.recordDecision("ESCALATE_TO_ADMIN", DecisionConfidence.MEDIUM, 200L);
        service.recordDecision("REQUIRE_APPROVAL", DecisionConfidence.CERTAIN, 30L);

        DecisionStatistics stats = service.getStatistics();
        assertEquals(4, stats.totalDecisions());
        assertEquals(1, stats.allowedCount());
        assertEquals(1, stats.deniedCount());
        assertEquals(1, stats.escalatedCount());
        assertEquals(1, stats.approvalCount());
    }

    @Test
    void getStatisticsReturnsNonNull() {
        service.recordDecision("ALLOW", DecisionConfidence.HIGH, 10L);
        DecisionStatistics stats = service.getStatistics();
        assertNotNull(stats);
        assertNotNull(stats.id());
        assertNotNull(stats.calculatedAt());
    }

    @Test
    void getDetailedMetricsContainsAllKeys() {
        service.recordDecision("ALLOW", DecisionConfidence.HIGH, 10L);
        service.recordConflict("DENY_OVERRIDES");
        service.recordReplay();

        var metrics = service.getDetailedMetrics();
        assertTrue(metrics.containsKey("totalDecisions"));
        assertTrue(metrics.containsKey("allowed"));
        assertTrue(metrics.containsKey("denied"));
        assertTrue(metrics.containsKey("escalated"));
        assertTrue(metrics.containsKey("approvals"));
        assertTrue(metrics.containsKey("conflicts"));
        assertTrue(metrics.containsKey("replays"));
        assertTrue(metrics.containsKey("avgConfidence"));
        assertTrue(metrics.containsKey("avgLatencyMs"));
        assertEquals(1L, metrics.get("totalDecisions"));
        assertEquals(1L, metrics.get("allowed"));
        assertEquals(1L, metrics.get("conflicts"));
        assertEquals(1L, metrics.get("replays"));
    }
}
