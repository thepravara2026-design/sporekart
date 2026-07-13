package com.sporekart.ai.decision.application;

import static org.junit.jupiter.api.Assertions.*;

import com.sporekart.ai.decision.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

class DecisionExplanationServiceImplTest {

    private DecisionExplanationServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new DecisionExplanationServiceImpl();
    }

    @Test
    void generateExplanationReturnsNonNull() {
        UUID reqId = UUID.randomUUID();
        UUID resId = UUID.randomUUID();
        DecisionRequest request = new DecisionRequest(reqId, "mod", "ALLOW", Map.of(),
            Map.of(), "user", List.of(), List.of("p1"), List.of("r1"), Map.of(), OffsetDateTime.now());
        DecisionResult result = new DecisionResult(resId, reqId, DecisionAction.ALLOW, DecisionStatus.ALLOWED,
            DecisionConfidence.HIGH, "summary", List.of(), List.of(), null, 50L, false, true, OffsetDateTime.now());

        DecisionExplanation explanation = service.generateExplanation(result, request);
        assertNotNull(explanation);
        assertNotNull(explanation.id());
        assertEquals(resId, explanation.decisionId());
    }

    @Test
    void generateSummaryContainsActionAndStatus() {
        DecisionResult result = new DecisionResult(UUID.randomUUID(), UUID.randomUUID(),
            DecisionAction.DENY, DecisionStatus.DENIED, DecisionConfidence.LOW, "summary",
            List.of(), List.of(), null, 100L, false, true, OffsetDateTime.now());
        String summary = service.generateSummary(result);
        assertTrue(summary.contains("DENY"));
        assertTrue(summary.contains("DENIED"));
        assertTrue(summary.contains("LOW"));
        assertTrue(summary.contains("100ms"));
    }

    @Test
    void gatherEvidenceReturnsListSizedToReasonsCount() {
        UUID reqId = UUID.randomUUID();
        UUID resId = UUID.randomUUID();
        DecisionReason reason1 = new DecisionReason(UUID.randomUUID(), "R01", "msg1", "cat1", DecisionConfidence.HIGH, Map.of());
        DecisionReason reason2 = new DecisionReason(UUID.randomUUID(), "R02", "msg2", "cat2", DecisionConfidence.MEDIUM, Map.of());
        DecisionRequest request = new DecisionRequest(reqId, "mod", "ALLOW", Map.of(),
            Map.of(), "user", List.of(), List.of(), List.of(), Map.of(), OffsetDateTime.now());
        DecisionResult result = new DecisionResult(resId, reqId, DecisionAction.ALLOW, DecisionStatus.ALLOWED,
            DecisionConfidence.HIGH, "summary", List.of(reason1, reason2), List.of(), null, 0L, false, true, OffsetDateTime.now());

        List<DecisionEvidence> evidence = service.gatherEvidence(result, request);
        assertEquals(2, evidence.size());
    }
}
