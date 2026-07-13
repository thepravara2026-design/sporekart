package com.sporekart.ai.decision.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record DecisionContext(
    UUID id, UUID requestId, String module, String action,
    Map<String, Object> resource, Map<String, Object> subject,
    Map<String, Object> environment, List<String> roles,
    List<String> activePolicyIds, Map<String, Object> policyEvaluationSummary,
    OffsetDateTime timestamp) {}
