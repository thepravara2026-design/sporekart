package com.sporekart.ai.compliance.interfaces.rest.dto;

public record FrameworkDto(
    String id,
    String name,
    String version,
    String type,
    String description,
    String authority,
    boolean active
) {}
