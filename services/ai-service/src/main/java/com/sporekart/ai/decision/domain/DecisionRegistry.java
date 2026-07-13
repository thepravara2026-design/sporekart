package com.sporekart.ai.decision.domain;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record DecisionRegistry(
    UUID id, String name, String module, String endpoint,
    boolean isActive, boolean isRegistered,
    Map<String, Object> config, OffsetDateTime registeredAt,
    OffsetDateTime updatedAt) {}
