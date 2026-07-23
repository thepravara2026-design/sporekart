package com.sporekart.marketplace.permission;

import com.sporekart.marketplace.sdk.PluginPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PermissionManager {

    private static final Logger log = LoggerFactory.getLogger(PermissionManager.class);

    private final ConcurrentHashMap<String, Set<PluginPermission>> approvals = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Map<String, Object>> permissionConfig = new ConcurrentHashMap<>();

    public boolean requestApproval(String pluginId, PluginPermission permission) {
        log.info("Permission requested: {} for plugin {}", permission, pluginId);
        return approve(pluginId, permission);
    }

    public boolean approve(String pluginId, PluginPermission permission) {
        approvals.computeIfAbsent(pluginId, k -> new HashSet<>()).add(permission);
        log.info("Permission approved: {} for plugin {}", permission, pluginId);
        return true;
    }

    public boolean revoke(String pluginId, PluginPermission permission) {
        var perms = approvals.get(pluginId);
        if (perms != null && perms.remove(permission)) {
            log.info("Permission revoked: {} for plugin {}", permission, pluginId);
            return true;
        }
        return false;
    }

    public boolean hasPermission(String pluginId, PluginPermission permission) {
        return approvals.getOrDefault(pluginId, Set.of()).contains(permission);
    }

    public boolean hasAllPermissions(String pluginId, List<PluginPermission> permissions) {
        var granted = approvals.getOrDefault(pluginId, Set.of());
        return permissions.stream().allMatch(granted::contains);
    }

    public List<PluginPermission> getApprovedPermissions(String pluginId) {
        return List.copyOf(approvals.getOrDefault(pluginId, Set.of()));
    }

    public void revokeAll(String pluginId) {
        approvals.remove(pluginId);
        permissionConfig.remove(pluginId);
        log.info("All permissions revoked for plugin {}", pluginId);
    }

    public void setPermissionConfig(String pluginId, String key, Object value) {
        permissionConfig.computeIfAbsent(pluginId, k -> new HashMap<>()).put(key, value);
    }

    public Map<String, Object> getPermissionConfig(String pluginId) {
        return permissionConfig.getOrDefault(pluginId, Map.of());
    }

    public Map<String, List<PluginPermission>> getAllApprovals() {
        var result = new LinkedHashMap<String, List<PluginPermission>>();
        approvals.forEach((id, perms) -> result.put(id, List.copyOf(perms)));
        return result;
    }
}
