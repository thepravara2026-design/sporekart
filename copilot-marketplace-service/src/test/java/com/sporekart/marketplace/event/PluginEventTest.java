package com.sporekart.marketplace.event;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PluginEventTest {

    @Test
    void constructor_shouldSetEventId() {
        var event = new PluginEvent(PluginEvent.EventType.PLUGIN_INSTALLED, "plugin1", Map.of());
        assertNotNull(event.getEventId());
        assertFalse(event.getEventId().isEmpty());
    }

    @Test
    void constructor_shouldGenerateUniqueEventIds() {
        var event1 = new PluginEvent(PluginEvent.EventType.PLUGIN_INSTALLED, "plugin1", Map.of());
        var event2 = new PluginEvent(PluginEvent.EventType.PLUGIN_INSTALLED, "plugin1", Map.of());
        assertNotEquals(event1.getEventId(), event2.getEventId());
    }

    @Test
    void constructor_shouldSetType() {
        var event = new PluginEvent(PluginEvent.EventType.PLUGIN_UPDATED, "plugin1", Map.of());
        assertEquals(PluginEvent.EventType.PLUGIN_UPDATED, event.getType());
    }

    @Test
    void constructor_shouldSetPluginId() {
        var event = new PluginEvent(PluginEvent.EventType.PLUGIN_REMOVED, "my-plugin", Map.of());
        assertEquals("my-plugin", event.getPluginId());
    }

    @Test
    void constructor_shouldSetTimestamp() {
        var event = new PluginEvent(PluginEvent.EventType.PLUGIN_FAILED, "plugin1", Map.of());
        assertNotNull(event.getTimestamp());
    }

    @Test
    void constructor_shouldSetData() {
        var data = Map.<String, Object>of("key1", "value1", "count", 42);
        var event = new PluginEvent(PluginEvent.EventType.CONFIGURATION_CHANGED, "plugin1", data);
        assertEquals(data, event.getData());
    }

    @Test
    void constructor_shouldHandleNullData() {
        var event = new PluginEvent(PluginEvent.EventType.PLUGIN_LOADED, "plugin1", null);
        assertNull(event.getData());
    }

    @Test
    void constructor_shouldHandleEmptyData() {
        var event = new PluginEvent(PluginEvent.EventType.PLUGIN_HEALTH_CHANGED, "plugin1", Map.of());
        assertTrue(event.getData().isEmpty());
    }

    @Test
    void eventType_PLUGIN_INSTALLED_shouldExist() {
        assertEquals("PLUGIN_INSTALLED", PluginEvent.EventType.PLUGIN_INSTALLED.name());
    }

    @Test
    void eventType_PLUGIN_UPDATED_shouldExist() {
        assertEquals("PLUGIN_UPDATED", PluginEvent.EventType.PLUGIN_UPDATED.name());
    }

    @Test
    void eventType_PLUGIN_REMOVED_shouldExist() {
        assertEquals("PLUGIN_REMOVED", PluginEvent.EventType.PLUGIN_REMOVED.name());
    }

    @Test
    void eventType_PLUGIN_FAILED_shouldExist() {
        assertEquals("PLUGIN_FAILED", PluginEvent.EventType.PLUGIN_FAILED.name());
    }

    @Test
    void eventType_PLUGIN_LOADED_shouldExist() {
        assertEquals("PLUGIN_LOADED", PluginEvent.EventType.PLUGIN_LOADED.name());
    }

    @Test
    void eventType_PLUGIN_HEALTH_CHANGED_shouldExist() {
        assertEquals("PLUGIN_HEALTH_CHANGED", PluginEvent.EventType.PLUGIN_HEALTH_CHANGED.name());
    }

    @Test
    void eventType_CAPABILITY_REGISTERED_shouldExist() {
        assertEquals("CAPABILITY_REGISTERED", PluginEvent.EventType.CAPABILITY_REGISTERED.name());
    }

    @Test
    void eventType_CAPABILITY_REMOVED_shouldExist() {
        assertEquals("CAPABILITY_REMOVED", PluginEvent.EventType.CAPABILITY_REMOVED.name());
    }

    @Test
    void eventType_CONFIGURATION_CHANGED_shouldExist() {
        assertEquals("CONFIGURATION_CHANGED", PluginEvent.EventType.CONFIGURATION_CHANGED.name());
    }

    @Test
    void allEventTypes_shouldBeNine() {
        assertEquals(9, PluginEvent.EventType.values().length);
    }

    @Test
    void allEventTypes_shouldBePresent() {
        var types = PluginEvent.EventType.values();
        assertTrue(contains(types, PluginEvent.EventType.PLUGIN_INSTALLED));
        assertTrue(contains(types, PluginEvent.EventType.PLUGIN_UPDATED));
        assertTrue(contains(types, PluginEvent.EventType.PLUGIN_REMOVED));
        assertTrue(contains(types, PluginEvent.EventType.PLUGIN_FAILED));
        assertTrue(contains(types, PluginEvent.EventType.PLUGIN_LOADED));
        assertTrue(contains(types, PluginEvent.EventType.PLUGIN_HEALTH_CHANGED));
        assertTrue(contains(types, PluginEvent.EventType.CAPABILITY_REGISTERED));
        assertTrue(contains(types, PluginEvent.EventType.CAPABILITY_REMOVED));
        assertTrue(contains(types, PluginEvent.EventType.CONFIGURATION_CHANGED));
    }

    @Test
    void accessors_shouldReturnConsistentValues() {
        var data = Map.<String, Object>of("version", "1.0.0", "status", "ok");
        var event = new PluginEvent(PluginEvent.EventType.PLUGIN_UPDATED, "plugin-abc", data);

        assertEquals(PluginEvent.EventType.PLUGIN_UPDATED, event.getType());
        assertEquals("plugin-abc", event.getPluginId());
        assertEquals(data, event.getData());
        assertNotNull(event.getEventId());
        assertNotNull(event.getTimestamp());
        assertTrue(event.getTimestamp().toString().contains("202") || event.getTimestamp().toString().contains("19")
            || event.getTimestamp().toString().contains("20"));
    }

    private boolean contains(PluginEvent.EventType[] types, PluginEvent.EventType target) {
        for (var t : types) {
            if (t == target) return true;
        }
        return false;
    }
}
