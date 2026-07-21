package com.sporekart.ai.provider.capability;

import com.sporekart.ai.provider.interfaces.AIProvider;
import com.sporekart.ai.provider.models.ProviderCapabilities;
import com.sporekart.ai.provider.registry.ProviderRegistry;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class CapabilityMatrix {
    private final ProviderRegistry registry;
    private final Map<String, Set<String>> capabilityIndex = new ConcurrentHashMap<>();

    public CapabilityMatrix(ProviderRegistry registry) {
        this.registry = registry;
        buildIndex();
    }

    public void buildIndex() {
        capabilityIndex.clear();
        for (var provider : registry.getAllProviders()) {
            var caps = provider.getCapabilities();
            indexProvider(provider, caps);
        }
    }

    public void rebuildIndex() {
        buildIndex();
    }

    public Set<String> findProvidersWithCapability(String capability) {
        return capabilityIndex.getOrDefault(capability.toLowerCase(), Set.of());
    }

    public List<AIProvider> findProvidersSupporting(String capability) {
        var providerIds = findProvidersWithCapability(capability);
        return providerIds.stream()
                .map(registry::lookup)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public Optional<AIProvider> findBestProviderFor(String capability) {
        return findProvidersSupporting(capability).stream()
                .filter(AIProvider::isAvailable)
                .findFirst();
    }

    public Set<String> getAllCapabilities() {
        return Set.copyOf(capabilityIndex.keySet());
    }

    public Map<String, Set<String>> getCapabilityMatrix() {
        return Map.copyOf(capabilityIndex);
    }

    public boolean providerSupports(String providerId, String capability) {
        var providers = capabilityIndex.get(capability.toLowerCase());
        return providers != null && providers.contains(providerId);
    }

    public Map<String, List<String>> getProviderCapabilityMap() {
        var result = new HashMap<String, List<String>>();
        for (var entry : capabilityIndex.entrySet()) {
            for (var providerId : entry.getValue()) {
                result.computeIfAbsent(providerId, k -> new ArrayList<>()).add(entry.getKey());
            }
        }
        return result;
    }

    public Set<String> getProviderCapabilities(String providerId) {
        return getProviderCapabilityMap()
                .getOrDefault(providerId, List.of())
                .stream()
                .collect(Collectors.toSet());
    }

    public void refreshProvider(String providerId) {
        registry.lookup(providerId).ifPresent(provider -> {
            removeFromIndex(providerId);
            indexProvider(provider, provider.getCapabilities());
        });
    }

    private void indexProvider(AIProvider provider, ProviderCapabilities caps) {
        var pid = provider.providerId();

        if (caps.streamingSupported())
            capabilityIndex.computeIfAbsent("streaming", k -> ConcurrentHashMap.newKeySet()).add(pid);
        if (caps.visionSupported())
            capabilityIndex.computeIfAbsent("vision", k -> ConcurrentHashMap.newKeySet()).add(pid);
        if (caps.functionCallingSupported())
            capabilityIndex.computeIfAbsent("function-calling", k -> ConcurrentHashMap.newKeySet()).add(pid);
        if (caps.jsonModeSupported())
            capabilityIndex.computeIfAbsent("json-mode", k -> ConcurrentHashMap.newKeySet()).add(pid);
        if (caps.embeddingsSupported())
            capabilityIndex.computeIfAbsent("embeddings", k -> ConcurrentHashMap.newKeySet()).add(pid);
        if (caps.imageGenerationSupported())
            capabilityIndex.computeIfAbsent("image-generation", k -> ConcurrentHashMap.newKeySet()).add(pid);
        if (caps.audioSupported())
            capabilityIndex.computeIfAbsent("audio", k -> ConcurrentHashMap.newKeySet()).add(pid);
        if (caps.videoSupported())
            capabilityIndex.computeIfAbsent("video", k -> ConcurrentHashMap.newKeySet()).add(pid);
        if (caps.reasoningSupported())
            capabilityIndex.computeIfAbsent("reasoning", k -> ConcurrentHashMap.newKeySet()).add(pid);

        capabilityIndex.computeIfAbsent("chat", k -> ConcurrentHashMap.newKeySet()).add(pid);
        capabilityIndex.computeIfAbsent("completion", k -> ConcurrentHashMap.newKeySet()).add(pid);

        for (var model : caps.supportedModels()) {
            capabilityIndex.computeIfAbsent("model:" + model.toLowerCase(), k -> ConcurrentHashMap.newKeySet()).add(pid);
        }
    }

    private void removeFromIndex(String providerId) {
        capabilityIndex.values().forEach(set -> set.remove(providerId));
        capabilityIndex.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }
}
