package com.sporekart.ai.approval.domain;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record ApprovalReviewer(
    UUID id,
    String userId,
    String name,
    String email,
    String department,
    List<String> roles,
    ReviewerType type,
    int priority,
    int maxAssignments,
    int currentAssignments,
    boolean isAvailable,
    OffsetDateTime lastAssigned
) {}
