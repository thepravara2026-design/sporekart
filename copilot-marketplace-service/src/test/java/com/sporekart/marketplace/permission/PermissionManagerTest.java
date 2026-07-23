package com.sporekart.marketplace.permission;

import com.sporekart.marketplace.sdk.PluginPermission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PermissionManagerTest {

    private PermissionManager manager;

    @BeforeEach
    void setUp() {
        manager = new PermissionManager();
    }

    @Test
    void requestApproval_shouldDelegateToApproveAndReturnTrue() {
        var result = manager.requestApproval("plugin-a", PluginPermission.READ_PRODUCTS);
        assertTrue(result);
        assertTrue(manager.hasPermission("plugin-a", PluginPermission.READ_PRODUCTS));
    }

    @Test
    void approve_shouldAddPermissionAndReturnTrue() {
        var result = manager.approve("plugin-a", PluginPermission.READ_PRODUCTS);
        assertTrue(result);
        assertTrue(manager.hasPermission("plugin-a", PluginPermission.READ_PRODUCTS));
    }

    @Test
    void approve_shouldAllowDuplicateApprovals() {
        manager.approve("plugin-a", PluginPermission.READ_PRODUCTS);
        manager.approve("plugin-a", PluginPermission.READ_PRODUCTS);
        assertTrue(manager.hasPermission("plugin-a", PluginPermission.READ_PRODUCTS));
    }

    @Test
    void revoke_shouldRemovePermissionAndReturnTrue() {
        manager.approve("plugin-a", PluginPermission.READ_PRODUCTS);
        var result = manager.revoke("plugin-a", PluginPermission.READ_PRODUCTS);
        assertTrue(result);
        assertFalse(manager.hasPermission("plugin-a", PluginPermission.READ_PRODUCTS));
    }

    @Test
    void revoke_shouldReturnFalseWhenPluginIdNotFound() {
        assertFalse(manager.revoke("nonexistent", PluginPermission.READ_PRODUCTS));
    }

    @Test
    void revoke_shouldReturnFalseWhenPermissionNotApproved() {
        manager.approve("plugin-a", PluginPermission.READ_PRODUCTS);
        assertFalse(manager.revoke("plugin-a", PluginPermission.EXECUTE_AI));
    }

    @Test
    void hasPermission_shouldReturnTrueWhenApproved() {
        manager.approve("plugin-a", PluginPermission.READ_PRODUCTS);
        assertTrue(manager.hasPermission("plugin-a", PluginPermission.READ_PRODUCTS));
    }

    @Test
    void hasPermission_shouldReturnFalseWhenNotApproved() {
        assertFalse(manager.hasPermission("plugin-a", PluginPermission.READ_PRODUCTS));
    }

    @Test
    void hasPermission_shouldReturnFalseForUnknownPlugin() {
        assertFalse(manager.hasPermission("unknown", PluginPermission.READ_PRODUCTS));
    }

    @Test
    void hasAllPermissions_shouldReturnTrueWhenAllGranted() {
        manager.approve("plugin-a", PluginPermission.READ_PRODUCTS);
        manager.approve("plugin-a", PluginPermission.EXECUTE_AI);
        assertTrue(manager.hasAllPermissions("plugin-a",
            List.of(PluginPermission.READ_PRODUCTS, PluginPermission.EXECUTE_AI)));
    }

    @Test
    void hasAllPermissions_shouldReturnFalseWhenAnyMissing() {
        manager.approve("plugin-a", PluginPermission.READ_PRODUCTS);
        assertFalse(manager.hasAllPermissions("plugin-a",
            List.of(PluginPermission.READ_PRODUCTS, PluginPermission.EXECUTE_AI)));
    }

    @Test
    void hasAllPermissions_shouldReturnTrueForEmptyList() {
        assertTrue(manager.hasAllPermissions("plugin-a", List.of()));
    }

    @Test
    void getApprovedPermissions_shouldReturnAllApproved() {
        manager.approve("plugin-a", PluginPermission.READ_PRODUCTS);
        manager.approve("plugin-a", PluginPermission.EXECUTE_AI);
        var perms = manager.getApprovedPermissions("plugin-a");
        assertTrue(perms.contains(PluginPermission.READ_PRODUCTS));
        assertTrue(perms.contains(PluginPermission.EXECUTE_AI));
        assertEquals(2, perms.size());
    }

    @Test
    void getApprovedPermissions_shouldReturnEmptyForUnknownPlugin() {
        assertTrue(manager.getApprovedPermissions("unknown").isEmpty());
    }

    @Test
    void revokeAll_shouldClearPermissionsAndConfig() {
        manager.approve("plugin-a", PluginPermission.READ_PRODUCTS);
        manager.setPermissionConfig("plugin-a", "key1", "value1");
        manager.revokeAll("plugin-a");
        assertFalse(manager.hasPermission("plugin-a", PluginPermission.READ_PRODUCTS));
        assertTrue(manager.getPermissionConfig("plugin-a").isEmpty());
    }

    @Test
    void setPermissionConfig_shouldStoreConfig() {
        manager.setPermissionConfig("plugin-a", "timeout", 5000);
        manager.setPermissionConfig("plugin-a", "retry", true);
        var config = manager.getPermissionConfig("plugin-a");
        assertEquals(5000, config.get("timeout"));
        assertEquals(true, config.get("retry"));
        assertEquals(2, config.size());
    }

    @Test
    void getPermissionConfig_shouldReturnEmptyForUnknownPlugin() {
        assertTrue(manager.getPermissionConfig("unknown").isEmpty());
    }

    @Test
    void getAllApprovals_shouldReturnAllPluginApprovals() {
        manager.approve("plugin-a", PluginPermission.READ_PRODUCTS);
        manager.approve("plugin-b", PluginPermission.EXECUTE_AI);
        var all = manager.getAllApprovals();
        assertEquals(2, all.size());
        assertTrue(all.containsKey("plugin-a"));
        assertTrue(all.containsKey("plugin-b"));
    }
}
