package com.sporekart.ai.governance.domain;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record GovernanceContext(
    UUID id,
    UUID requestId,
    String module,
    String action,
    Map<String, Object> resource,
    Map<String, Object> subject,
    Map<String, Object> environment,
    OffsetDateTime timestamp) {
}
