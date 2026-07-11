package com.sporekart.ai.core.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AuditMetadata(
        UUID createdBy,
        OffsetDateTime createdAt,
        UUID updatedBy,
        OffsetDateTime updatedAt,
        boolean deleted) {
    public static AuditMetadata create(UUID createdBy) {
        return new AuditMetadata(createdBy, OffsetDateTime.now(), null, null, false);
    }
}
