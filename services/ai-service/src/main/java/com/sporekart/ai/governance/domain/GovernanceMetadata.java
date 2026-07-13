package com.sporekart.ai.governance.domain;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record GovernanceMetadata(
    UUID id,
    UUID requestId,
    String version,
    Map<String, Object> attributes,
    Map<String, String> tags,
    OffsetDateTime createdAt) {
}
