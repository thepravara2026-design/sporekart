package com.sporekart.events.domain.plugin;

import com.sporekart.events.model.DomainEvent;

public class PluginInstalled extends DomainEvent {
    private final String pluginId;
    private final String pluginName;

    private PluginInstalled(Builder builder) {
        super(builder);
        this.pluginId = builder.pluginId;
        this.pluginName = builder.pluginName;
    }

    public String getPluginId() { return pluginId; }
    public String getPluginName() { return pluginName; }

    public static Builder builder() { return new Builder(); }

    public static class Builder extends DomainEvent.Builder<Builder> {
        private String pluginId;
        private String pluginName;

        public Builder pluginId(String pluginId) { this.pluginId = pluginId; return this; }
        public Builder pluginName(String pluginName) { this.pluginName = pluginName; return this; }

        public PluginInstalled build() {
            return new PluginInstalled(this);
        }
    }
}
