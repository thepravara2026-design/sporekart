package com.sporekart.ai.approval.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record ApprovalGroup(
    UUID id,
    String name,
    String description,
    List<String> memberIds,
    String department,
    boolean isActive,
    OffsetDateTime createdAt
) {}
