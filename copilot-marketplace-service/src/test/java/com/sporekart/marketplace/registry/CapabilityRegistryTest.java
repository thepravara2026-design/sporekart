package com.sporekart.marketplace.registry;

import com.sporekart.marketplace.domain.CapabilityRegistration;
import com.sporekart.marketplace.sdk.PluginCapability;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CapabilityRegistryTest {

    private CapabilityRegistry capabilityRegistry;

    @BeforeEach
    void setUp() {
        capabilityRegistry = new CapabilityRegistry();
    }

    @Test
    void register_shouldStoreCapability() {
        var reg = capabilityRegistry.register("plugin1", "Plugin One", PluginCapability.CONVERSATION);
        assertNotNull(reg);
        assertEquals("plugin1:CONVERSATION", reg.capabilityId());
        assertEquals(PluginCapability.CONVERSATION, reg.capability());
        assertEquals("plugin1", reg.pluginId());
        assertEquals("Plugin One", reg.pluginName());
        assertTrue(reg.enabled());
        assertNotNull(reg.registeredAt());
    }

    @Test
    void register_multipleCapabilitiesForSamePlugin() {
        capabilityRegistry.register("plugin1", "Plugin One", PluginCapability.CONVERSATION);
        capabilityRegistry.register("plugin1", "Plugin One", PluginCapability.ANALYTICS);
        assertEquals(2, capabilityRegistry.count());
    }

    @Test
    void register_duplicateCapabilityOverwrites() {
        var reg1 = capabilityRegistry.register("plugin1", "Plugin One", PluginCapability.CONVERSATION);
        var reg2 = capabilityRegistry.register("plugin1", "Plugin One", PluginCapability.CONVERSATION);
        assertEquals(1, capabilityRegistry.count());
        assertEquals(reg1.capabilityId(), reg2.capabilityId());
    }

    @Test
    void unregisterAll_shouldRemoveAllForPlugin() {
        capabilityRegistry.register("plugin1", "Plugin One", PluginCapability.CONVERSATION);
        capabilityRegistry.register("plugin1", "Plugin One", PluginCapability.ANALYTICS);
        capabilityRegistry.register("plugin2", "Plugin Two", PluginCapability.SEARCH);

        capabilityRegistry.unregisterAll("plugin1");

        assertEquals(1, capabilityRegistry.count());
        assertTrue(capabilityRegistry.getByPlugin("plugin1").isEmpty());
        assertFalse(capabilityRegistry.getByPlugin("plugin2").isEmpty());
    }

    @Test
    void unregisterAll_shouldDoNothingForUnknownPlugin() {
        capabilityRegistry.register("plugin1", "Plugin One", PluginCapability.CONVERSATION);
        capabilityRegistry.unregisterAll("nonexistent");
        assertEquals(1, capabilityRegistry.count());
    }

    @Test
    void get_shouldReturnCapabilityWhenExists() {
        capabilityRegistry.register("plugin1", "Plugin One", PluginCapability.CONVERSATION);
        var result = capabilityRegistry.get("plugin1:CONVERSATION");
        assertTrue(result.isPresent());
        assertEquals(PluginCapability.CONVERSATION, result.get().capability());
    }

    @Test
    void get_shouldReturnEmptyWhenNotExists() {
        var result = capabilityRegistry.get("nonexistent");
        assertTrue(result.isEmpty());
    }

    @Test
    void getByPlugin_shouldReturnAllCapabilitiesForPlugin() {
        capabilityRegistry.register("plugin1", "Plugin One", PluginCapability.CONVERSATION);
        capabilityRegistry.register("plugin1", "Plugin One", PluginCapability.ANALYTICS);
        capabilityRegistry.register("plugin2", "Plugin Two", PluginCapability.SEARCH);

        var plugin1Caps = capabilityRegistry.getByPlugin("plugin1");
        assertEquals(2, plugin1Caps.size());
    }

    @Test
    void getByPlugin_shouldReturnEmptyForUnknownPlugin() {
        var result = capabilityRegistry.getByPlugin("nonexistent");
        assertTrue(result.isEmpty());
    }

    @Test
    void getByCapability_shouldReturnAllWithGivenCapability() {
        capabilityRegistry.register("plugin1", "Plugin One", PluginCapability.CONVERSATION);
        capabilityRegistry.register("plugin2", "Plugin Two", PluginCapability.CONVERSATION);
        capabilityRegistry.register("plugin3", "Plugin Three", PluginCapability.ANALYTICS);

        var conversations = capabilityRegistry.getByCapability(PluginCapability.CONVERSATION);
        assertEquals(2, conversations.size());
    }

    @Test
    void getByCapability_shouldReturnEmptyWhenNone() {
        var result = capabilityRegistry.getByCapability(PluginCapability.AI_MODELS);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAll_shouldReturnAllRegistrations() {
        capabilityRegistry.register("plugin1", "Plugin One", PluginCapability.CONVERSATION);
        capabilityRegistry.register("plugin2", "Plugin Two", PluginCapability.ANALYTICS);
        var all = capabilityRegistry.getAll();
        assertEquals(2, all.size());
    }

    @Test
    void getAll_shouldReturnUnmodifiableList() {
        capabilityRegistry.register("plugin1", "Plugin One", PluginCapability.CONVERSATION);
        var all = capabilityRegistry.getAll();
        assertThrows(UnsupportedOperationException.class, () -> all.add(
            new CapabilityRegistration("x", PluginCapability.SEARCH, "p", "n", null, true)
        ));
    }

    @Test
    void getAll_shouldReturnSnapshot() {
        var all = capabilityRegistry.getAll();
        capabilityRegistry.register("plugin1", "Plugin One", PluginCapability.CONVERSATION);
        assertEquals(0, all.size());
    }

    @Test
    void hasCapability_shouldReturnTrueWhenPluginHasCapability() {
        capabilityRegistry.register("plugin1", "Plugin One", PluginCapability.CONVERSATION);
        assertTrue(capabilityRegistry.hasCapability("plugin1", PluginCapability.CONVERSATION));
    }

    @Test
    void hasCapability_shouldReturnFalseWhenPluginDoesNotHaveCapability() {
        capabilityRegistry.register("plugin1", "Plugin One", PluginCapability.CONVERSATION);
        assertFalse(capabilityRegistry.hasCapability("plugin1", PluginCapability.ANALYTICS));
    }

    @Test
    void hasCapability_shouldReturnFalseForUnknownPlugin() {
        assertFalse(capabilityRegistry.hasCapability("unknown", PluginCapability.CONVERSATION));
    }

    @Test
    void count_shouldReturnCorrectCount() {
        assertEquals(0, capabilityRegistry.count());
        capabilityRegistry.register("plugin1", "Plugin One", PluginCapability.CONVERSATION);
        assertEquals(1, capabilityRegistry.count());
        capabilityRegistry.register("plugin2", "Plugin Two", PluginCapability.ANALYTICS);
        assertEquals(2, capabilityRegistry.count());
    }

    @Test
    void getCapabilityDistribution_shouldReturnCounts() {
        capabilityRegistry.register("plugin1", "Plugin One", PluginCapability.CONVERSATION);
        capabilityRegistry.register("plugin2", "Plugin Two", PluginCapability.CONVERSATION);
        capabilityRegistry.register("plugin3", "Plugin Three", PluginCapability.ANALYTICS);

        var dist = capabilityRegistry.getCapabilityDistribution();
        assertEquals(2, dist.get(PluginCapability.CONVERSATION));
        assertEquals(1, dist.get(PluginCapability.ANALYTICS));
        assertNull(dist.get(PluginCapability.SEARCH));
    }

    @Test
    void getCapabilityDistribution_shouldReturnEmptyMapWhenEmpty() {
        var dist = capabilityRegistry.getCapabilityDistribution();
        assertTrue(dist.isEmpty());
    }

    @Test
    void getCapabilityDistribution_shouldNotBeAffectedByRemoval() {
        capabilityRegistry.register("plugin1", "Plugin One", PluginCapability.CONVERSATION);
        var dist = capabilityRegistry.getCapabilityDistribution();
        capabilityRegistry.register("plugin2", "Plugin Two", PluginCapability.CONVERSATION);
        assertEquals(1, dist.size());
        assertEquals(1L, dist.get(PluginCapability.CONVERSATION));
    }

    @Test
    void register_createsUniqueIdsForSameCapabilityDifferentPlugins() {
        var reg1 = capabilityRegistry.register("p1", "P1", PluginCapability.SEARCH);
        var reg2 = capabilityRegistry.register("p2", "P2", PluginCapability.SEARCH);
        assertNotEquals(reg1.capabilityId(), reg2.capabilityId());
    }

    @Test
    void unregisterAll_shouldHandlePluginWithNoCapabilities() {
        capabilityRegistry.register("plugin1", "Plugin One", PluginCapability.CONVERSATION);
        capabilityRegistry.unregisterAll("plugin2");
        assertEquals(1, capabilityRegistry.count());
    }

    @Test
    void getByPlugin_shouldReturnAllAfterMultipleRegistrations() {
        capabilityRegistry.register("p1", "P1", PluginCapability.CONVERSATION);
        capabilityRegistry.register("p1", "P1", PluginCapability.ANALYTICS);
        capabilityRegistry.register("p1", "P1", PluginCapability.SEARCH);
        capabilityRegistry.register("p1", "P1", PluginCapability.KNOWLEDGE);

        var caps = capabilityRegistry.getByPlugin("p1");
        assertEquals(4, caps.size());

        var types = caps.stream().map(CapabilityRegistration::capability).toList();
        assertTrue(types.containsAll(List.of(
            PluginCapability.CONVERSATION, PluginCapability.ANALYTICS,
            PluginCapability.SEARCH, PluginCapability.KNOWLEDGE
        )));
    }
}
