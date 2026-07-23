package com.sporekart.marketplace.sdk;

import java.util.Map;

public class PluginContext {

    private final String pluginId;
    private final Map<String, Object> config;
    private final Map<String, Object> workspace;

    public PluginContext(String pluginId, Map<String, Object> config, Map<String, Object> workspace) {
        this.pluginId = pluginId;
        this.config = config;
        this.workspace = workspace;
    }

    public String getPluginId() { return pluginId; }
    public Map<String, Object> getConfig() { return config; }
    public Map<String, Object> getWorkspace() { return workspace; }

    public String getConfigValue(String key) {
        if (config.containsKey(key)) return config.get(key).toString();
        return null;
    }
}