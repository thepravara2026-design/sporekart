package com.sporekart.ai.provider.registry;

import com.sporekart.ai.provider.implementations.MockAIProvider;
import com.sporekart.ai.provider.interfaces.AIProvider;
import com.sporekart.ai.provider.models.ProviderHealth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProviderRegistryTest {
    private ProviderRegistryImpl registry;

    @BeforeEach
    void setUp() {
        registry = new ProviderRegistryImpl();
    }

    @Test
    void shouldRegisterProvider() {
        var provider = new MockAIProvider("test-1", "TEST");
        registry.register(provider);
        assertTrue(registry.isRegistered("test-1"));
        assertEquals(1, registry.providerCount());
    }

    @Test
    void shouldUnregisterProvider() {
        var provider = new MockAIProvider("test-1", "TEST");
        registry.register(provider);
        registry.unregister("test-1");
        assertFalse(registry.isRegistered("test-1"));
        assertEquals(0, registry.providerCount());
    }

    @Test
    void shouldLookupProviderById() {
        var provider = new MockAIProvider("test-1", "TEST");
        registry.register(provider);
        var found = registry.lookup("test-1");
        assertTrue(found.isPresent());
        assertEquals("test-1", found.get().providerId());
    }

    @Test
    void shouldReturnEmptyForUnknownProvider() {
        var found = registry.lookup("nonexistent");
        assertTrue(found.isEmpty());
    }

    @Test
    void shouldLookupByType() {
        var provider = new MockAIProvider("test-1", "TEST_PROVIDER");
        registry.register(provider);
        var found = registry.lookupByType("TEST_PROVIDER");
        assertTrue(found.isPresent());
        assertEquals("test-1", found.get().providerId());
    }

    @Test
    void shouldReturnAllProviders() {
        registerThreeProviders();
        assertEquals(3, registry.getAllProviders().size());
    }

    @Test
    void shouldReturnAvailableProviders() {
        var p1 = new MockAIProvider("p1", "P1");
        var p2 = new MockAIProvider("p2", "P2");
        p2.setAvailable(false);
        registry.register(p1);
        registry.register(p2);
        var available = registry.getAvailableProviders();
        assertEquals(1, available.size());
        assertEquals("p1", available.get(0).providerId());
    }

    @Test
    void shouldReturnHealthyProviders() {
        var p1 = new MockAIProvider("p1", "P1");
        var p2 = new MockAIProvider("p2", "P2");
        registry.register(p1);
        registry.register(p2);
        registry.updateHealth("p2", ProviderHealth.unhealthy("p2", "P2", "failure"));
        var healthy = registry.getHealthyProviders();
        assertEquals(1, healthy.size());
        assertEquals("p1", healthy.get(0).providerId());
    }

    @Test
    void shouldReturnProviderMetadata() {
        var provider = new MockAIProvider("test-1", "TEST");
        registry.register(provider);
        var metadata = registry.getProviderMetadata();
        assertEquals(1, metadata.size());
        assertEquals("test-1", metadata.get(0).providerId());
    }

    @Test
    void shouldReturnMetadataForSpecificProvider() {
        var provider = new MockAIProvider("test-1", "TEST");
        registry.register(provider);
        var meta = registry.getProviderMetadata("test-1");
        assertTrue(meta.isPresent());
        assertEquals("TEST", meta.get().providerName());
    }

    @Test
    void shouldUpdateHealth() {
        var provider = new MockAIProvider("test-1", "TEST");
        registry.register(provider);
        var health = ProviderHealth.unhealthy("test-1", "TEST", "down");
        registry.updateHealth("test-1", health);
        var stored = registry.getHealth("test-1");
        assertNotNull(stored);
        assertEquals(ProviderHealth.HealthStatus.UNHEALTHY, stored.status());
    }

    @Test
    void shouldClearAllProviders() {
        registerThreeProviders();
        registry.clear();
        assertEquals(0, registry.providerCount());
        assertTrue(registry.getAllProviders().isEmpty());
    }

    @Test
    void shouldNotAllowDuplicateRegistration() {
        var p1 = new MockAIProvider("test-1", "TEST");
        var p2 = new MockAIProvider("test-1", "TEST");
        registry.register(p1);
        registry.register(p2);
        assertEquals(1, registry.providerCount());
    }

    @Test
    void shouldReturnAllHealth() {
        var p1 = new MockAIProvider("p1", "P1");
        registry.register(p1);
        var allHealth = registry.getAllHealth();
        assertEquals(1, allHealth.size());
        assertTrue(allHealth.containsKey("p1"));
    }

    private void registerThreeProviders() {
        registry.register(new MockAIProvider("p1", "P1"));
        registry.register(new MockAIProvider("p2", "P2"));
        registry.register(new MockAIProvider("p3", "P3"));
    }
}
