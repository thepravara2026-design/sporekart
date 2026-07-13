package com.sporekart.ai.analytics.interfaces.rest.dto;

import java.util.Map;

public record SnapshotDto(
    String id,
    String name,
    Map<String, Object> data,
    String capturedAt
) {}
