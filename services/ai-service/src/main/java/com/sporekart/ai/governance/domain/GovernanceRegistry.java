package com.sporekart.ai.governance.domain;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record GovernanceRegistry(
    UUID id,
    String name,
    String module,
    String endpoint,
    GovernanceScope scope,
    GovernanceMode mode,
    Map<String, Object> config,
    boolean isRegistered,
    OffsetDateTime registeredAt,
    OffsetDateTime updatedAt) {
}
