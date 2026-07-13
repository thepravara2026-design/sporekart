package com.sporekart.ai.approval.domain;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record ApprovalMetadata(
    UUID id,
    UUID requestId,
    String version,
    String environment,
    Map<String, String> tags,
    Map<String, Object> attributes,
    OffsetDateTime createdAt
) {}
