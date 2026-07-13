package com.sporekart.ai.decision.application;

import static org.junit.jupiter.api.Assertions.*;

import com.sporekart.ai.decision.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

class DecisionReasoningServiceImplTest {

    private DecisionReasoningServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new DecisionReasoningServiceImpl();
    }

    @Test
    void resolveDecisionReturnsAllowForEmptyRules() {
        UUID reqId = UUID.randomUUID();
        DecisionRequest request = new DecisionRequest(reqId, "mod", "ALLOW", Map.of(),
            Map.of(), "user", List.of(), List.of(), List.of(), Map.of(), OffsetDateTime.now());
        assertEquals(DecisionAction.ALLOW, service.resolveDecision(List.of(), request));
    }

    @Test
    void resolveDecisionReturnsAllowForNullRules() {
        UUID reqId = UUID.randomUUID();
        DecisionRequest request = new DecisionRequest(reqId, "mod", "ALLOW", Map.of(),
            Map.of(), "user", List.of(), List.of(), List.of(), Map.of(), OffsetDateTime.now());
        assertEquals(DecisionAction.ALLOW, service.resolveDecision(null, request));
    }

    @Test
    void resolveDecisionReturnsFirstActiveRuleAction() {
        OffsetDateTime now = OffsetDateTime.now();
        DecisionRule rule1 = new DecisionRule(UUID.randomUUID(), "r1", "desc", DecisionAction.ALLOW,
            1, 10, Map.of(), Map.of(), true, now, now);
        DecisionRule rule2 = new DecisionRule(UUID.randomUUID(), "r2", "desc", DecisionAction.DENY,
            2, 20, Map.of(), Map.of(), true, now, now);
        UUID reqId = UUID.randomUUID();
        DecisionRequest request = new DecisionRequest(reqId, "mod", "ALLOW", Map.of(),
            Map.of(), "user", List.of(), List.of(), List.of(), Map.of(), OffsetDateTime.now());
        assertEquals(DecisionAction.DENY, service.resolveDecision(List.of(rule1, rule2), request));
    }

    @Test
    void resolveConflictReturnsDenyForDenyOverridesWhenDenyPresent() {
        assertEquals(DecisionAction.DENY, service.resolveConflict(
            List.of(DecisionAction.ALLOW, DecisionAction.DENY), ConflictStrategy.DENY_OVERRIDES));
    }

    @Test
    void resolveConflictReturnsAllowForDenyOverridesWhenNoDeny() {
        assertEquals(DecisionAction.ALLOW, service.resolveConflict(
            List.of(DecisionAction.ALLOW, DecisionAction.REQUIRE_APPROVAL), ConflictStrategy.DENY_OVERRIDES));
    }

    @Test
    void resolveConflictReturnsAllowForAllowOverridesWhenAllowPresent() {
        assertEquals(DecisionAction.ALLOW, service.resolveConflict(
            List.of(DecisionAction.DENY, DecisionAction.ALLOW), ConflictStrategy.ALLOW_OVERRIDES));
    }

    @Test
    void resolveConflictReturnsDenyForSafeDefault() {
        assertEquals(DecisionAction.DENY, service.resolveConflict(
            List.of(DecisionAction.ALLOW, DecisionAction.DENY), ConflictStrategy.SAFE_DEFAULT));
    }

    @Test
    void resolveConflictReturnsBlockRequestForFailClosed() {
        assertEquals(DecisionAction.BLOCK_REQUEST, service.resolveConflict(
            List.of(DecisionAction.ALLOW), ConflictStrategy.FAIL_CLOSED));
    }

    @Test
    void resolveConflictReturnsLastForMostRecent() {
        assertEquals(DecisionAction.DENY, service.resolveConflict(
            List.of(DecisionAction.ALLOW, DecisionAction.DENY, DecisionAction.ALLOW), ConflictStrategy.MOST_RECENT));
    }

    @Test
    void resolveConflictReturnsFirstForDefaultCase() {
        assertEquals(DecisionAction.REQUIRE_APPROVAL, service.resolveConflict(
            List.of(DecisionAction.REQUIRE_APPROVAL, DecisionAction.DENY), ConflictStrategy.PRIORITY_BASED));
    }

    @Test
    void resolveConflictReturnsAllowForEmptyActions() {
        assertEquals(DecisionAction.ALLOW, service.resolveConflict(List.of(), ConflictStrategy.DENY_OVERRIDES));
    }

    @Test
    void resolveConflictReturnsAllowForNullActions() {
        assertEquals(DecisionAction.ALLOW, service.resolveConflict(null, ConflictStrategy.DENY_OVERRIDES));
    }

    @Test
    void calculateConfidenceReturnsCertainForWeightGE100() {
        OffsetDateTime now = OffsetDateTime.now();
        DecisionRule r1 = new DecisionRule(UUID.randomUUID(), "r1", "desc", DecisionAction.ALLOW,
            1, 60, Map.of(), Map.of(), true, now, now);
        DecisionRule r2 = new DecisionRule(UUID.randomUUID(), "r2", "desc", DecisionAction.ALLOW,
            2, 40, Map.of(), Map.of(), true, now, now);
        assertEquals(DecisionConfidence.CERTAIN, service.calculateConfidence(List.of(r1, r2), DecisionAction.ALLOW));
    }

    @Test
    void calculateConfidenceReturnsHighForWeightGE75() {
        OffsetDateTime now = OffsetDateTime.now();
        DecisionRule r1 = new DecisionRule(UUID.randomUUID(), "r1", "desc", DecisionAction.ALLOW,
            1, 50, Map.of(), Map.of(), true, now, now);
        DecisionRule r2 = new DecisionRule(UUID.randomUUID(), "r2", "desc", DecisionAction.ALLOW,
            2, 25, Map.of(), Map.of(), true, now, now);
        assertEquals(DecisionConfidence.HIGH, service.calculateConfidence(List.of(r1, r2), DecisionAction.ALLOW));
    }

    @Test
    void calculateConfidenceReturnsMediumForWeightGE50() {
        OffsetDateTime now = OffsetDateTime.now();
        DecisionRule r1 = new DecisionRule(UUID.randomUUID(), "r1", "desc", DecisionAction.ALLOW,
            1, 30, Map.of(), Map.of(), true, now, now);
        DecisionRule r2 = new DecisionRule(UUID.randomUUID(), "r2", "desc", DecisionAction.ALLOW,
            2, 20, Map.of(), Map.of(), true, now, now);
        assertEquals(DecisionConfidence.MEDIUM, service.calculateConfidence(List.of(r1, r2), DecisionAction.ALLOW));
    }

    @Test
    void calculateConfidenceReturnsLowForWeightGE25() {
        OffsetDateTime now = OffsetDateTime.now();
        DecisionRule r1 = new DecisionRule(UUID.randomUUID(), "r1", "desc", DecisionAction.ALLOW,
            1, 15, Map.of(), Map.of(), true, now, now);
        DecisionRule r2 = new DecisionRule(UUID.randomUUID(), "r2", "desc", DecisionAction.ALLOW,
            2, 10, Map.of(), Map.of(), true, now, now);
        assertEquals(DecisionConfidence.LOW, service.calculateConfidence(List.of(r1, r2), DecisionAction.ALLOW));
    }

    @Test
    void calculateConfidenceReturnsVeryLowForWeightLT25() {
        OffsetDateTime now = OffsetDateTime.now();
        DecisionRule r1 = new DecisionRule(UUID.randomUUID(), "r1", "desc", DecisionAction.ALLOW,
            1, 10, Map.of(), Map.of(), true, now, now);
        assertEquals(DecisionConfidence.VERY_LOW, service.calculateConfidence(List.of(r1), DecisionAction.ALLOW));
    }

    @Test
    void calculateConfidenceReturnsLowForEmptyRules() {
        assertEquals(DecisionConfidence.LOW, service.calculateConfidence(List.of(), DecisionAction.ALLOW));
    }

    @Test
    void calculateConfidenceReturnsLowForNullRules() {
        assertEquals(DecisionConfidence.LOW, service.calculateConfidence(null, DecisionAction.ALLOW));
    }
}
