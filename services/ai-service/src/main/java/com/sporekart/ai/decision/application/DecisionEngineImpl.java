package com.sporekart.ai.decision.application;
import com.sporekart.ai.decision.api.*;
import com.sporekart.ai.decision.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DecisionEngineImpl implements DecisionEngine {
    private final DecisionResolver resolver;
    private final DecisionEvaluator evaluator;
    private final DecisionReasoningService reasoningService;
    private final DecisionExplanationService explanationService;
    private final DecisionAuditService auditService;
    private final DecisionMetricsService metricsService;

    @Override
    public DecisionResult evaluate(DecisionRequest request) {
        long start = System.currentTimeMillis();
        DecisionContext context = resolver.resolveContext(request);
        DecisionResult result = evaluator.evaluate(request, context);
        DecisionExplanation explanation = explanationService.generateExplanation(result, request);
        long elapsed = System.currentTimeMillis() - start;
        DecisionResult finalResult = new DecisionResult(
            result.id(), result.requestId(), result.action(), result.status(),
            result.confidence(), result.summary(), result.reasons(), result.evidence(),
            explanation, elapsed, result.requiresApproval(), result.overrideable(),
            OffsetDateTime.now()
        );
        auditService.recordAudit(new DecisionAudit(
            UUID.randomUUID(), request.id(), finalResult.id(), finalResult.action(),
            finalResult.status(), finalResult.confidence(), finalResult.reasons(),
            Map.of("module", request.module()), request.userId(), elapsed,
            finalResult.status() == DecisionStatus.ALLOWED, OffsetDateTime.now(), OffsetDateTime.now()
        ));
        metricsService.recordDecision(finalResult.action().name(), finalResult.confidence(), elapsed);
        log.info("Decision {} for request {}: {} ({}ms)", finalResult.id(), request.id(), finalResult.action(), elapsed);
        return finalResult;
    }

    @Override
    public DecisionResult evaluateWithContext(DecisionRequest request, DecisionContext context) {
        return evaluate(request);
    }

    @Override
    public DecisionResult replay(UUID originalRequestId) {
        log.info("Replay requested for decision {}", originalRequestId);
        metricsService.recordReplay();
        return null;
    }

    @Override
    public DecisionAction resolveConflict(List<DecisionResult> conflictingResults, ConflictStrategy strategy) {
        return reasoningService.resolveConflict(
            conflictingResults.stream().map(DecisionResult::action).toList(), strategy);
    }

    @Override
    public boolean isAllowed(DecisionRequest request) {
        return evaluate(request).status() == DecisionStatus.ALLOWED;
    }
}
