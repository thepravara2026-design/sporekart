package com.sporekart.marketplace.sdk;

import java.util.Map;

public abstract class AbstractPlugin implements SporekartPlugin {

    protected PluginContext context;
    protected boolean enabled = false;

    @Override
    public void onInstall(PluginContext context) {
        this.context = context;
    }

    @Override
    public void onEnable(PluginContext context) {
        this.context = context;
        this.enabled = true;
    }

    @Override
    public void onDisable(PluginContext context) {
        this.enabled = false;
    }

    @Override
    public void onUninstall(PluginContext context) {
        this.enabled = false;
        this.context = null;
    }

    @Override
    public void onUpgrade(PluginContext context, String previousVersion) {
        this.context = context;
    }

    @Override
    public void onDowngrade(PluginContext context, String previousVersion) {
        this.context = context;
    }

    @Override
    public Map<String, Object> healthCheck() {
        return Map.of("status", enabled ? "HEALTHY" : "DISABLED", "pluginId", getManifest().pluginId());
    }

    public boolean isEnabled() { return enabled; }
    public PluginContext getContext() { return context; }
}