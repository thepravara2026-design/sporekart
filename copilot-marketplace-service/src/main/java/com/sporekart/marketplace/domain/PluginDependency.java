package com.sporekart.marketplace.domain;

public record PluginDependency(
    String pluginId,
    String version,
    boolean required
) {}