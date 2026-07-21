package com.sporekart.ai.providers;

import com.sporekart.ai.providers.capability.ProviderCapability;
import com.sporekart.ai.providers.registry.activation.ActivationState;
import com.sporekart.ai.providers.registry.audit.AuditEntry;
import com.sporekart.ai.providers.registry.cache.CacheConfig;
import com.sporekart.ai.providers.registry.cache.CacheEntry;
import com.sporekart.ai.providers.registry.catalog.ProviderCatalogEntry;
import com.sporekart.ai.providers.registry.discovery.DiscoverySource;
import com.sporekart.ai.providers.registry.health.HealthIndex;
import com.sporekart.ai.providers.registry.health.HealthScore;
import com.sporekart.ai.providers.registry.lifecycle.RegistrationState;
import com.sporekart.ai.providers.registry.versioning.SemanticVersion;
import com.sporekart.ai.providers.registry.versioning.VersionCompatibility;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProviderRegistryTest {

    @Test
    void shouldCreateProviderCatalogEntry() {
        var entry = new ProviderCatalogEntry(
            "openai", "OpenAI", "OpenAI GPT", ProviderType.OPENAI, "1.0.0",
            List.of(ProviderCapability.CHAT, ProviderCapability.STREAMING),
            List.of("gpt-4", "gpt-3.5"), ProviderStatus.ACTIVE, 1, 100,
            "us-east", true, 45, "standard", 128000, 4096,
            true, true, true, true, true, true, "hipaa",
            Instant.now(), Instant.now()
        );
        assertEquals("openai", entry.providerId());
        assertEquals("OpenAI GPT", entry.displayName());
        assertTrue(entry.streamingSupported());
        assertTrue(entry.visionSupported());
        assertEquals(2, entry.capabilities().size());
    }

    @Test
    void shouldCreateHealthIndex() {
        var index = new HealthIndex("openai", true, 45, 0,
            Instant.now(), null, false, false, 0.95);
        assertTrue(index.available());
        assertFalse(index.circuitOpen());
        assertEquals(0.95, index.healthScore());
    }

    @Test
    void shouldCreateHealthScore() {
        var score = new HealthScore(0.95, HealthScore.HealthLevel.HEALTHY);
        assertEquals(HealthScore.HealthLevel.HEALTHY, score.level());
        assertEquals(HealthScore.HealthLevel.CRITICAL, HealthScore.HealthLevel.fromScore(0.1));
        assertEquals(HealthScore.HealthLevel.DEGRADED, HealthScore.HealthLevel.fromScore(0.3));
        assertEquals(HealthScore.HealthLevel.STABLE, HealthScore.HealthLevel.fromScore(0.6));
        assertEquals(HealthScore.HealthLevel.HEALTHY, HealthScore.HealthLevel.fromScore(0.9));
    }

    @Test
    void shouldCreateAuditEntry() {
        var entry = new AuditEntry("evt-1", "openai", "REGISTRATION",
            "Provider registered", "system", Instant.now(), "{}");
        assertEquals("evt-1", entry.eventId());
        assertEquals("REGISTRATION", entry.eventType());
    }

    @Test
    void shouldCreateCacheConfig() {
        var defaults = CacheConfig.defaults();
        assertTrue(defaults.enabled());
        assertEquals(1000, defaults.maxEntries());
    }

    @Test
    void shouldCreateCacheEntry() {
        var entry = new CacheEntry<>("test-value", Instant.now(),
            Instant.now().plusSeconds(300), 0);
        assertFalse(entry.isExpired());
        var accessed = entry.recordAccess();
        assertEquals(1, accessed.accessCount());
    }

    @Test
    void shouldHaveActivationStates() {
        assertEquals(8, ActivationState.values().length);
        assertEquals(ActivationState.INACTIVE, ActivationState.valueOf("INACTIVE"));
        assertEquals(ActivationState.ACTIVE, ActivationState.valueOf("ACTIVE"));
    }

    @Test
    void shouldHaveRegistrationStates() {
        assertEquals(8, RegistrationState.values().length);
        assertEquals(RegistrationState.DRAFT, RegistrationState.valueOf("DRAFT"));
        assertEquals(RegistrationState.REGISTERED, RegistrationState.valueOf("REGISTERED"));
    }

    @Test
    void shouldParseSemanticVersion() {
        var v1 = SemanticVersion.parse("1.2.3");
        assertEquals(1, v1.major());
        assertEquals(2, v1.minor());
        assertEquals(3, v1.patch());
        assertTrue(v1.isStable());

        var v2 = SemanticVersion.parse("2.0.0-beta.1");
        assertEquals(2, v2.major());
        assertFalse(v2.isStable());
        assertTrue(v2.isPreRelease());

        var v3 = SemanticVersion.parse("1.0.0+build123");
        assertEquals("build123", v3.buildMetadata());
    }

    @Test
    void shouldCompareSemanticVersions() {
        var v1 = SemanticVersion.parse("1.0.0");
        var v2 = SemanticVersion.parse("2.0.0");
        var v3 = SemanticVersion.parse("1.0.0");
        assertTrue(v1.compareTo(v2) < 0);
        assertTrue(v2.compareTo(v1) > 0);
        assertEquals(0, v1.compareTo(v3));
    }

    @Test
    void shouldCreateVersionCompatibility() {
        var compat = new VersionCompatibility(
            "openai", "2.0.0",
            List.of("1.0.0", "2.0.0"), "2.0.0", "2.0.0", null,
            List.of("0.9.0"),
            List.of(new VersionCompatibility.UpgradePath("1.0.0", "2.0.0", false,
                List.of("Breaking change in API"))),
            List.of(new VersionCompatibility.UpgradePath("2.0.0", "1.0.0", true, List.of()))
        );
        assertEquals("openai", compat.providerId());
        assertEquals(1, compat.upgradePaths().size());
        assertEquals(1, compat.rollbackPaths().size());
    }

    @Test
    void shouldSupportDiscoverySource() {
        var source = new DiscoverySource() {
            @Override public String sourceName() { return "test"; }
            @Override public List<ProviderCatalogEntry> discover() { return List.of(); }
            @Override public boolean isAvailable() { return true; }
        };
        assertEquals("test", source.sourceName());
        assertTrue(source.isAvailable());
    }

    @Test
    void shouldCreateProviderSnapshot() {
        var snapshot = new com.sporekart.ai.providers.registry.ProviderSnapshot(
            "snap-1", Instant.now(), List.of(), Map.of(), Map.of(), 0, 0, 0
        );
        assertEquals("snap-1", snapshot.snapshotId());
        assertEquals(0, snapshot.totalProviders());
    }
}
