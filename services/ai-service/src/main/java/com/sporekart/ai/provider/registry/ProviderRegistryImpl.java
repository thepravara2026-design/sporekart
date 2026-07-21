package com.sporekart.ai.provider.registry;

import com.sporekart.ai.provider.interfaces.AIProvider;
import com.sporekart.ai.provider.models.ProviderHealth;
import com.sporekart.ai.provider.models.ProviderInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ProviderRegistryImpl implements ProviderRegistry {
    private final Map<String, AIProvider> providers = new ConcurrentHashMap<>();
    private final Map<String, ProviderHealth> healthMap = new ConcurrentHashMap<>();

    @Override
    public void register(AIProvider provider) {
        providers.put(provider.providerId(), provider);
        healthMap.put(provider.providerId(), provider.checkHealth());
    }

    @Override
    public void unregister(String providerId) {
        providers.remove(providerId);
        healthMap.remove(providerId);
    }

    @Override
    public Optional<AIProvider> lookup(String providerId) {
        return Optional.ofNullable(providers.get(providerId));
    }

    @Override
    public Optional<AIProvider> lookupByType(String providerType) {
        return providers.values().stream()
                .filter(p -> p.providerName().equalsIgnoreCase(providerType)
                        || p.providerId().equalsIgnoreCase(providerType))
                .findFirst();
    }

    @Override
    public List<AIProvider> getAllProviders() {
        return List.copyOf(providers.values());
    }

    @Override
    public List<AIProvider> getAvailableProviders() {
        return providers.values().stream()
                .filter(AIProvider::isAvailable)
                .collect(Collectors.toList());
    }

    @Override
    public List<AIProvider> getHealthyProviders() {
        return providers.values().stream()
                .filter(p -> {
                    var h = healthMap.get(p.providerId());
                    return h != null && h.isAvailable();
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ProviderInfo> getProviderMetadata() {
        return providers.values().stream()
                .map(this::toProviderInfo)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProviderInfo> getProviderMetadata(String providerId) {
        return lookup(providerId).map(this::toProviderInfo);
    }

    @Override
    public void updateHealth(String providerId, ProviderHealth health) {
        healthMap.put(providerId, health);
    }

    @Override
    public boolean isRegistered(String providerId) {
        return providers.containsKey(providerId);
    }

    @Override
    public int providerCount() {
        return providers.size();
    }

    @Override
    public void clear() {
        providers.clear();
        healthMap.clear();
    }

    public ProviderHealth getHealth(String providerId) {
        return healthMap.get(providerId);
    }

    public Map<String, ProviderHealth> getAllHealth() {
        return Map.copyOf(healthMap);
    }

    private ProviderInfo toProviderInfo(AIProvider provider) {
        var caps = provider.getCapabilities();
        var health = healthMap.getOrDefault(provider.providerId(),
                ProviderHealth.healthy(provider.providerId(), provider.providerName()));
        return new ProviderInfo(
                provider.providerId(),
                provider.providerName(),
                provider.providerName(),
                provider.version().orElse("0.0.0"),
                provider.providerName() + " provider",
                "",
                "",
                health.status(),
                com.sporekart.ai.provider.models.ProviderCostInfo.PricingTier.MEDIUM,
                caps.supportedModels().stream().toList(),
                caps.supportedModalities().stream().toList(),
                caps.maxTokens(),
                caps.streamingSupported(),
                caps.visionSupported(),
                caps.functionCallingSupported(),
                Optional.empty(),
                true);
    }
}
