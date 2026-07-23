package com.sporekart.marketplace.registry;

import com.sporekart.marketplace.domain.CapabilityRegistration;
import com.sporekart.marketplace.sdk.PluginCapability;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CapabilityRegistry {

    private static final Logger log = LoggerFactory.getLogger(CapabilityRegistry.class);

    private final ConcurrentHashMap<String, CapabilityRegistration> capabilities = new ConcurrentHashMap<>();

    public CapabilityRegistration register(String pluginId, String pluginName, PluginCapability capability) {
        var id = pluginId + ":" + capability.name();
        var registration = new CapabilityRegistration(id, capability, pluginId, pluginName, Instant.now(), true);
        capabilities.put(id, registration);
        log.info("Capability registered: {} by plugin {}", capability, pluginId);
        return registration;
    }

    public void unregisterAll(String pluginId) {
        var toRemove = capabilities.values().stream()
            .filter(c -> c.pluginId().equals(pluginId))
            .map(CapabilityRegistration::capabilityId)
            .toList();
        toRemove.forEach(capabilities::remove);
        log.info("Unregistered {} capabilities for plugin {}", toRemove.size(), pluginId);
    }

    public Optional<CapabilityRegistration> get(String capabilityId) {
        return Optional.ofNullable(capabilities.get(capabilityId));
    }

    public List<CapabilityRegistration> getByPlugin(String pluginId) {
        return capabilities.values().stream()
            .filter(c -> c.pluginId().equals(pluginId))
            .toList();
    }

    public List<CapabilityRegistration> getByCapability(PluginCapability capability) {
        return capabilities.values().stream()
            .filter(c -> c.capability() == capability)
            .toList();
    }

    public List<CapabilityRegistration> getAll() {
        return List.copyOf(capabilities.values());
    }

    public boolean hasCapability(String pluginId, PluginCapability capability) {
        return capabilities.values().stream()
            .anyMatch(c -> c.pluginId().equals(pluginId) && c.capability() == capability);
    }

    public int count() {
        return capabilities.size();
    }

    public Map<PluginCapability, Long> getCapabilityDistribution() {
        var dist = new HashMap<PluginCapability, Long>();
        capabilities.values().forEach(c -> dist.merge(c.capability(), 1L, Long::sum));
        return dist;
    }
}
