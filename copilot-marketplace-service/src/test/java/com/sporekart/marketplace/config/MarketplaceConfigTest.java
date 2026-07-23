package com.sporekart.marketplace.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MarketplaceConfigTest {

    private MarketplaceConfig config;

    @BeforeEach
    void setUp() {
        config = new MarketplaceConfig();
    }

    @Test
    void shouldHaveDefaultValues() {
        assertThat(config.getName()).isEqualTo("SporeKart Copilot Marketplace");
        assertThat(config.getVersion()).isEqualTo("1.0.0");
        assertThat(config.getPluginStorePath()).isEqualTo("./plugins");
        assertThat(config.getMaxPluginSize()).isEqualTo("100MB");
        assertThat(config.getMaxInstalledPlugins()).isEqualTo(100);
    }

    @Test
    void shouldSetAndGetName() {
        config.setName("Custom Marketplace");
        assertThat(config.getName()).isEqualTo("Custom Marketplace");
    }

    @Test
    void shouldSetAndGetVersion() {
        config.setVersion("2.0.0");
        assertThat(config.getVersion()).isEqualTo("2.0.0");
    }

    @Test
    void shouldSetAndGetPluginStorePath() {
        config.setPluginStorePath("/custom/plugins");
        assertThat(config.getPluginStorePath()).isEqualTo("/custom/plugins");
    }

    @Test
    void shouldSetAndGetMaxPluginSize() {
        config.setMaxPluginSize("500MB");
        assertThat(config.getMaxPluginSize()).isEqualTo("500MB");
    }

    @Test
    void shouldSetAndGetMaxInstalledPlugins() {
        config.setMaxInstalledPlugins(200);
        assertThat(config.getMaxInstalledPlugins()).isEqualTo(200);
    }

    @Test
    void shouldHaveDefaultSandboxConfig() {
        var sandbox = config.getSandbox();
        assertThat(sandbox).isNotNull();
        assertThat(sandbox.isEnabled()).isTrue();
        assertThat(sandbox.getMaxMemoryPerPlugin()).isEqualTo("256MB");
        assertThat(sandbox.getMaxExecutionTimeoutMs()).isEqualTo(30000);
        assertThat(sandbox.getMaxThreadsPerPlugin()).isEqualTo(5);
    }

    @Test
    void shouldSetAndGetSandboxEnabled() {
        var sandbox = config.getSandbox();
        sandbox.setEnabled(false);
        assertThat(sandbox.isEnabled()).isFalse();
    }

    @Test
    void shouldSetAndGetSandboxMaxMemory() {
        var sandbox = config.getSandbox();
        sandbox.setMaxMemoryPerPlugin("512MB");
        assertThat(sandbox.getMaxMemoryPerPlugin()).isEqualTo("512MB");
    }

    @Test
    void shouldSetAndGetSandboxExecutionTimeout() {
        var sandbox = config.getSandbox();
        sandbox.setMaxExecutionTimeoutMs(60000);
        assertThat(sandbox.getMaxExecutionTimeoutMs()).isEqualTo(60000);
    }

    @Test
    void shouldSetAndGetSandboxMaxThreads() {
        var sandbox = config.getSandbox();
        sandbox.setMaxThreadsPerPlugin(10);
        assertThat(sandbox.getMaxThreadsPerPlugin()).isEqualTo(10);
    }

    @Test
    void shouldHaveDefaultHealthConfig() {
        var health = config.getHealth();
        assertThat(health).isNotNull();
        assertThat(health.getCheckIntervalMs()).isEqualTo(60000);
        assertThat(health.getFailureThreshold()).isEqualTo(3);
        assertThat(health.getRecoveryThreshold()).isEqualTo(2);
    }

    @Test
    void shouldSetAndGetHealthCheckInterval() {
        var health = config.getHealth();
        health.setCheckIntervalMs(120000);
        assertThat(health.getCheckIntervalMs()).isEqualTo(120000);
    }

    @Test
    void shouldSetAndGetHealthFailureThreshold() {
        var health = config.getHealth();
        health.setFailureThreshold(5);
        assertThat(health.getFailureThreshold()).isEqualTo(5);
    }

    @Test
    void shouldSetAndGetHealthRecoveryThreshold() {
        var health = config.getHealth();
        health.setRecoveryThreshold(3);
        assertThat(health.getRecoveryThreshold()).isEqualTo(3);
    }

    @Test
    void shouldSetAndGetSandbox() {
        var customSandbox = new MarketplaceConfig.SandboxConfig();
        customSandbox.setEnabled(false);
        config.setSandbox(customSandbox);
        assertThat(config.getSandbox()).isSameAs(customSandbox);
        assertThat(config.getSandbox().isEnabled()).isFalse();
    }

    @Test
    void shouldSetAndGetHealth() {
        var customHealth = new MarketplaceConfig.HealthConfig();
        customHealth.setCheckIntervalMs(999);
        config.setHealth(customHealth);
        assertThat(config.getHealth()).isSameAs(customHealth);
        assertThat(config.getHealth().getCheckIntervalMs()).isEqualTo(999);
    }
}
