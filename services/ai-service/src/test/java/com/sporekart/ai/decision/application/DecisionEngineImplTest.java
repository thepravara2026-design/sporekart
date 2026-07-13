package com.sporekart.ai.decision.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.sporekart.ai.decision.api.*;
import com.sporekart.ai.decision.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class DecisionEngineImplTest {

    @Mock private DecisionResolver resolver;
    @Mock private DecisionEvaluator evaluator;
    @Mock private DecisionReasoningService reasoningService;
    @Mock private DecisionExplanationService explanationService;
    @Mock private DecisionAuditService auditService;
    @Mock private DecisionMetricsService metricsService;

    private DecisionEngineImpl engine;

    @BeforeEach
    void setUp() {
        engine = new DecisionEngineImpl(resolver, evaluator, reasoningService, explanationService, auditService, metricsService);
    }

    @Test
    void evaluateReturnsResultWithStatusAllowedWhenNoConflictingRules() {
        UUID reqId = UUID.randomUUID();
        DecisionRequest request = new DecisionRequest(reqId, "mod", "ALLOW", Map.of(),
            Map.of(), "user", List.of(), List.of(), List.of(), Map.of(), OffsetDateTime.now());
        DecisionContext context = new DecisionContext(UUID.randomUUID(), reqId, "mod", "ALLOW",
            Map.of(), Map.of(), Map.of(), List.of(), List.of(), Map.of(), OffsetDateTime.now());
        DecisionResult evalResult = new DecisionResult(UUID.randomUUID(), reqId, DecisionAction.ALLOW,
            DecisionStatus.ALLOWED, DecisionConfidence.HIGH, "summary", List.of(), List.of(),
            null, 0L, false, true, OffsetDateTime.now());
        DecisionExplanation explanation = new DecisionExplanation(UUID.randomUUID(), evalResult.id(),
            "summary", List.of(), List.of(), DecisionConfidence.HIGH, Map.of(), List.of(),
            "ALLOW", "audit", "text");

        when(resolver.resolveContext(request)).thenReturn(context);
        when(evaluator.evaluate(request, context)).thenReturn(evalResult);
        when(explanationService.generateExplanation(evalResult, request)).thenReturn(explanation);

        DecisionResult result = engine.evaluate(request);

        assertNotNull(result);
        assertEquals(DecisionStatus.ALLOWED, result.status());
        assertEquals(DecisionAction.ALLOW, result.action());
        verify(auditService).recordAudit(any(DecisionAudit.class));
        verify(metricsService).recordDecision(eq("ALLOW"), any(DecisionConfidence.class), anyLong());
    }

    @Test
    void replayDoesNotThrow() {
        assertDoesNotThrow(() -> engine.replay(UUID.randomUUID()));
        verify(metricsService).recordReplay();
    }

    @Test
    void isAllowedReturnsTrueForAllowedDecisions() {
        UUID reqId = UUID.randomUUID();
        DecisionRequest request = new DecisionRequest(reqId, "mod", "ALLOW", Map.of(),
            Map.of(), "user", List.of(), List.of(), List.of(), Map.of(), OffsetDateTime.now());
        DecisionContext context = new DecisionContext(UUID.randomUUID(), reqId, "mod", "ALLOW",
            Map.of(), Map.of(), Map.of(), List.of(), List.of(), Map.of(), OffsetDateTime.now());
        DecisionResult evalResult = new DecisionResult(UUID.randomUUID(), reqId, DecisionAction.ALLOW,
            DecisionStatus.ALLOWED, DecisionConfidence.HIGH, "summary", List.of(), List.of(),
            null, 0L, false, true, OffsetDateTime.now());
        DecisionExplanation explanation = new DecisionExplanation(UUID.randomUUID(), evalResult.id(),
            "summary", List.of(), List.of(), DecisionConfidence.HIGH, Map.of(), List.of(),
            "ALLOW", "audit", "text");

        when(resolver.resolveContext(request)).thenReturn(context);
        when(evaluator.evaluate(request, context)).thenReturn(evalResult);
        when(explanationService.generateExplanation(evalResult, request)).thenReturn(explanation);

        assertTrue(engine.isAllowed(request));
    }
}
