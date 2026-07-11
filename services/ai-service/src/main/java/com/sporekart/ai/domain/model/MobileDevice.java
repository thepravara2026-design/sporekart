package com.sporekart.ai.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class MobileDevice {
    private UUID id;
    private UUID userId;
    private String deviceName;
    private String deviceType;
    private String osVersion;
    private String appVersion;
    private String fcmToken;
    private boolean isActive;
    private boolean isTrusted;
    private OffsetDateTime lastLogin;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public MobileDevice(UUID id, UUID userId, String deviceName, String deviceType,
            String osVersion, String appVersion, String fcmToken) {
        this.id = id;
        this.userId = userId;
        this.deviceName = deviceName;
        this.deviceType = deviceType;
        this.osVersion = osVersion;
        this.appVersion = appVersion;
        this.fcmToken = fcmToken;
        this.isActive = true;
        this.isTrusted = false;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isTrusted() {
        return isTrusted;
    }

    public OffsetDateTime getLastLogin() {
        return lastLogin;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setTrusted(boolean trusted) {
        this.isTrusted = trusted;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public void setLastLogin(OffsetDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
}
