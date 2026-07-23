package com.sporekart.marketplace.permission;

import com.sporekart.marketplace.sdk.PluginPermission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PermissionValidatorTest {

    @Mock
    private PermissionManager permissionManager;

    private PermissionValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PermissionValidator(permissionManager);
    }

    @Test
    void validateExecution_shouldReturnTrueWhenPermissionGranted() {
        when(permissionManager.hasPermission("plugin-a", PluginPermission.READ_PRODUCTS)).thenReturn(true);
        assertTrue(validator.validateExecution("plugin-a", PluginPermission.READ_PRODUCTS));
        verify(permissionManager).hasPermission("plugin-a", PluginPermission.READ_PRODUCTS);
    }

    @Test
    void validateExecution_shouldReturnFalseWhenPermissionDenied() {
        when(permissionManager.hasPermission("plugin-a", PluginPermission.READ_PRODUCTS)).thenReturn(false);
        assertFalse(validator.validateExecution("plugin-a", PluginPermission.READ_PRODUCTS));
    }

    @Test
    void validateBatch_shouldReturnTrueWhenAllPermissionsGranted() {
        when(permissionManager.hasPermission("plugin-a", PluginPermission.READ_PRODUCTS)).thenReturn(true);
        when(permissionManager.hasPermission("plugin-a", PluginPermission.EXECUTE_AI)).thenReturn(true);
        assertTrue(validator.validateBatch("plugin-a",
            List.of(PluginPermission.READ_PRODUCTS, PluginPermission.EXECUTE_AI)));
    }

    @Test
    void validateBatch_shouldReturnFalseWhenAnyPermissionMissing() {
        when(permissionManager.hasPermission("plugin-a", PluginPermission.READ_PRODUCTS)).thenReturn(true);
        when(permissionManager.hasPermission("plugin-a", PluginPermission.EXECUTE_AI)).thenReturn(false);
        assertFalse(validator.validateBatch("plugin-a",
            List.of(PluginPermission.READ_PRODUCTS, PluginPermission.EXECUTE_AI)));
    }

    @Test
    void validateBatch_shouldReturnFalseWhenAllPermissionsMissing() {
        when(permissionManager.hasPermission(anyString(), any())).thenReturn(false);
        assertFalse(validator.validateBatch("plugin-a",
            List.of(PluginPermission.READ_PRODUCTS, PluginPermission.EXECUTE_AI)));
    }

    @Test
    void getMissingPermissions_shouldReturnOnlyMissingOnes() {
        when(permissionManager.getApprovedPermissions("plugin-a"))
            .thenReturn(List.of(PluginPermission.READ_PRODUCTS));
        var missing = validator.getMissingPermissions("plugin-a",
            List.of(PluginPermission.READ_PRODUCTS, PluginPermission.EXECUTE_AI));
        assertEquals(1, missing.size());
        assertEquals(PluginPermission.EXECUTE_AI, missing.get(0));
    }

    @Test
    void getMissingPermissions_shouldReturnEmptyWhenAllGranted() {
        when(permissionManager.getApprovedPermissions("plugin-a"))
            .thenReturn(List.of(PluginPermission.READ_PRODUCTS, PluginPermission.EXECUTE_AI));
        var missing = validator.getMissingPermissions("plugin-a",
            List.of(PluginPermission.READ_PRODUCTS, PluginPermission.EXECUTE_AI));
        assertTrue(missing.isEmpty());
    }

    @Test
    void getMissingPermissions_shouldReturnAllWhenNoneGranted() {
        when(permissionManager.getApprovedPermissions("plugin-a"))
            .thenReturn(List.of());
        var missing = validator.getMissingPermissions("plugin-a",
            List.of(PluginPermission.READ_PRODUCTS, PluginPermission.EXECUTE_AI));
        assertEquals(2, missing.size());
    }
}
