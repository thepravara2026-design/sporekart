package com.sporekart.ai.decision.domain;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record DecisionExplanation(
    UUID id, UUID decisionId, String summary, List<String> matchedPolicies,
    List<String> triggeredRules, DecisionConfidence confidence,
    Map<String, Object> reasoning, List<DecisionEvidence> evidenceList,
    String recommendedAction, String auditMetadata, String explanationText) {}
