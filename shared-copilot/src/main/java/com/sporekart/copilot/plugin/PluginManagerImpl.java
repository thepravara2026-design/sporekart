package com.sporekart.copilot.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class PluginManagerImpl implements PluginManager {

    private static final Logger log = LoggerFactory.getLogger(PluginManagerImpl.class);

    private final ConcurrentMap<String, CopilotPlugin> plugins = new ConcurrentHashMap<>();
    private final CopilotPluginContext pluginContext;

    public PluginManagerImpl(CopilotPluginContext pluginContext) {
        this.pluginContext = pluginContext;
        log.debug("PluginManagerImpl initialized");
    }

    @Override
    public void loadPlugin(CopilotPlugin plugin) {
        String pluginId = plugin.getId();
        if (plugins.containsKey(pluginId)) {
            log.warn("Plugin already loaded: {}", pluginId);
            return;
        }

        log.info("Loading plugin: {} ({} v{})", pluginId, plugin.getName(), plugin.getVersion());
        try {
            plugin.onLoad(pluginContext);

            plugin.getProvidedCapabilities().forEach(cap -> {
                pluginContext.getCapabilityRegistry().register(cap);
                log.debug("Registered capability from plugin '{}': {}", pluginId, cap.id());
            });

            plugins.put(pluginId, plugin);
            log.info("Plugin loaded successfully: {}", pluginId);
        } catch (Exception e) {
            log.error("Failed to load plugin '{}': {}", pluginId, e.getMessage(), e);
            throw new RuntimeException("Failed to load plugin: " + pluginId, e);
        }
    }

    @Override
    public void unloadPlugin(String pluginId) {
        CopilotPlugin plugin = plugins.remove(pluginId);
        if (plugin == null) {
            log.warn("Plugin not found for unloading: {}", pluginId);
            return;
        }

        log.info("Unloading plugin: {}", pluginId);
        try {
            plugin.onUnload();
            log.info("Plugin unloaded successfully: {}", pluginId);
        } catch (Exception e) {
            log.error("Error unloading plugin '{}': {}", pluginId, e.getMessage(), e);
        }
    }

    @Override
    public List<CopilotPlugin> getLoadedPlugins() {
        return Collections.unmodifiableList(new ArrayList<>(plugins.values()));
    }

    @Override
    public CopilotPlugin getPlugin(String pluginId) {
        return plugins.get(pluginId);
    }
}
