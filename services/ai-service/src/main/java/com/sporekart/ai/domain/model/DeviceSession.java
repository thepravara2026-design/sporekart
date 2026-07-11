package com.sporekart.ai.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class DeviceSession {
    private UUID id;
    private UUID deviceId;
    private UUID userId;
    private String token;
    private String refreshToken;
    private OffsetDateTime expiresAt;
    private boolean isActive;

    public DeviceSession(UUID id, UUID deviceId, UUID userId, String token,
            String refreshToken, OffsetDateTime expiresAt) {
        this.id = id;
        this.deviceId = deviceId;
        this.userId = userId;
        this.token = token;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
        this.isActive = true;
    }

    public UUID getId() {
        return id;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public OffsetDateTime getExpiresAt() {
        return expiresAt;
    }

    public boolean isActive() {
        return isActive;
    }
}
