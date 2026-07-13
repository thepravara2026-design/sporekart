package com.sporekart.ai.governance.domain;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record GovernanceViolation(
    UUID id,
    String ruleName,
    String message,
    GovernanceSeverity severity,
    Map<String, Object> details,
    boolean overridable,
    OffsetDateTime timestamp) {
}
