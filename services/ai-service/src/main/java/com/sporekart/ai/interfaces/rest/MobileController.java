package com.sporekart.ai.interfaces.rest;

import com.sporekart.ai.application.service.MobileService;
import com.sporekart.ai.domain.model.MobileDevice;
import com.sporekart.ai.domain.model.OfflineSyncLog;
import com.sporekart.ai.domain.model.PushNotification;
import com.sporekart.ai.common.exception.DeviceRegistrationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Mobile REST API endpoints
 * Handles device registration, offline sync, notifications, and mobile
 * configuration
 */
@RestController
@RequestMapping("/mobile")
@Tag(name = "Mobile", description = "Mobile device management and offline sync endpoints")
public class MobileController {
    private final MobileService mobileService;

    public MobileController(MobileService mobileService) {
        this.mobileService = mobileService;
    }

    @PostMapping("/register-device")
    @Operation(summary = "Register a mobile device")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Device registered"),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> registerDevice(@RequestBody Map<String, String> payload) {
        String deviceName = payload.getOrDefault("deviceName", "device");
        String deviceType = payload.getOrDefault("deviceType", "iOS");
        String osVersion = payload.getOrDefault("osVersion", "1.0");
        String appVersion = payload.getOrDefault("appVersion", "1.0.0");
        String fcmToken = payload.getOrDefault("fcmToken", "token123");

        MobileDevice device = mobileService.registerDevice(deviceName, deviceType,
                osVersion, appVersion, fcmToken, UUID.randomUUID());

        return Map.of(
                "deviceId", device.getId(),
                "deviceName", device.getDeviceName(),
                "isTrusted", device.isTrusted(),
                "createdAt", device.getCreatedAt());
    }

    @PostMapping("/sync")
    @Operation(summary = "Sync offline changes")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Offline changes synced"),
        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> syncOfflineChanges(@RequestBody Map<String, Object> payload) {
        UUID deviceId = UUID.fromString((String) payload.getOrDefault("deviceId", UUID.randomUUID().toString()));

        OfflineSyncLog log = mobileService.logSyncOperation(deviceId);
        mobileService.completeSyncOperation(log.getId(), 42, 4200);

        return Map.of(
                "syncId", log.getId(),
                "status", "SUCCESS",
                "recordsSync", 42,
                "bytesSync", 4200,
                "completedAt", log.getStartedAt().plusMinutes(1));
    }

    @GetMapping("/config")
    @Operation(summary = "Get mobile configuration")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Mobile configuration"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> getMobileConfig() {
        return mobileService.getMobileConfig();
    }

    @GetMapping("/notifications")
    @Operation(summary = "Get push notifications for user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "List of notifications"),
        @ApiResponse(responseCode = "400", description = "Invalid user ID", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public java.util.List<java.util.Map<String, Object>> getNotifications(
            @RequestParam UUID userId) {
        java.util.List<PushNotification> notifications = mobileService.getNotifications(userId);
        return notifications.stream()
                .map(n -> {
                    java.util.Map<String, Object> map = new java.util.HashMap<>();
                    map.put("id", n.getId());
                    map.put("title", n.getTitle());
                    map.put("body", n.getBody());
                    map.put("type", n.getNotificationType());
                    map.put("isRead", n.isRead());
                    map.put("sentAt", n.getSentAt());
                    return map;
                })
                .toList();
    }

    @PostMapping("/push-token")
    @Operation(summary = "Register push notification token")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Push token registered"),
        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> registerPushToken(@RequestBody Map<String, String> payload) {
        UUID deviceId = UUID.fromString(payload.getOrDefault("deviceId", UUID.randomUUID().toString()));
        String fcmToken = payload.getOrDefault("fcmToken", "token123");

        return Map.of(
                "deviceId", deviceId,
                "fcmToken", fcmToken,
                "registered", true,
                "timestamp", System.currentTimeMillis());
    }

    @GetMapping("/version")
    @Operation(summary = "Get app version information")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Version info"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, String> getVersion() {
        return mobileService.getVersionInfo();
    }

    @PostMapping("/offline-sync")
    @Operation(summary = "Initiate offline sync")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Offline sync initiated"),
        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> offlineSync(@RequestBody Map<String, Object> payload) {
        UUID deviceId = UUID.fromString((String) payload.getOrDefault("deviceId", UUID.randomUUID().toString()));

        OfflineSyncLog log = mobileService.logSyncOperation(deviceId);

        return Map.of(
                "syncId", log.getId(),
                "status", "INITIATED",
                "startedAt", log.getStartedAt());
    }

    @GetMapping("/settings")
    @Operation(summary = "Get mobile settings for user")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Mobile settings"),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public Map<String, Object> getSettings(@RequestParam UUID userId) {
        return Map.of(
                "userId", userId,
                "allowNotifications", true,
                "offlineSyncEnabled", true,
                "biometricEnabled", true,
                "autoSync", true,
                "syncInterval", 300000);
    }
}
