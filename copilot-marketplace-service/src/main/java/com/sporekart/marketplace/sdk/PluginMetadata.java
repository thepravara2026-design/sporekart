package com.sporekart.marketplace.sdk;

import java.time.Instant;
import java.util.Map;

public record PluginMetadata(
    String pluginId,
    String installedVersion,
    Instant installedAt,
    Instant lastUpdatedAt,
    Instant lastHealthCheckAt,
    String installSource,
    Map<String, String> customAttributes
) {}