package com.sporekart.ai.approval.domain;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record ApprovalEvidence(
    UUID id,
    UUID requestId,
    String source,
    String type,
    String value,
    Map<String, Object> metadata,
    OffsetDateTime timestamp
) {}
