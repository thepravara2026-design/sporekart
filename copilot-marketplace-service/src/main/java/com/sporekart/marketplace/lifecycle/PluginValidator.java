package com.sporekart.marketplace.lifecycle;

import com.sporekart.marketplace.sdk.PluginManifest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PluginValidator {

    private static final Logger log = LoggerFactory.getLogger(PluginValidator.class);

    private final VersionManager versionManager;

    public PluginValidator(VersionManager versionManager) {
        this.versionManager = versionManager;
    }

    public List<String> validate(PluginManifest manifest) {
        var errors = new ArrayList<String>();

        if (manifest.pluginId() == null || manifest.pluginId().isBlank()) {
            errors.add("pluginId is required");
        }
        if (manifest.name() == null || manifest.name().isBlank()) {
            errors.add("name is required");
        }
        if (manifest.version() == null || manifest.version().isBlank()) {
            errors.add("version is required");
        }
        if (manifest.type() == null) {
            errors.add("type is required");
        }
        if (manifest.author() == null || manifest.author().isBlank()) {
            errors.add("author is required");
        }
        if (manifest.entryPoint() == null || manifest.entryPoint().isBlank()) {
            errors.add("entryPoint is required");
        }
        if (manifest.capabilities() == null || manifest.capabilities().isEmpty()) {
            errors.add("at least one capability is required");
        }
        if (manifest.minPlatformVersion() != null && !manifest.minPlatformVersion().isBlank()) {
            if (!versionManager.isCompatible(manifest.minPlatformVersion(), manifest.maxPlatformVersion())) {
                errors.add("platform version incompatibility: min=" + manifest.minPlatformVersion()
                    + " max=" + manifest.maxPlatformVersion());
            }
        }
        if (manifest.requiredPermissions() == null) {
            errors.add("requiredPermissions must not be null");
        }

        if (!errors.isEmpty()) {
            log.warn("Plugin validation failed for {}: {}", manifest.pluginId(), errors);
        }
        return errors;
    }
}
