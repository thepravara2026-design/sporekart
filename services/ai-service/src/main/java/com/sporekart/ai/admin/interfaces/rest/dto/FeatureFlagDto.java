package com.sporekart.ai.admin.interfaces.rest.dto;

public record FeatureFlagDto(
    String id,
    String key,
    String name,
    String description,
    boolean enabled,
    String environment,
    String module,
    String updatedAt
) {}
