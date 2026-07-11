package com.sporekart.ai.application.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Mobile service unit tests
 */
@SpringBootTest
class MobileServiceTest {
    @Autowired
    private MobileService mobileService;

    @Test
    void shouldRegisterDevice() {
        UUID userId = UUID.randomUUID();
        var device = mobileService.registerDevice("iPhone 15", "iOS", "18.0", "1.0.0",
                "token123", userId);

        assertNotNull(device);
        assertEquals("iPhone 15", device.getDeviceName());
        assertEquals("iOS", device.getDeviceType());
        assertTrue(device.isActive());
        assertFalse(device.isTrusted());
    }

    @Test
    void shouldTrustDevice() {
        UUID userId = UUID.randomUUID();
        var device = mobileService.registerDevice("Device", "Android", "14.0", "1.0.0",
                "token456", userId);

        mobileService.trustDevice(device.getId());
        var retrieved = mobileService.getDevice(device.getId());

        assertTrue(retrieved.isTrusted());
    }

    @Test
    void shouldLogSyncOperation() {
        UUID userId = UUID.randomUUID();
        var device = mobileService.registerDevice("Device", "iOS", "18.0", "1.0.0",
                "token789", userId);

        var log = mobileService.logSyncOperation(device.getId());

        assertNotNull(log);
        assertEquals("IN_PROGRESS", log.getStatus());
    }

    @Test
    void shouldCompleteSyncOperation() {
        UUID userId = UUID.randomUUID();
        var device = mobileService.registerDevice("Device", "iOS", "18.0", "1.0.0",
                "token111", userId);
        var log = mobileService.logSyncOperation(device.getId());

        mobileService.completeSyncOperation(log.getId(), 42, 4200);

        assertEquals("SUCCESS", log.getStatus());
        assertEquals(42, log.getRecordsSync());
        assertEquals(4200, log.getBytesSync());
    }

    @Test
    void shouldSendNotification() {
        UUID userId = UUID.randomUUID();
        var device = mobileService.registerDevice("Device", "iOS", "18.0", "1.0.0",
                "token222", userId);

        var notification = mobileService.sendNotification(device.getId(), userId,
                "Order Update", "Your order has been shipped", "ORDER", null);

        assertNotNull(notification);
        assertEquals("Order Update", notification.getTitle());
        assertFalse(notification.isRead());
    }

    @Test
    void shouldGetMobileConfig() {
        Map<String, Object> config = mobileService.getMobileConfig();

        assertNotNull(config);
        assertTrue(config.containsKey("apiBaseUrl"));
        assertTrue(config.containsKey("biometricEnabled"));
        assertTrue(config.containsKey("features"));
    }

    @Test
    void shouldGetVersionInfo() {
        Map<String, String> version = mobileService.getVersionInfo();

        assertNotNull(version);
        assertEquals("1.0.0", version.get("appVersion"));
        assertEquals("1.0", version.get("apiVersion"));
    }
}
