package com.sporekart.ai.application.service;

import com.sporekart.ai.domain.model.DeviceSession;
import com.sporekart.ai.domain.model.MobileDevice;
import com.sporekart.ai.domain.model.OfflineSyncLog;
import com.sporekart.ai.domain.model.PushNotification;
import com.sporekart.ai.common.exception.DeviceRegistrationException;
import com.sporekart.ai.common.exception.OfflineSyncException;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Mobile platform service
 * Handles device registration, session management, offline sync, and push
 * notifications
 */
@Service
public class MobileService {
    private final List<MobileDevice> devices = new ArrayList<>();
    private final List<DeviceSession> sessions = new ArrayList<>();
    private final List<OfflineSyncLog> syncLogs = new ArrayList<>();
    private final List<PushNotification> notifications = new ArrayList<>();

    /**
     * Register a mobile device
     */
    public MobileDevice registerDevice(String deviceName, String deviceType,
            String osVersion, String appVersion,
            String fcmToken, UUID userId) {
        MobileDevice device = new MobileDevice(UUID.randomUUID(), userId, deviceName,
                deviceType, osVersion, appVersion, fcmToken);
        devices.add(device);
        return device;
    }

    /**
     * Get device by ID
     */
    public MobileDevice getDevice(UUID deviceId) {
        return devices.stream()
                .filter(d -> d.getId().equals(deviceId))
                .findFirst()
                .orElseThrow(() -> new DeviceRegistrationException("Device not found"));
    }

    /**
     * List devices for user
     */
    public List<MobileDevice> listDevices(UUID userId) {
        return devices.stream()
                .filter(d -> d.getUserId().equals(userId))
                .toList();
    }

    /**
     * Trust a device (skip OTP next time)
     */
    public void trustDevice(UUID deviceId) {
        MobileDevice device = getDevice(deviceId);
        device.setTrusted(true);
    }

    /**
     * Create a device session
     */
    public DeviceSession createSession(UUID deviceId, UUID userId, String token,
            String refreshToken, int expiresInSeconds) {
        DeviceSession session = new DeviceSession(UUID.randomUUID(), deviceId, userId,
                token, refreshToken,
                OffsetDateTime.now().plusSeconds(expiresInSeconds));
        sessions.add(session);

        // Update device last login
        MobileDevice device = getDevice(deviceId);
        device.setLastLogin(OffsetDateTime.now());

        return session;
    }

    /**
     * Get active session for device
     */
    public DeviceSession getActiveSession(UUID deviceId) {
        return sessions.stream()
                .filter(s -> s.getDeviceId().equals(deviceId) && s.isActive())
                .findFirst()
                .orElseThrow(() -> new DeviceRegistrationException("No active session"));
    }

    /**
     * Log offline sync operation
     */
    public OfflineSyncLog logSyncOperation(UUID deviceId) {
        OfflineSyncLog log = new OfflineSyncLog(UUID.randomUUID(), deviceId, "IN_PROGRESS",
                OffsetDateTime.now());
        syncLogs.add(log);
        return log;
    }

    /**
     * Complete sync operation successfully
     */
    public void completeSyncOperation(UUID syncLogId, int recordsCount, int bytesCount) {
        OfflineSyncLog log = syncLogs.stream()
                .filter(l -> l.getId().equals(syncLogId))
                .findFirst()
                .orElseThrow(() -> new OfflineSyncException("Sync log not found"));
        log.complete(recordsCount, bytesCount);
    }

    /**
     * Fail sync operation
     */
    public void failSyncOperation(UUID syncLogId, String errorMessage) {
        OfflineSyncLog log = syncLogs.stream()
                .filter(l -> l.getId().equals(syncLogId))
                .findFirst()
                .orElseThrow(() -> new OfflineSyncException("Sync log not found"));
        log.fail(errorMessage);
    }

    /**
     * List sync logs for device
     */
    public List<OfflineSyncLog> listSyncLogs(UUID deviceId) {
        return syncLogs.stream()
                .filter(l -> l.getDeviceId().equals(deviceId))
                .toList();
    }

    /**
     * Send push notification
     */
    public PushNotification sendNotification(UUID deviceId, UUID userId, String title,
            String body, String type, String actionUrl) {
        PushNotification notification = new PushNotification(UUID.randomUUID(), deviceId,
                userId, title, body, type);
        if (actionUrl != null) {
            notification.setActionUrl(actionUrl);
        }
        notifications.add(notification);
        return notification;
    }

    /**
     * Get notifications for user
     */
    public List<PushNotification> getNotifications(UUID userId) {
        return notifications.stream()
                .filter(n -> n.getUserId().equals(userId) && !n.isArchived())
                .toList();
    }

    /**
     * Mark notification as read
     */
    public void markNotificationRead(UUID notificationId) {
        PushNotification notification = notifications.stream()
                .filter(n -> n.getId().equals(notificationId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.markRead();
    }

    /**
     * Get mobile configuration
     */
    public Map<String, Object> getMobileConfig() {
        return Map.of(
                "apiBaseUrl", "https://api.sporekart.com",
                "firebaseProjectId", "sporekart-prod",
                "biometricEnabled", true,
                "offlineSyncInterval", 300000,
                "requestTimeout", 30000,
                "features", Map.of(
                        "offlineMode", true,
                        "biometricAuth", true,
                        "pushNotifications", true,
                        "aiAssistant", true,
                        "qrScanner", true,
                        "barcode", true));
    }

    /**
     * Get app version info
     */
    public Map<String, String> getVersionInfo() {
        return Map.of(
                "appVersion", "1.0.0",
                "buildNumber", "1",
                "apiVersion", "1.0",
                "releaseDate", "2026-07-10");
    }
}

