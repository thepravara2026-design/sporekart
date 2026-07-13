package com.sporekart.ai.policy.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public record PolicyVersion(
    UUID id, UUID policyId, int versionNumber, String name,
    String description, String content, PolicyStatus status,
    String changeNotes, UUID createdBy, OffsetDateTime createdAt) {}
