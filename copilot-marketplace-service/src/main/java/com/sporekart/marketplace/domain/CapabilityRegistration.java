package com.sporekart.marketplace.domain;

import com.sporekart.marketplace.sdk.PluginCapability;

import java.time.Instant;

public record CapabilityRegistration(
    String capabilityId,
    PluginCapability capability,
    String pluginId,
    String pluginName,
    Instant registeredAt,
    boolean enabled
) {}