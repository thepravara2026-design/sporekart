package com.sporekart.ai.approval.domain;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ApprovalSla(
    UUID id,
    UUID requestId,
    int slaMinutes,
    OffsetDateTime deadline,
    SlaStatus status,
    OffsetDateTime notifiedAt,
    OffsetDateTime escalatedAt
) {}
