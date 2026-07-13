package com.sporekart.ai.compliance.domain;

import java.util.Map;
import java.util.UUID;

public record ComplianceMetadata(
    UUID id,
    String key,
    String value,
    String category,
    Map<String, Object> attributes
) {}
