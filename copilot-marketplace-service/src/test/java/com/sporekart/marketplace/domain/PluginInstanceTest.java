package com.sporekart.marketplace.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sporekart.marketplace.sdk.PluginManifest;
import com.sporekart.marketplace.sdk.PluginMetadata;
import com.sporekart.marketplace.sdk.PluginType;

class PluginInstanceTest {

    private PluginManifest manifest;
    private PluginMetadata metadata;

    @BeforeEach
    void setUp() {
        manifest = new PluginManifest(
            "p001", "Test", "1.0", "author", "desc", PluginType.ML,
            List.of(), List.of(), List.of(), "1", "2", "/h", Map.of(), "e");
        metadata = new PluginMetadata(
            "p001", "1.0", null, null, null, "src", Map.of());
    }

    @Test
    void shouldConstructWithInstalledState() {
        var instance = new PluginInstance("p001", manifest, metadata);
        assertThat(instance.getId()).isEqualTo("p001");
        assertThat(instance.getManifest()).isSameAs(manifest);
        assertThat(instance.getMetadata()).isSameAs(metadata);
        assertThat(instance.getState()).isEqualTo(PluginState.INSTALLED);
    }

    @Test
    void shouldSetAndGetState() {
        var instance = new PluginInstance("p001", manifest, metadata);
        instance.setState(PluginState.ENABLED);
        assertThat(instance.getState()).isEqualTo(PluginState.ENABLED);
    }

    @Test
    void shouldSetAndGetPlugin() {
        var instance = new PluginInstance("p001", manifest, metadata);
        var plugin = new com.sporekart.marketplace.sdk.AbstractPlugin() {
            @Override
            public com.sporekart.marketplace.sdk.PluginManifest getManifest() { return manifest; }
            @Override
            public java.util.Map<String, Object> execute(String a, java.util.Map<String, Object> p) { return java.util.Map.of(); }
        };
        instance.setPlugin(plugin);
        assertThat(instance.getPlugin()).isSameAs(plugin);
    }

    @Test
    void isEnabled_shouldReturnTrueWhenStateIsEnabled() {
        var instance = new PluginInstance("p001", manifest, metadata);
        instance.setState(PluginState.ENABLED);
        assertThat(instance.isEnabled()).isTrue();
    }

    @Test
    void isEnabled_shouldReturnFalseWhenStateIsNotEnabled() {
        var instance = new PluginInstance("p001", manifest, metadata);
        assertThat(instance.isEnabled()).isFalse();
        instance.setState(PluginState.DISABLED);
        assertThat(instance.isEnabled()).isFalse();
    }

    @Test
    void isInstalled_shouldReturnTrueWhenStateIsNotUninstalled() {
        var instance = new PluginInstance("p001", manifest, metadata);
        assertThat(instance.isInstalled()).isTrue();
        instance.setState(PluginState.ENABLED);
        assertThat(instance.isInstalled()).isTrue();
    }

    @Test
    void isInstalled_shouldReturnFalseWhenStateIsUninstalled() {
        var instance = new PluginInstance("p001", manifest, metadata);
        instance.setState(PluginState.UNINSTALLED);
        assertThat(instance.isInstalled()).isFalse();
    }

    @Test
    void isInstalled_shouldReturnTrueWhenStateIsUninstalling() {
        var instance = new PluginInstance("p001", manifest, metadata);
        instance.setState(PluginState.UNINSTALLING);
        assertThat(instance.isInstalled()).isTrue();
    }
}
