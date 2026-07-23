package com.sporekart.marketplace.lifecycle;

import com.sporekart.marketplace.domain.PluginInstance;
import com.sporekart.marketplace.domain.PluginState;
import com.sporekart.marketplace.registry.PluginRegistry;
import com.sporekart.marketplace.sdk.PluginContext;
import com.sporekart.marketplace.sdk.SporekartPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class PluginLifecycleManager {

    private static final Logger log = LoggerFactory.getLogger(PluginLifecycleManager.class);

    private final PluginRegistry registry;
    private final PluginValidator validator;

    public PluginLifecycleManager(PluginRegistry registry, PluginValidator validator) {
        this.registry = registry;
        this.validator = validator;
    }

    public PluginInstance install(PluginInstance instance) {
        log.info("Installing plugin: {}", instance.getManifest().pluginId());
        var errors = validator.validate(instance.getManifest());
        if (!errors.isEmpty()) {
            instance.setState(PluginState.ERROR);
            registry.register(instance.getId(), instance);
            throw new IllegalStateException("Plugin validation failed: " + String.join(", ", errors));
        }
        registry.register(instance.getId(), instance);
        instance.setState(PluginState.INSTALLED);
        return instance;
    }

    public Optional<PluginInstance> enable(String pluginId, Map<String, Object> config) {
        log.info("Enabling plugin: {}", pluginId);
        return registry.get(pluginId).map(instance -> {
            var plugin = instance.getPlugin();
            if (plugin != null) {
                var context = new PluginContext(pluginId, config, Map.of());
                plugin.onEnable(context);
            }
            instance.setState(PluginState.ENABLED);
            return instance;
        });
    }

    public Optional<PluginInstance> disable(String pluginId) {
        log.info("Disabling plugin: {}", pluginId);
        return registry.get(pluginId).map(instance -> {
            var plugin = instance.getPlugin();
            if (plugin != null) {
                plugin.onDisable(new PluginContext(pluginId, Map.of(), Map.of()));
            }
            instance.setState(PluginState.DISABLED);
            return instance;
        });
    }

    public Optional<PluginInstance> uninstall(String pluginId) {
        log.info("Uninstalling plugin: {}", pluginId);
        return registry.get(pluginId).map(instance -> {
            var plugin = instance.getPlugin();
            if (plugin != null) {
                plugin.onUninstall(new PluginContext(pluginId, Map.of(), Map.of()));
            }
            instance.setState(PluginState.UNINSTALLED);
            registry.unregister(pluginId);
            return instance;
        });
    }

    public Optional<PluginInstance> upgrade(String pluginId, SporekartPlugin newPlugin, String previousVersion) {
        log.info("Upgrading plugin: {} from {}", pluginId, previousVersion);
        return registry.get(pluginId).map(instance -> {
            var context = new PluginContext(pluginId, Map.of(), Map.of());
            if (instance.getPlugin() != null) {
                instance.getPlugin().onDisable(context);
            }
            instance.setPlugin(newPlugin);
            if (newPlugin != null) {
                newPlugin.onUpgrade(context, previousVersion);
            }
            instance.setState(PluginState.ENABLED);
            return instance;
        });
    }

    public Optional<PluginInstance> downgrade(String pluginId, SporekartPlugin downgradedPlugin, String previousVersion) {
        log.info("Downgrading plugin: {} from {}", pluginId, previousVersion);
        return registry.get(pluginId).map(instance -> {
            var context = new PluginContext(pluginId, Map.of(), Map.of());
            if (instance.getPlugin() != null) {
                instance.getPlugin().onDisable(context);
            }
            instance.setPlugin(downgradedPlugin);
            if (downgradedPlugin != null) {
                downgradedPlugin.onDowngrade(context, previousVersion);
            }
            instance.setState(PluginState.ENABLED);
            return instance;
        });
    }

    public Optional<Map<String, Object>> executeAction(String pluginId, String action, Map<String, Object> params) {
        return registry.get(pluginId).filter(PluginInstance::isEnabled).map(instance -> {
            var plugin = instance.getPlugin();
            if (plugin != null) {
                return plugin.execute(action, params);
            }
            return Map.<String, Object>of("error", "Plugin not loaded");
        });
    }
}
