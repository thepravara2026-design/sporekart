package com.sporekart.ai.governance.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record GovernanceRequest(
    UUID id,
    String module,
    String action,
    Map<String, Object> payload,
    Map<String, Object> metadata,
    String userId,
    List<String> roles,
    OffsetDateTime timestamp) {
}
