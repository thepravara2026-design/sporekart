package com.sporekart.ai.decision.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.sporekart.ai.decision.api.DecisionReasoningService;
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
class DecisionEvaluatorImplTest {

    @Mock private DecisionReasoningService reasoningService;

    private DecisionEvaluatorImpl evaluator;

    @BeforeEach
    void setUp() {
        evaluator = new DecisionEvaluatorImpl(reasoningService);
    }

    @Test
    void evaluateReturnsResultWithCorrectActionAndStatus() {
        UUID reqId = UUID.randomUUID();
        DecisionRequest request = new DecisionRequest(reqId, "mod", "ALLOW", Map.of(),
            Map.of(), "user", List.of(), List.of(), List.of(), Map.of(), OffsetDateTime.now());
        DecisionContext context = new DecisionContext(UUID.randomUUID(), reqId, "mod", "ALLOW",
            Map.of(), Map.of(), Map.of(), List.of(), List.of(), Map.of(), OffsetDateTime.now());

        when(reasoningService.resolveDecision(anyList(), eq(request))).thenReturn(DecisionAction.ALLOW);
        when(reasoningService.calculateConfidence(anyList(), eq(DecisionAction.ALLOW))).thenReturn(DecisionConfidence.CERTAIN);
        when(reasoningService.buildReasons(eq(DecisionAction.ALLOW), anyList())).thenReturn(
            List.of(new DecisionReason(UUID.randomUUID(), "R01", "msg", "cat", DecisionConfidence.HIGH, Map.of())));

        DecisionResult result = evaluator.evaluate(request, context);

        assertNotNull(result);
        assertEquals(DecisionAction.ALLOW, result.action());
        assertEquals(DecisionStatus.ALLOWED, result.status());
        assertNotNull(result.reasons());
        assertFalse(result.reasons().isEmpty());
    }

    @Test
    void evaluateReturnsDeniedForDenyAction() {
        UUID reqId = UUID.randomUUID();
        DecisionRequest request = new DecisionRequest(reqId, "mod", "DENY", Map.of(),
            Map.of(), "user", List.of(), List.of(), List.of(), Map.of(), OffsetDateTime.now());
        DecisionContext context = new DecisionContext(UUID.randomUUID(), reqId, "mod", "DENY",
            Map.of(), Map.of(), Map.of(), List.of(), List.of(), Map.of(), OffsetDateTime.now());

        when(reasoningService.resolveDecision(anyList(), eq(request))).thenReturn(DecisionAction.DENY);
        when(reasoningService.calculateConfidence(anyList(), eq(DecisionAction.DENY))).thenReturn(DecisionConfidence.HIGH);
        when(reasoningService.buildReasons(eq(DecisionAction.DENY), anyList())).thenReturn(List.of());

        DecisionResult result = evaluator.evaluate(request, context);

        assertEquals(DecisionAction.DENY, result.action());
        assertEquals(DecisionStatus.DENIED, result.status());
    }
}
