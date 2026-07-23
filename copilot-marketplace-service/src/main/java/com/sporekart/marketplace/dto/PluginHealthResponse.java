package com.sporekart.marketplace.dto;

import com.sporekart.marketplace.domain.PluginHealthStatus;

import java.util.List;
import java.util.Map;

public record PluginHealthResponse(
    int totalPlugins,
    int healthy,
    int unhealthy,
    int unknown,
    List<PluginHealthStatus> statuses,
    Map<String, Object> summary
) {}