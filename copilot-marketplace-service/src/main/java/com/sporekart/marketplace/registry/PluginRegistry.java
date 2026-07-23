package com.sporekart.marketplace.registry;

import com.sporekart.marketplace.domain.PluginInstance;
import com.sporekart.marketplace.domain.PluginState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PluginRegistry {

    private static final Logger log = LoggerFactory.getLogger(PluginRegistry.class);

    private final ConcurrentHashMap<String, PluginInstance> plugins = new ConcurrentHashMap<>();

    public PluginInstance register(String id, PluginInstance instance) {
        log.info("Registering plugin: {} version={}", id, instance.getManifest().version());
        plugins.put(id, instance);
        return instance;
    }

    public Optional<PluginInstance> get(String id) {
        return Optional.ofNullable(plugins.get(id));
    }

    public List<PluginInstance> getAll() {
        return List.copyOf(plugins.values());
    }

    public List<PluginInstance> getByState(PluginState state) {
        return plugins.values().stream()
            .filter(p -> p.getState() == state)
            .toList();
    }

    public List<PluginInstance> getByType(String type) {
        return plugins.values().stream()
            .filter(p -> p.getManifest().type().name().equalsIgnoreCase(type))
            .toList();
    }

    public boolean exists(String id) {
        return plugins.containsKey(id);
    }

    public PluginInstance unregister(String id) {
        log.info("Unregistering plugin: {}", id);
        return plugins.remove(id);
    }

    public int count() {
        return plugins.size();
    }

    public int countByState(PluginState state) {
        return (int) plugins.values().stream().filter(p -> p.getState() == state).count();
    }

    public void updateState(String id, PluginState state) {
        get(id).ifPresent(instance -> {
            instance.setState(state);
            log.info("Plugin {} state updated to: {}", id, state);
        });
    }
}
