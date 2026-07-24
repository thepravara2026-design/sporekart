package com.sporekart.identity.domain.model;

import java.time.Instant;

public class OtpCode {
    private final Long id;
    private final String userId;
    private final String code;
    private final OtpPurpose purpose;
    private final Instant expiresAt;
    private int attempts;
    private final int maxAttempts;
    private boolean verified;
    private final Instant createdAt;

    public OtpCode(Long id, String userId, String code, OtpPurpose purpose,
                   Instant expiresAt, int maxAttempts, int attempts,
                   boolean verified, Instant createdAt) {
        this.id = id;
        this.userId = userId;
        this.code = code;
        this.purpose = purpose;
        this.expiresAt = expiresAt;
        this.maxAttempts = maxAttempts;
        this.attempts = attempts;
        this.verified = verified;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getUserId() { return userId; }
    public String getCode() { return code; }
    public OtpPurpose getPurpose() { return purpose; }
    public Instant getExpiresAt() { return expiresAt; }
    public int getAttempts() { return attempts; }
    public int getMaxAttempts() { return maxAttempts; }
    public boolean isVerified() { return verified; }
    public boolean isExpired() { return Instant.now().isAfter(expiresAt); }
    public boolean isMaxAttemptsReached() { return attempts >= maxAttempts; }
    public Instant getCreatedAt() { return createdAt; }

    public void incrementAttempts() { this.attempts++; }
    public void markVerified() { this.verified = true; }

    public enum OtpPurpose {
        LOGIN, EMAIL_VERIFICATION, PHONE_VERIFICATION, PASSWORD_RESET
    }
}
