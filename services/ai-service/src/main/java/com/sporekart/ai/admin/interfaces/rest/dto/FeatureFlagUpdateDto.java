package com.sporekart.ai.admin.interfaces.rest.dto;

import java.util.Map;

public record FeatureFlagUpdateDto(
    String key,
    boolean enabled,
    String environment,
    String module,
    Map<String, Object> metadata
) {}
