package com.sporekart.ai.policy.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record PolicyEvaluation(
    UUID id, UUID requestId, UUID policyId, PolicyDecision decision,
    List<PolicyViolation> violations, Map<String, Object> context,
    long evaluationTimeMs, int rulesEvaluated, int rulesPassed, int rulesFailed,
    boolean matched, OffsetDateTime timestamp) {}
