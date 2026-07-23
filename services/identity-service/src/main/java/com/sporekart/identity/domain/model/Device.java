package com.sporekart.identity.domain.model;

import java.time.Instant;

public class Device {
    private final String deviceId;
    private final String userId;
    private final String name;
    private final DeviceType type;
    private final String os;
    private final String browser;
    private final String deviceIdentifier;
    private boolean trusted;
    private boolean suspicious;
    private Instant lastUsedAt;
    private final Instant createdAt;

    public Device(String deviceId, String userId, String name, DeviceType type,
                  String os, String browser, String deviceIdentifier) {
        this.deviceId = deviceId;
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.os = os;
        this.browser = browser;
        this.deviceIdentifier = deviceIdentifier;
        this.trusted = false;
        this.suspicious = false;
        this.lastUsedAt = Instant.now();
        this.createdAt = Instant.now();
    }

    public String getDeviceId() { return deviceId; }
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public DeviceType getType() { return type; }
    public String getOs() { return os; }
    public String getBrowser() { return browser; }
    public String getDeviceIdentifier() { return deviceIdentifier; }
    public boolean isTrusted() { return trusted; }
    public boolean isSuspicious() { return suspicious; }
    public Instant getLastUsedAt() { return lastUsedAt; }
    public Instant getCreatedAt() { return createdAt; }

    public void markTrusted() { this.trusted = true; this.suspicious = false; }
    public void markSuspicious() { this.suspicious = true; this.trusted = false; }
    public void updateLastUsed() { this.lastUsedAt = Instant.now(); }

    public enum DeviceType {
        DESKTOP, TABLET, MOBILE, UNKNOWN
    }
}
