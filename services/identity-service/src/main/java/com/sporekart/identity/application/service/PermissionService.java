package com.sporekart.identity.application.service;

import com.sporekart.identity.domain.model.Permission;
import com.sporekart.identity.domain.model.RoleType;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PermissionService {

    private static final Map<RoleType, Set<Permission>> ROLE_PERMISSIONS = new ConcurrentHashMap<>();

    static {
        ROLE_PERMISSIONS.put(RoleType.CUSTOMER, EnumSet.of(
                Permission.PRODUCT_READ, Permission.ORDER_READ, Permission.ORDER_WRITE));
        ROLE_PERMISSIONS.put(RoleType.GROWER, EnumSet.of(
                Permission.PRODUCT_READ, Permission.PRODUCT_WRITE,
                Permission.ORDER_READ, Permission.TRAINING_MANAGE));
        ROLE_PERMISSIONS.put(RoleType.SUPPORT, EnumSet.of(
                Permission.PRODUCT_READ, Permission.ORDER_READ, Permission.ORDER_WRITE,
                Permission.USER_MANAGE, Permission.AUDIT_VIEW));
        ROLE_PERMISSIONS.put(RoleType.OPERATIONS, EnumSet.of(
                Permission.PRODUCT_READ, Permission.PRODUCT_WRITE,
                Permission.ORDER_READ, Permission.ORDER_WRITE,
                Permission.USER_MANAGE, Permission.AUDIT_VIEW));
        ROLE_PERMISSIONS.put(RoleType.ADMIN, EnumSet.allOf(Permission.class));
        ROLE_PERMISSIONS.put(RoleType.SUPER_ADMIN, EnumSet.allOf(Permission.class));
    }

    public Set<String> getPermissionsForRoles(Set<RoleType> roles) {
        var permissions = new HashSet<String>();
        for (var role : roles) {
            var perms = ROLE_PERMISSIONS.get(role);
            if (perms != null) {
                perms.stream().map(Permission::name).forEach(permissions::add);
            }
        }
        return permissions;
    }

    public boolean hasPermission(Set<RoleType> userRoles, String permission) {
        var permissions = getPermissionsForRoles(userRoles);
        return permissions.contains(permission);
    }

    public boolean hasAnyPermission(Set<RoleType> userRoles, String... permissions) {
        var userPerms = getPermissionsForRoles(userRoles);
        return Arrays.stream(permissions).anyMatch(userPerms::contains);
    }

    public boolean hasAllPermissions(Set<RoleType> userRoles, String... permissions) {
        var userPerms = getPermissionsForRoles(userRoles);
        return Arrays.stream(permissions).allMatch(userPerms::contains);
    }

    public Set<String> getAllPermissions() {
        return Arrays.stream(Permission.values()).map(Permission::name).collect(java.util.stream.Collectors.toSet());
    }
}
