package com.sporekart.marketplace.loader;

import com.sporekart.marketplace.domain.PluginInstance;
import com.sporekart.marketplace.domain.PluginState;
import com.sporekart.marketplace.domain.PluginVersion;
import com.sporekart.marketplace.registry.PluginRegistry;
import com.sporekart.marketplace.sdk.*;
import com.sporekart.marketplace.lifecycle.PluginLifecycleManager;
import com.sporekart.marketplace.lifecycle.VersionManager;
import com.sporekart.marketplace.sandbox.PluginSandbox;
import com.sporekart.marketplace.sandbox.SandboxSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Component
public class PluginLoader {

    private static final Logger log = LoggerFactory.getLogger(PluginLoader.class);

    private final PluginRegistry registry;
    private final PluginLifecycleManager lifecycleManager;
    private final VersionManager versionManager;
    private final PluginSandbox sandbox;
    private final SandboxSecurityManager securityManager;

    public PluginLoader(PluginRegistry registry, PluginLifecycleManager lifecycleManager,
                        VersionManager versionManager, PluginSandbox sandbox,
                        SandboxSecurityManager securityManager) {
        this.registry = registry;
        this.lifecycleManager = lifecycleManager;
        this.versionManager = versionManager;
        this.sandbox = sandbox;
        this.securityManager = securityManager;
    }

    public PluginInstance loadPlugin(PluginManifest manifest, SporekartPlugin plugin) {
        var id = manifest.pluginId();
        log.info("Loading plugin: {} v{}", id, manifest.version());

        if (registry.exists(id)) {
            throw new IllegalStateException("Plugin already registered: " + id);
        }

        var metadata = new PluginMetadata(id, manifest.version(), Instant.now(), Instant.now(), null, "local", Map.of());
        var instance = new PluginInstance(id, manifest, metadata);
        instance.setPlugin(plugin);
        instance.setState(PluginState.INSTALLED);

        lifecycleManager.install(instance);

        versionManager.recordVersion(id, new PluginVersion(manifest.version(),
            manifest.minPlatformVersion(), manifest.maxPlatformVersion(), 100));

        securityManager.grantPermissions(id, manifest.requiredPermissions());
        sandbox.createSandbox(instance);

        log.info("Plugin loaded successfully: {}", id);
        return instance;
    }

    public void unloadPlugin(String pluginId) {
        log.info("Unloading plugin: {}", pluginId);
        lifecycleManager.uninstall(pluginId);
        securityManager.revokePermissions(pluginId);
        sandbox.destroySandbox(pluginId);
    }

    public void reloadPlugin(String pluginId, PluginManifest manifest, SporekartPlugin plugin) {
        unloadPlugin(pluginId);
        loadPlugin(manifest, plugin);
    }
}
