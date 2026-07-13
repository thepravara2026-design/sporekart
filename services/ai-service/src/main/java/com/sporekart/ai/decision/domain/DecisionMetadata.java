package com.sporekart.ai.decision.domain;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record DecisionMetadata(
    UUID id, UUID decisionId, String version, String environment,
    Map<String, String> tags, Map<String, Object> attributes,
    OffsetDateTime createdAt) {}
