package com.sporekart.ai.policy.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public record PolicyConfiguration(
    UUID id, String key, String value, String description,
    boolean isActive, int version, OffsetDateTime createdAt, OffsetDateTime updatedAt) {}
