package com.sporekart.ai.decision.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record DecisionRequest(
    UUID id, String module, String action, Map<String, Object> payload,
    Map<String, Object> context, String userId, List<String> roles,
    List<String> matchedPolicyIds, List<String> matchedRuleIds,
    Map<String, Object> policyResults, OffsetDateTime timestamp) {}
