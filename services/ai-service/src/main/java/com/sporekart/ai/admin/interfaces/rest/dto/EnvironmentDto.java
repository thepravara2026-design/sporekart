package com.sporekart.ai.admin.interfaces.rest.dto;

public record EnvironmentDto(
    String id,
    String name,
    String type,
    String description,
    boolean active
) {}
