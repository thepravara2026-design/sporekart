package com.sporekart.marketplace.dto;

import com.sporekart.marketplace.domain.PluginInstance;
import com.sporekart.marketplace.domain.PluginState;
import com.sporekart.marketplace.sdk.PluginManifest;

public record PluginResponse(
    String pluginId,
    String name,
    String version,
    String author,
    String description,
    String type,
    String state,
    String healthStatus
) {
    public static PluginResponse from(PluginInstance instance) {
        var m = instance.getManifest();
        return new PluginResponse(
            m.pluginId(), m.name(), m.version(), m.author(),
            m.description(), m.type().name(), instance.getState().name(), "UNKNOWN");
    }

    public static PluginResponse from(PluginInstance instance, String healthStatus) {
        var m = instance.getManifest();
        return new PluginResponse(
            m.pluginId(), m.name(), m.version(), m.author(),
            m.description(), m.type().name(), instance.getState().name(), healthStatus);
    }
}