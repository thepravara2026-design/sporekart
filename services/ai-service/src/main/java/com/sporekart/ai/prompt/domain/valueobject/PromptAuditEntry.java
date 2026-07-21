package com.sporekart.ai.prompt.domain.valueobject;

import java.time.Instant;
import java.util.Objects;

public record PromptAuditEntry(
    AuditAction action,
    String performedBy,
    Instant timestamp,
    String previousValue,
    String newValue,
    String reason,
    String approvalReference,
    String reviewNotes,
    String rollbackReference
) {
    public PromptAuditEntry {
        Objects.requireNonNull(action, "AuditAction must not be null");
        Objects.requireNonNull(performedBy, "performedBy must not be null");
        if (performedBy.isBlank()) throw new IllegalArgumentException("performedBy must not be blank");
        timestamp = timestamp == null ? Instant.now() : timestamp;
    }

    public PromptAuditEntry(AuditAction action, String performedBy) {
        this(action, performedBy, Instant.now(), null, null, null, null, null, null);
    }

    public PromptAuditEntry(AuditAction action, String performedBy, String reason) {
        this(action, performedBy, Instant.now(), null, null, reason, null, null, null);
    }
}
