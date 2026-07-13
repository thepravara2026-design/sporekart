package com.sporekart.ai.decision.domain;

import java.util.Map;
import java.util.UUID;

public record DecisionReason(
    UUID id, String code, String message, String category,
    DecisionConfidence confidence, Map<String, Object> details) {}
