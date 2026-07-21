package com.sporekart.ai.providers.lifecycle;

import com.sporekart.ai.providers.lifecycle.state.LifecycleState;

import java.time.Instant;
import java.util.Map;

public record LifecycleSnapshot(
    String snapshotId,
    Instant timestamp,
    Map<String, LifecycleState> providerStates,
    int totalProviders,
    int activeProviders,
    int degradedProviders,
    int failedProviders,
    int maintenanceProviders
) {}
