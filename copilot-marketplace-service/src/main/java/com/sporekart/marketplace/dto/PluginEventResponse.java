package com.sporekart.marketplace.dto;

import java.time.Instant;
import java.util.Map;

public record PluginEventResponse(
    String eventId,
    String eventType,
    String pluginId,
    Instant timestamp,
    Map<String, Object> data
) {}