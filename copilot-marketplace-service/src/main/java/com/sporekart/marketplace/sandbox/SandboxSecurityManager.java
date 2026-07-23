package com.sporekart.marketplace.sandbox;

import com.sporekart.marketplace.sdk.PluginPermission;
import com.sporekart.marketplace.sdk.PluginManifest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SandboxSecurityManager {

    private static final Logger log = LoggerFactory.getLogger(SandboxSecurityManager.class);

    private final Map<String, Set<PluginPermission>> grantedPermissions = new HashMap<>();

    public void grantPermissions(String pluginId, List<PluginPermission> permissions) {
        grantedPermissions.computeIfAbsent(pluginId, k -> new HashSet<>()).addAll(permissions);
        log.info("Permissions granted for {}: {}", pluginId, permissions);
    }

    public void revokePermissions(String pluginId) {
        grantedPermissions.remove(pluginId);
        log.info("Permissions revoked for {}", pluginId);
    }

    public boolean hasPermission(String pluginId, PluginPermission permission) {
        return grantedPermissions.getOrDefault(pluginId, Set.of()).contains(permission);
    }

    public boolean validateManifestPermissions(PluginManifest manifest) {
        if (manifest.requiredPermissions() == null) return false;
        return manifest.requiredPermissions().stream()
            .allMatch(p -> p != null);
    }

    public List<PluginPermission> getGrantedPermissions(String pluginId) {
        return List.copyOf(grantedPermissions.getOrDefault(pluginId, Set.of()));
    }

    public Set<String> getPluginsWithPermission(PluginPermission permission) {
        var result = new HashSet<String>();
        grantedPermissions.forEach((id, perms) -> {
            if (perms.contains(permission)) result.add(id);
        });
        return result;
    }
}
