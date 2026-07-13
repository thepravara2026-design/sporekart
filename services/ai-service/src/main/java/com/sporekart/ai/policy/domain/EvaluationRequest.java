package com.sporekart.ai.policy.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record EvaluationRequest(
    UUID id, String module, String action, Map<String, Object> payload,
    Map<String, Object> context, String userId, List<String> roles,
    Map<String, String> headers, OffsetDateTime timestamp) {}
