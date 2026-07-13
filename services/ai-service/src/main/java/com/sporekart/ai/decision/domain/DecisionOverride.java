package com.sporekart.ai.decision.domain;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record DecisionOverride(
    UUID id, UUID decisionId, DecisionAction originalAction,
    DecisionAction overrideAction, String reason, String overriddenBy,
    Map<String, Object> metadata, OffsetDateTime timestamp) {}
