package com.sporekart.ai.approval.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public record DomainConfig(UUID id, String key, String value, String description, boolean isActive, int version, OffsetDateTime createdAt, OffsetDateTime updatedAt) {}
