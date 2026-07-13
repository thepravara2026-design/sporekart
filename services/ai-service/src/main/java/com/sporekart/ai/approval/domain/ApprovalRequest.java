package com.sporekart.ai.approval.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record ApprovalRequest(
    UUID id,
    String module,
    String action,
    Map<String, Object> payload,
    Map<String, Object> context,
    String userId,
    List<String> roles,
    String reason,
    String urgency,
    UUID decisionId,
    Map<String, Object> metadata,
    OffsetDateTime deadline,
    ApprovalStatus status,
    OffsetDateTime createdAt
) {}
