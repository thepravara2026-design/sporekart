package com.sporekart.ai.providers.registry;

import com.sporekart.ai.providers.ProviderHealth;
import com.sporekart.ai.providers.ProviderStatus;
import com.sporekart.ai.providers.registry.catalog.ProviderCatalogEntry;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record ProviderSnapshot(
    String snapshotId,
    Instant timestamp,
    List<ProviderCatalogEntry> entries,
    Map<String, ProviderHealth> healthMap,
    Map<String, ProviderStatus> statusMap,
    int totalProviders,
    int activeProviders,
    int healthyProviders
) {}
