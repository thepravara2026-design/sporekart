package com.sporekart.ai.policy.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record EvaluationResult(
    UUID requestId, PolicyDecision finalDecision,
    List<PolicyEvaluation> evaluations, List<PolicyViolation> violations,
    long totalEvaluationTimeMs, boolean passed,
    OffsetDateTime timestamp) {}
