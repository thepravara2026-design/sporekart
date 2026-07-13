package com.sporekart.ai.automation.interfaces.rest.dto;

import java.util.Map;

public record LifecycleDto(
    String id,
    String name,
    String entityType,
    String initialState,
    Map<String, Object> transitions,
    Map<String, Object> config
) {}
