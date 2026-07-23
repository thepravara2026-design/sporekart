package com.sporekart.marketplace.sdk;

import java.util.List;
import java.util.Map;

public record PluginManifest(
    String pluginId,
    String name,
    String version,
    String author,
    String description,
    PluginType type,
    List<PluginCapability> capabilities,
    List<PluginPermission> requiredPermissions,
    List<String> dependencies,
    String minPlatformVersion,
    String maxPlatformVersion,
    String healthEndpoint,
    Map<String, Object> configurationSchema,
    String entryPoint
) {}