package com.sporekart.identity.domain.model;

import java.time.Instant;

public class Session {
    private final String sessionId;
    private final String userId;
    private String deviceId;
    private final String ipAddress;
    private final String deviceFingerprint;
    private String workspaceContext;
    private final Instant issuedAt;
    private final Instant expiresAt;
    private Instant lastActivityAt;
    private boolean revoked;

    public Session(String sessionId, String userId, String deviceId, String ipAddress,
                   String deviceFingerprint, String workspaceContext,
                   Instant issuedAt, Instant expiresAt) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.deviceId = deviceId;
        this.ipAddress = ipAddress;
        this.deviceFingerprint = deviceFingerprint;
        this.workspaceContext = workspaceContext;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.lastActivityAt = issuedAt;
        this.revoked = false;
    }

    public String getSessionId() { return sessionId; }
    public String getUserId() { return userId; }
    public String getDeviceId() { return deviceId; }
    public String getIpAddress() { return ipAddress; }
    public String getDeviceFingerprint() { return deviceFingerprint; }
    public String getWorkspaceContext() { return workspaceContext; }
    public Instant getIssuedAt() { return issuedAt; }
    public Instant getExpiresAt() { return expiresAt; }
    public Instant getLastActivityAt() { return lastActivityAt; }
    public boolean isRevoked() { return revoked; }
    public boolean isExpired() { return Instant.now().isAfter(expiresAt); }

    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public void setWorkspaceContext(String workspaceContext) { this.workspaceContext = workspaceContext; }
    public void touch() { this.lastActivityAt = Instant.now(); }
    public void revoke() { this.revoked = true; }
}
