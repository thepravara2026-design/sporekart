package com.sporekart.ai.risk.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

class RiskScoreTest {

    @Test
    void shouldConstructRecord() {
        var id = UUID.randomUUID();
        var assessmentId = UUID.randomUUID();
        var now = Instant.now();
        var categoryScores = Map.of(RiskCategory.SECURITY, 85.0, RiskCategory.COMPLIANCE, 60.0);

        var score = new RiskScore(id, assessmentId, 72.5, RiskLevel.HIGH, categoryScores, 3, now);

        assertNotNull(score);
        assertEquals(id, score.id());
        assertEquals(assessmentId, score.assessmentId());
        assertEquals(72.5, score.overallScore());
        assertEquals(RiskLevel.HIGH, score.riskLevel());
        assertEquals(categoryScores, score.categoryScores());
        assertEquals(3, score.factorCount());
        assertEquals(now, score.calculatedAt());
    }

    @Test
    void shouldHandleLowRiskScore() {
        var score = new RiskScore(UUID.randomUUID(), UUID.randomUUID(), 15.0, RiskLevel.LOW, Map.of(), 1, Instant.now());

        assertEquals(RiskLevel.LOW, score.riskLevel());
        assertTrue(score.overallScore() <= 20);
    }

    @Test
    void shouldHandleCriticalRiskScore() {
        var score = new RiskScore(UUID.randomUUID(), UUID.randomUUID(), 95.0, RiskLevel.CRITICAL, Map.of(), 5, Instant.now());

        assertEquals(RiskLevel.CRITICAL, score.riskLevel());
        assertTrue(score.overallScore() > 70);
    }

    @Test
    void shouldSupportEquality() {
        var id = UUID.randomUUID();
        var now = Instant.now();
        var s1 = new RiskScore(id, UUID.randomUUID(), 50.0, RiskLevel.MEDIUM, Map.of(), 2, now);
        var s2 = new RiskScore(id, UUID.randomUUID(), 50.0, RiskLevel.MEDIUM, Map.of(), 2, now);

        assertEquals(s1, s2);
    }
}
