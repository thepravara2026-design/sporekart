package com.sporekart.ai.admin.interfaces.rest.dto;

public record ConfigDto(
    String id,
    String key,
    String value,
    String module,
    String environment,
    String description,
    String status,
    int version,
    String updatedAt
) {}
