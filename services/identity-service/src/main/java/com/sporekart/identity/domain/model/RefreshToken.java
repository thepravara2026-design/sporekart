package com.sporekart.identity.domain.model;

import java.time.Instant;

public class RefreshToken {
    private final Long id;
    private final String token;
    private final String userId;
    private boolean revoked;
    private Instant revokedAt;
    private String replacedBy;
    private final Instant expiresAt;
    private final Instant createdAt;

    public RefreshToken(Long id, String token, String userId,
                        boolean revoked, Instant revokedAt, String replacedBy,
                        Instant expiresAt, Instant createdAt) {
        this.id = id;
        this.token = token;
        this.userId = userId;
        this.revoked = revoked;
        this.revokedAt = revokedAt;
        this.replacedBy = replacedBy;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getToken() { return token; }
    public String getUserId() { return userId; }
    public boolean isRevoked() { return revoked; }
    public Instant getRevokedAt() { return revokedAt; }
    public String getReplacedBy() { return replacedBy; }
    public Instant getExpiresAt() { return expiresAt; }
    public Instant getCreatedAt() { return createdAt; }
    public boolean isExpired() { return Instant.now().isAfter(expiresAt); }

    public void revoke() { this.revoked = true; this.revokedAt = Instant.now(); }
    public void revokeWithReplacement(String newToken) {
        this.revoked = true;
        this.revokedAt = Instant.now();
        this.replacedBy = newToken;
    }
}
