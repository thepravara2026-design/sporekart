package com.sporekart.ai.risk.domain;

import java.util.Map;
import java.util.UUID;

public record RiskMetadata(
    UUID id,
    String key,
    String value,
    String category,
    Map<String, Object> attributes
) {}
