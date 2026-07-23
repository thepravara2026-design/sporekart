package com.sporekart.marketplace.event;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class PluginEvent {

    public enum EventType {
        PLUGIN_INSTALLED,
        PLUGIN_UPDATED,
        PLUGIN_REMOVED,
        PLUGIN_FAILED,
        PLUGIN_LOADED,
        PLUGIN_HEALTH_CHANGED,
        CAPABILITY_REGISTERED,
        CAPABILITY_REMOVED,
        CONFIGURATION_CHANGED
    }

    private final String eventId;
    private final EventType type;
    private final String pluginId;
    private final Instant timestamp;
    private final Map<String, Object> data;

    public PluginEvent(EventType type, String pluginId, Map<String, Object> data) {
        this.eventId = UUID.randomUUID().toString();
        this.type = type;
        this.pluginId = pluginId;
        this.timestamp = Instant.now();
        this.data = data;
    }

    public String getEventId() { return eventId; }
    public EventType getType() { return type; }
    public String getPluginId() { return pluginId; }
    public Instant getTimestamp() { return timestamp; }
    public Map<String, Object> getData() { return data; }
}
