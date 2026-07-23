package com.sporekart.marketplace.permission;

import com.sporekart.marketplace.sdk.PluginPermission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PermissionValidator {

    private static final Logger log = LoggerFactory.getLogger(PermissionValidator.class);

    private final PermissionManager permissionManager;

    public PermissionValidator(PermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }

    public boolean validateExecution(String pluginId, PluginPermission requiredPermission) {
        if (!permissionManager.hasPermission(pluginId, requiredPermission)) {
            log.warn("Permission denied: {} for plugin {} - missing permission {}", requiredPermission, pluginId, requiredPermission);
            return false;
        }
        return true;
    }

    public boolean validateBatch(String pluginId, List<PluginPermission> requiredPermissions) {
        var missing = requiredPermissions.stream()
            .filter(p -> !permissionManager.hasPermission(pluginId, p))
            .toList();
        if (!missing.isEmpty()) {
            log.warn("Permission denied for plugin {}: missing {}", pluginId, missing);
            return false;
        }
        return true;
    }

    public List<PluginPermission> getMissingPermissions(String pluginId, List<PluginPermission> required) {
        var granted = permissionManager.getApprovedPermissions(pluginId);
        return required.stream()
            .filter(p -> !granted.contains(p))
            .toList();
    }
}
