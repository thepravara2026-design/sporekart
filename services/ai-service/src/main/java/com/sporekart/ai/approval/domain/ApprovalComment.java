package com.sporekart.ai.approval.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ApprovalComment(
    UUID id,
    UUID requestId,
    UUID reviewerId,
    String comment,
    String type,
    OffsetDateTime timestamp
) {}
