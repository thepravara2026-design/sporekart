package com.sporekart.ai.policy.domain;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record PolicyRegistry(
    UUID id, String name, String module, PolicyType type,
    PolicyScope scope, boolean isActive, boolean isRegistered,
    Map<String, Object> config, OffsetDateTime registeredAt,
    OffsetDateTime updatedAt) {}
