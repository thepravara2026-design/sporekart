package com.sporekart.ai.decision.application;
import com.sporekart.ai.decision.api.*;
import com.sporekart.ai.decision.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.OffsetDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DecisionEvaluatorImpl implements DecisionEvaluator {
    private final DecisionReasoningService reasoningService;

    @Override
    public DecisionResult evaluate(DecisionRequest request, DecisionContext context) {
        List<DecisionRule> rules = List.of();
        DecisionAction action = evaluateAction(request, rules);
        DecisionConfidence confidence = evaluateConfidence(request, rules, action);
        List<DecisionReason> reasons = generateReasons(request, action, confidence);
        return new DecisionResult(
            UUID.randomUUID(), request.id(), action,
            action == DecisionAction.ALLOW ? DecisionStatus.ALLOWED : DecisionStatus.DENIED,
            confidence, "Decision: " + action.name(), reasons, List.of(), null,
            0L, action == DecisionAction.REQUIRE_APPROVAL, action != DecisionAction.DENY,
            OffsetDateTime.now()
        );
    }

    @Override
    public DecisionAction evaluateAction(DecisionRequest request, List<DecisionRule> rules) {
        return reasoningService.resolveDecision(rules, request);
    }

    @Override
    public DecisionConfidence evaluateConfidence(DecisionRequest request, List<DecisionRule> rules, DecisionAction proposedAction) {
        return reasoningService.calculateConfidence(rules, proposedAction);
    }

    @Override
    public List<DecisionReason> generateReasons(DecisionRequest request, DecisionAction action, DecisionConfidence confidence) {
        return reasoningService.buildReasons(action, List.of());
    }
}
