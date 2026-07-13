package com.sporekart.ai.admin.domain;

import java.util.Map;
import java.util.UUID;

public record OperationalSetting(
    UUID id,
    String key,
    Object value,
    String category,
    Map<String, Object> constraints
) {}
