package com.sporekart.ai.admin.interfaces.rest.dto;

public record ConfigUpdateDto(
    String key,
    String value,
    String module,
    String environment,
    String description
) {}
