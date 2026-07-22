package com.sporekart.prompt.dto.response;

import com.sporekart.prompt.domain.AuditAction;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AuditEntryResponse(
        UUID id,
        UUID templateId,
        UUID versionId,
        AuditAction action,
        UUID performedBy,
        String details,
        OffsetDateTime createdAt
) {}
