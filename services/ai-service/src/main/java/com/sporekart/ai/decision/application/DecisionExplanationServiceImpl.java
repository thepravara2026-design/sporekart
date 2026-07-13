package com.sporekart.ai.decision.application;
import com.sporekart.ai.decision.api.DecisionExplanationService;
import com.sporekart.ai.decision.domain.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class DecisionExplanationServiceImpl implements DecisionExplanationService {

    @Override
    public DecisionExplanation generateExplanation(DecisionResult result, DecisionRequest request) {
        return new DecisionExplanation(
            UUID.randomUUID(), result.id(), generateSummary(result),
            request.matchedPolicyIds(), request.matchedRuleIds(),
            result.confidence(), Map.of("action", result.action().name(), "status", result.status().name()),
            gatherEvidence(result, request), result.action().name(),
            "audit:" + result.id(), generateExplanationText(result)
        );
    }

    @Override
    public String generateSummary(DecisionResult result) {
        return String.format("Decision: %s | Status: %s | Confidence: %s | Time: %dms",
            result.action(), result.status(), result.confidence(), result.processingTimeMs());
    }

    @Override
    public List<DecisionEvidence> gatherEvidence(DecisionResult result, DecisionRequest request) {
        return result.reasons().stream()
            .map(r -> new DecisionEvidence(
                UUID.randomUUID(), "reasoning", "reason",
                r.message(), r.confidence().ordinal() / 5.0, Map.of("code", r.code())
            )).toList();
    }

    @Override
    public String generateExplanationText(DecisionResult result) {
        return String.format("The request was %s with %s confidence. %d reasons were evaluated.",
            result.action(), result.confidence(), result.reasons().size());
    }

    @Override
    public Map<String, Object> buildAuditMetadata(DecisionResult result, DecisionRequest request) {
        return Map.of(
            "decisionId", result.id(), "requestId", request.id(),
            "action", result.action().name(), "confidence", result.confidence().name(),
            "processingTimeMs", result.processingTimeMs()
        );
    }
}
