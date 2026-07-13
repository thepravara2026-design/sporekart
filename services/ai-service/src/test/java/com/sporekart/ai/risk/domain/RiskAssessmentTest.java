package com.sporekart.ai.risk.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

class RiskAssessmentTest {

    @Test
    void shouldConstructRecord() {
        var id = UUID.randomUUID();
        var now = Instant.now();
        var context = Map.of("key", "value");

        var assessment = new RiskAssessment(id, "prompt", "generate", RiskAssessmentStatus.PENDING, context, null, now, null);

        assertNotNull(assessment);
        assertEquals(id, assessment.id());
        assertEquals("prompt", assessment.module());
        assertEquals("generate", assessment.action());
        assertEquals(RiskAssessmentStatus.PENDING, assessment.status());
        assertEquals(context, assessment.context());
        assertNull(assessment.reviewerId());
        assertEquals(now, assessment.assessedAt());
        assertNull(assessment.completedAt());
    }

    @Test
    void shouldHandleCompletedAssessment() {
        var id = UUID.randomUUID();
        var assessedAt = Instant.now();
        var completedAt = Instant.now().plusSeconds(60);
        var context = Map.<String, Object>of();

        var assessment = new RiskAssessment(id, "search", "query", RiskAssessmentStatus.COMPLETED, context, UUID.randomUUID(), assessedAt, completedAt);

        assertEquals(RiskAssessmentStatus.COMPLETED, assessment.status());
        assertNotNull(assessment.reviewerId());
        assertNotNull(assessment.completedAt());
    }

    @Test
    void shouldSupportEquality() {
        var id = UUID.randomUUID();
        var now = Instant.now();
        var a1 = new RiskAssessment(id, "m", "a", RiskAssessmentStatus.PENDING, Map.of(), null, now, null);
        var a2 = new RiskAssessment(id, "m", "a", RiskAssessmentStatus.PENDING, Map.of(), null, now, null);

        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    void shouldSupportToString() {
        var assessment = new RiskAssessment(UUID.randomUUID(), "m", "a", RiskAssessmentStatus.PENDING, Map.of(), null, Instant.now(), null);

        assertNotNull(assessment.toString());
        assertTrue(assessment.toString().contains("RiskAssessment"));
    }
}
