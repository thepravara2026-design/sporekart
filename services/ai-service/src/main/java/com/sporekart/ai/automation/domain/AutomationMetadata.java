package com.sporekart.ai.automation.domain;

import java.util.Map;
import java.util.UUID;

public record AutomationMetadata(
    UUID id,
    String key,
    String value,
    String category,
    Map<String, Object> attributes
) {}
