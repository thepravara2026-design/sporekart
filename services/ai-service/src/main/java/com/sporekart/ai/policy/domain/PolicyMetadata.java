package com.sporekart.ai.policy.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public record PolicyMetadata(
    UUID id, UUID policyId, String key, String value,
    String type, boolean isActive, OffsetDateTime createdAt) {}
