package com.sporekart.copilot.permission;

import com.sporekart.copilot.context.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class PermissionCheckerImpl implements PermissionChecker {

    private static final Logger log = LoggerFactory.getLogger(PermissionCheckerImpl.class);

    private final ConcurrentMap<String, Boolean> permissionCache = new ConcurrentHashMap<>();

    public PermissionCheckerImpl() {
        log.debug("PermissionCheckerImpl initialized");
    }

    @Override
    public boolean hasPermission(UserContext user, String resource, String action) {
        log.debug("Checking permission: userId={}, resource={}, action={}", user.userId(), resource, action);

        if (user.roles() == null || user.roles().isEmpty()) {
            log.debug("No roles assigned to user: {}", user.userId());
            return false;
        }

        for (String role : user.roles()) {
            String cacheKey = role + ":" + resource + ":" + action;
            Boolean cached = permissionCache.get(cacheKey);
            if (cached != null) {
                if (cached) return true;
                continue;
            }

            boolean granted = evaluateRolePermission(role, resource, action);
            permissionCache.put(cacheKey, granted);
            if (granted) {
                log.debug("Permission granted via role '{}': resource={}, action={}", role, resource, action);
                return true;
            }
        }

        log.debug("Permission denied: userId={}, resource={}, action={}", user.userId(), resource, action);
        return false;
    }

    @Override
    public List<CopilotPermission> getPermissions(UserContext user) {
        if (user.roles() == null || user.roles().isEmpty()) {
            return Collections.emptyList();
        }

        List<CopilotPermission> permissions = new ArrayList<>();

        for (String role : user.roles()) {
            if (role.equals("admin") || role.equals("ADMIN")) {
                permissions.add(new CopilotPermission("*", "*", true));
                return List.copyOf(permissions);
            }

            String[] parts = role.split(":");
            if (parts.length == 2) {
                permissions.add(new CopilotPermission(parts[0], parts[1], true));
            } else {
                String resource = role.contains("_") ? role.split("_", 2)[1] : role;
                permissions.add(new CopilotPermission(resource, "*", true));
            }
        }

        return List.copyOf(permissions);
    }

    private boolean evaluateRolePermission(String role, String resource, String action) {
        if (role.equals("admin") || role.equals("ADMIN") || role.equals("ROLE_ADMIN")) {
            return true;
        }

        if (role.endsWith(":*")) {
            String roleResource = role.substring(0, role.length() - 2);
            if (resource.startsWith(roleResource)) {
                return true;
            }
        }

        String expectedRole = action + ":" + resource;
        if (role.equals(expectedRole)) {
            return true;
        }

        String[] roleParts = role.split(":");
        String[] resParts = resource.split(":");
        if (roleParts.length == 2 && resParts.length >= 1) {
            return roleParts[0].equalsIgnoreCase(action)
                && (roleParts[1].equals("*") || roleParts[1].equals(resParts[0]));
        }

        return false;
    }
}
