package com.sporekart.ai.admin.interfaces.rest.dto;

public record ModuleDto(
    String id,
    String type,
    String name,
    String description,
    boolean enabled,
    String version
) {}
