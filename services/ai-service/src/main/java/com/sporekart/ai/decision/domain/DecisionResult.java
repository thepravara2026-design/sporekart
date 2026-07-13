package com.sporekart.ai.decision.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record DecisionResult(
    UUID id, UUID requestId, DecisionAction action, DecisionStatus status,
    DecisionConfidence confidence, String summary,
    List<DecisionReason> reasons, List<DecisionEvidence> evidence,
    DecisionExplanation explanation, long processingTimeMs,
    boolean requiresApproval, boolean overrideable,
    OffsetDateTime timestamp) {}
