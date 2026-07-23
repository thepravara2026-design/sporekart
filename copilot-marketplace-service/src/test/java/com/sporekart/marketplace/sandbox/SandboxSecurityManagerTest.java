package com.sporekart.marketplace.sandbox;

import com.sporekart.marketplace.sdk.PluginManifest;
import com.sporekart.marketplace.sdk.PluginPermission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SandboxSecurityManagerTest {

    private SandboxSecurityManager securityManager;

    @BeforeEach
    void setUp() {
        securityManager = new SandboxSecurityManager();
    }

    @Test
    void grantPermissions_shouldAddPermissions() {
        securityManager.grantPermissions("plugin1", List.of(PluginPermission.READ_PRODUCTS, PluginPermission.READ_ORDERS));

        assertTrue(securityManager.hasPermission("plugin1", PluginPermission.READ_PRODUCTS));
        assertTrue(securityManager.hasPermission("plugin1", PluginPermission.READ_ORDERS));
    }

    @Test
    void grantPermissions_shouldAppendToExisting() {
        securityManager.grantPermissions("plugin1", List.of(PluginPermission.READ_PRODUCTS));
        securityManager.grantPermissions("plugin1", List.of(PluginPermission.READ_ORDERS));

        assertTrue(securityManager.hasPermission("plugin1", PluginPermission.READ_PRODUCTS));
        assertTrue(securityManager.hasPermission("plugin1", PluginPermission.READ_ORDERS));
    }

    @Test
    void grantPermissions_shouldNotCreateDuplicates() {
        securityManager.grantPermissions("plugin1", List.of(PluginPermission.READ_PRODUCTS));
        securityManager.grantPermissions("plugin1", List.of(PluginPermission.READ_PRODUCTS));

        var granted = securityManager.getGrantedPermissions("plugin1");
        assertEquals(1, granted.size());
    }

    @Test
    void grantPermissions_shouldNotAffectOtherPlugins() {
        securityManager.grantPermissions("plugin1", List.of(PluginPermission.READ_PRODUCTS));
        securityManager.grantPermissions("plugin2", List.of(PluginPermission.READ_ANALYTICS));

        assertFalse(securityManager.hasPermission("plugin2", PluginPermission.READ_PRODUCTS));
        assertFalse(securityManager.hasPermission("plugin1", PluginPermission.READ_ANALYTICS));
    }

    @Test
    void revokePermissions_shouldRemoveAllPermissions() {
        securityManager.grantPermissions("plugin1", List.of(PluginPermission.READ_PRODUCTS, PluginPermission.READ_ORDERS));
        securityManager.revokePermissions("plugin1");

        assertFalse(securityManager.hasPermission("plugin1", PluginPermission.READ_PRODUCTS));
        assertFalse(securityManager.hasPermission("plugin1", PluginPermission.READ_ORDERS));
    }

    @Test
    void revokePermissions_shouldDoNothingForUnknownPlugin() {
        securityManager.revokePermissions("unknown");
        assertFalse(securityManager.hasPermission("unknown", PluginPermission.READ_PRODUCTS));
    }

    @Test
    void revokePermissions_shouldNotAffectOtherPlugins() {
        securityManager.grantPermissions("plugin1", List.of(PluginPermission.READ_PRODUCTS));
        securityManager.grantPermissions("plugin2", List.of(PluginPermission.READ_ANALYTICS));
        securityManager.revokePermissions("plugin1");

        assertFalse(securityManager.hasPermission("plugin1", PluginPermission.READ_PRODUCTS));
        assertTrue(securityManager.hasPermission("plugin2", PluginPermission.READ_ANALYTICS));
    }

    @Test
    void hasPermission_shouldReturnFalseWhenNotGranted() {
        assertFalse(securityManager.hasPermission("plugin1", PluginPermission.READ_PRODUCTS));
    }

    @Test
    void hasPermission_shouldReturnFalseForUnknownPlugin() {
        securityManager.grantPermissions("plugin1", List.of(PluginPermission.READ_PRODUCTS));
        assertFalse(securityManager.hasPermission("plugin2", PluginPermission.READ_PRODUCTS));
    }

    @Test
    void hasPermission_shouldReturnTrueWhenGranted() {
        securityManager.grantPermissions("plugin1", List.of(PluginPermission.EXECUTE_AI));
        assertTrue(securityManager.hasPermission("plugin1", PluginPermission.EXECUTE_AI));
    }

    @Test
    void validateManifestPermissions_shouldReturnTrueWhenAllPermissionsValid() {
        var manifest = new PluginManifest(
            "p1", "Test", "1.0.0", "a", "d",
            null, null,
            List.of(PluginPermission.READ_PRODUCTS, PluginPermission.READ_ORDERS),
            null, null, null, null, null, null
        );
        assertTrue(securityManager.validateManifestPermissions(manifest));
    }

    @Test
    void validateManifestPermissions_shouldReturnFalseWhenPermissionsIsNull() {
        var manifest = new PluginManifest(
            "p1", "Test", "1.0.0", "a", "d",
            null, null, null,
            null, null, null, null, null, null
        );
        assertFalse(securityManager.validateManifestPermissions(manifest));
    }

    @Test
    void validateManifestPermissions_shouldReturnTrueWhenPermissionsIsEmpty() {
        var manifest = new PluginManifest(
            "p1", "Test", "1.0.0", "a", "d",
            null, null, List.of(),
            null, null, null, null, null, null
        );
        assertTrue(securityManager.validateManifestPermissions(manifest));
    }

    @Test
    void getGrantedPermissions_shouldReturnEmptyListForUnknownPlugin() {
        var permissions = securityManager.getGrantedPermissions("unknown");
        assertTrue(permissions.isEmpty());
    }

    @Test
    void getGrantedPermissions_shouldReturnAllGrantedPermissions() {
        securityManager.grantPermissions("plugin1", List.of(
            PluginPermission.READ_PRODUCTS,
            PluginPermission.READ_ORDERS,
            PluginPermission.READ_INVENTORY
        ));

        var permissions = securityManager.getGrantedPermissions("plugin1");
        assertEquals(3, permissions.size());
        assertTrue(permissions.contains(PluginPermission.READ_PRODUCTS));
        assertTrue(permissions.contains(PluginPermission.READ_ORDERS));
        assertTrue(permissions.contains(PluginPermission.READ_INVENTORY));
    }

    @Test
    void getGrantedPermissions_shouldReturnUnmodifiableList() {
        securityManager.grantPermissions("plugin1", List.of(PluginPermission.READ_PRODUCTS));
        var permissions = securityManager.getGrantedPermissions("plugin1");
        assertThrows(UnsupportedOperationException.class, () -> permissions.add(PluginPermission.READ_ORDERS));
    }

    @Test
    void getPluginsWithPermission_shouldReturnPluginsHavingPermission() {
        securityManager.grantPermissions("plugin1", List.of(PluginPermission.READ_PRODUCTS));
        securityManager.grantPermissions("plugin2", List.of(PluginPermission.READ_PRODUCTS, PluginPermission.READ_ORDERS));
        securityManager.grantPermissions("plugin3", List.of(PluginPermission.READ_ORDERS));

        var withReadProducts = securityManager.getPluginsWithPermission(PluginPermission.READ_PRODUCTS);
        assertEquals(Set.of("plugin1", "plugin2"), withReadProducts);

        var withReadOrders = securityManager.getPluginsWithPermission(PluginPermission.READ_ORDERS);
        assertEquals(Set.of("plugin2", "plugin3"), withReadOrders);
    }

    @Test
    void getPluginsWithPermission_shouldReturnEmptyWhenNoneHavePermission() {
        securityManager.grantPermissions("plugin1", List.of(PluginPermission.READ_PRODUCTS));
        var result = securityManager.getPluginsWithPermission(PluginPermission.EXECUTE_AI);
        assertTrue(result.isEmpty());
    }

    @Test
    void getPluginsWithPermission_shouldReturnEmptyWhenNoPlugins() {
        var result = securityManager.getPluginsWithPermission(PluginPermission.READ_PRODUCTS);
        assertTrue(result.isEmpty());
    }

    @Test
    void lifecycle_grantThenRevokeThenGrant() {
        securityManager.grantPermissions("p1", List.of(PluginPermission.READ_PRODUCTS));
        assertTrue(securityManager.hasPermission("p1", PluginPermission.READ_PRODUCTS));

        securityManager.revokePermissions("p1");
        assertFalse(securityManager.hasPermission("p1", PluginPermission.READ_PRODUCTS));

        securityManager.grantPermissions("p1", List.of(PluginPermission.READ_ORDERS));
        assertTrue(securityManager.hasPermission("p1", PluginPermission.READ_ORDERS));
        assertFalse(securityManager.hasPermission("p1", PluginPermission.READ_PRODUCTS));
    }

    @Test
    void getGrantedPermissions_shouldReturnEmptyAfterRevoke() {
        securityManager.grantPermissions("p1", List.of(PluginPermission.READ_PRODUCTS));
        securityManager.revokePermissions("p1");
        assertTrue(securityManager.getGrantedPermissions("p1").isEmpty());
    }
}
