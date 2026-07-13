package com.sporekart.ai.governance.domain;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record GovernanceLifecycle(
    UUID id,
    UUID policyId,
    String event,
    String fromStatus,
    String toStatus,
    String triggeredBy,
    Map<String, Object> metadata,
    OffsetDateTime timestamp) {
}
