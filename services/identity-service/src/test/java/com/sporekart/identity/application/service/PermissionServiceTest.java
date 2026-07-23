package com.sporekart.identity.application.service;

import com.sporekart.identity.domain.model.RoleType;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PermissionServiceTest {

    private final PermissionService permissionService = new PermissionService();

    @Test
    void shouldReturnPermissionsForCustomerRole() {
        var permissions = permissionService.getPermissionsForRoles(Set.of(RoleType.CUSTOMER));

        assertTrue(permissions.contains("PRODUCT_READ"));
        assertTrue(permissions.contains("ORDER_READ"));
        assertTrue(permissions.contains("ORDER_WRITE"));
        assertEquals(3, permissions.size());
    }

    @Test
    void shouldReturnPermissionsForAdminRole() {
        var permissions = permissionService.getPermissionsForRoles(Set.of(RoleType.ADMIN));

        assertTrue(permissions.contains("SYSTEM_CONFIG"));
        assertTrue(permissions.contains("USER_MANAGE"));
        assertTrue(permissions.contains("PAYMENT_MANAGE"));
        assertEquals(permissionService.getAllPermissions().size(), permissions.size());
    }

    @Test
    void shouldReturnAllPermissionsForSuperAdmin() {
        var permissions = permissionService.getPermissionsForRoles(Set.of(RoleType.SUPER_ADMIN));

        assertEquals(permissionService.getAllPermissions(), permissions);
    }

    @Test
    void shouldCheckHasPermission() {
        assertTrue(permissionService.hasPermission(Set.of(RoleType.CUSTOMER), "PRODUCT_READ"));
        assertFalse(permissionService.hasPermission(Set.of(RoleType.CUSTOMER), "SYSTEM_CONFIG"));
    }

    @Test
    void shouldCheckHasAnyPermission() {
        assertTrue(permissionService.hasAnyPermission(Set.of(RoleType.CUSTOMER), "ORDER_READ", "SYSTEM_CONFIG"));
        assertFalse(permissionService.hasAnyPermission(Set.of(RoleType.CUSTOMER), "SYSTEM_CONFIG", "ROLE_MANAGE"));
    }

    @Test
    void shouldCheckHasAllPermissions() {
        assertTrue(permissionService.hasAllPermissions(Set.of(RoleType.CUSTOMER), "PRODUCT_READ", "ORDER_READ"));
        assertFalse(permissionService.hasAllPermissions(Set.of(RoleType.CUSTOMER), "PRODUCT_READ", "SYSTEM_CONFIG"));
    }

    @Test
    void customerShouldNotHaveAdminPermission() {
        var customerPerms = permissionService.getPermissionsForRoles(Set.of(RoleType.CUSTOMER));
        var adminPerms = permissionService.getPermissionsForRoles(Set.of(RoleType.ADMIN));

        assertTrue(adminPerms.containsAll(customerPerms));
        assertTrue(adminPerms.size() > customerPerms.size());
        assertFalse(customerPerms.contains("SYSTEM_CONFIG"));
        assertFalse(customerPerms.contains("USER_MANAGE"));
        assertFalse(customerPerms.contains("ROLE_MANAGE"));
    }
}
