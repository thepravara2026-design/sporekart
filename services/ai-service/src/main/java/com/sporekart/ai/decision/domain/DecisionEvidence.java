package com.sporekart.ai.decision.domain;

import java.util.Map;
import java.util.UUID;

public record DecisionEvidence(
    UUID id, String source, String type, String value,
    double relevance, Map<String, Object> metadata) {}
