package com.sporekart.ai.providers.discovery;

import java.util.Map;

public record DiscoverySource(
    String sourceId,
    String sourceType,
    String location,
    boolean enabled,
    Map<String, Object> configuration,
    int priority
) {}
