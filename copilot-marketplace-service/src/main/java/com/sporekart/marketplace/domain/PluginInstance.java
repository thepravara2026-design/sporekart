package com.sporekart.marketplace.domain;

import com.sporekart.marketplace.sdk.PluginManifest;
import com.sporekart.marketplace.sdk.PluginMetadata;
import com.sporekart.marketplace.sdk.SporekartPlugin;

public class PluginInstance {

    private final String id;
    private final PluginManifest manifest;
    private final PluginMetadata metadata;
    private PluginState state;
    private SporekartPlugin plugin;

    public PluginInstance(String id, PluginManifest manifest, PluginMetadata metadata) {
        this.id = id;
        this.manifest = manifest;
        this.metadata = metadata;
        this.state = PluginState.INSTALLED;
    }

    public String getId() { return id; }
    public PluginManifest getManifest() { return manifest; }
    public PluginMetadata getMetadata() { return metadata; }
    public PluginState getState() { return state; }
    public void setState(PluginState state) { this.state = state; }
    public SporekartPlugin getPlugin() { return plugin; }
    public void setPlugin(SporekartPlugin plugin) { this.plugin = plugin; }

    public boolean isEnabled() { return state == PluginState.ENABLED; }
    public boolean isInstalled() { return state != PluginState.UNINSTALLED; }
}