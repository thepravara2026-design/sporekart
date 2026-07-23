# Session Management

**Version:** 2.0.0
**Last Updated:** 2026-07-23
**Module:** identity-service

---

## Overview

The session management system tracks authenticated user sessions with configurable limits, device binding, expiry, revocation, and full audit logging. Sessions are created on login and provide a server-side reference tied to the JWT access token.

---

## Session Model

```java
public class Session {
    private final String sessionId;       // UUID primary key
    private final String userId;          // Owner
    private String deviceId;              // Bound device (nullable)
    private final String ipAddress;       // Client IP at creation
    private final String deviceFingerprint; // Computed fingerprint
    private String workspaceContext;       // Active workspace ID
    private final Instant issuedAt;       // Creation timestamp
    private final Instant expiresAt;      // Expiry timestamp (60min)
    private Instant lastActivityAt;       // Last touch() timestamp
    private boolean revoked;              // Revocation flag
}
```

---

## Session Properties

| Property | Value | Configuration |
|----------|-------|---------------|
| Duration | 60 minutes | `SESSION_DURATION_MINUTES = 60` (hardcoded) |
| Max concurrent | 10 per user | `MAX_CONCURRENT_SESSIONS = 10` (hardcoded) |
| Session ID | UUID v4 | Generated via `UUID.randomUUID()` |
| Revocable | Yes | `revoke()` sets `revoked = true` |
| Expiry check | UTC comparison | `Instant.now().isAfter(expiresAt)` |
| Last activity | Updated on `touch()` | Manual call from service layer |

---

## Session Lifecycle

```
User Login
    │
    ▼
┌─────────────────────────────────────────────┐
│  SessionService.createSession()             │
│  • Check active count (max 10)             │
│  • If exceeded: evict oldest session       │
│  • Generate UUID session ID                │
│  • Set issuedAt = now, expiresAt = now+60min│
│  • Set lastActivityAt = issuedAt           │
│  • revoked = false                         │
└─────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────┐
│  Active Session (60 min TTL)               │
│  • API calls update lastActivityAt (touch) │
│  • Bound to device fingerprint             │
│  • Associated with workspace context       │
└─────────────────────────────────────────────┘
    │
    ├──► Logout → revokeSession()
    │
    ├──► Expiry → isExpired() returns true
    │
    └──► Eviction → concurrent limit exceeded
```

---

## Session CRUD

### Create Session

```java
@Transactional
public Session createSession(String userId, String deviceId, String ipAddress,
                              String deviceFingerprint, String workspaceContext) {
    var activeCount = sessionRepository.countActiveByUserId(userId);
    if (activeCount >= MAX_CONCURRENT_SESSIONS) {
        var oldest = sessionRepository.findActiveByUserId(userId).stream()
                .min((a, b) -> a.getLastActivityAt().compareTo(b.getLastActivityAt()));
        oldest.ifPresent(s -> sessionRepository.revokeById(s.getSessionId()));
    }
    var sessionId = UUID.randomUUID().toString();
    var now = Instant.now();
    var session = new Session(
            sessionId, userId, deviceId, ipAddress, deviceFingerprint,
            workspaceContext, now, now.plusSeconds(SESSION_DURATION_MINUTES * 60));
    return sessionRepository.save(session);
}
```

### Read Sessions

```java
Optional<Session> getSession(String sessionId)      // Get by ID
List<Session> getUserSessions(String userId)         // All sessions
List<Session> getActiveSessions(String userId)       // Non-expired, non-revoked
```

### Update Session

```java
void touchSession(String sessionId)      // Refresh lastActivityAt
```

### Delete / Revoke Sessions

```java
void revokeSession(String sessionId)         // Revoke single session
void revokeAllSessions(String userId)        // Revoke all for user
void cleanupExpired()                        // Delete expired sessions (scheduled)
```

---

## Session API

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `GET` | `/sessions/me` | Bearer token | List current user's active sessions |
| `GET` | `/sessions?userId=` | Bearer token | List all sessions for a user |
| `DELETE` | `/sessions/{sessionId}` | Bearer token | Revoke a specific session |
| `DELETE` | `/sessions/me` | Bearer token | Revoke all sessions for current user |

### SessionController

```java
@GetMapping("/me")
public ResponseEntity<List<Session>> mySessions(Authentication authentication) {
    var userId = authentication.getName();
    return ResponseEntity.ok(sessionService.getActiveSessions(userId));
}

@GetMapping
public ResponseEntity<List<Session>> userSessions(@RequestParam String userId) {
    return ResponseEntity.ok(sessionService.getUserSessions(userId));
}

@DeleteMapping("/{sessionId}")
public ResponseEntity<Void> revokeSession(@PathVariable String sessionId) {
    sessionService.revokeSession(sessionId);
    return ResponseEntity.noContent().build();
}

@DeleteMapping("/me")
public ResponseEntity<Void> revokeAllMySessions(Authentication authentication) {
    sessionService.revokeAllSessions(authentication.getName());
    return ResponseEntity.noContent().build();
}
```

---

## Device Tracking

### Device Fingerprint

On login, a fingerprint is computed from User-Agent and IP:

```java
var deviceFingerprint = userAgent != null ? userAgent.hashCode() + "-" + ipAddress : ipAddress;
```

### Device Registration

Devices are auto-registered on first login and updated on subsequent logins:

```java
private String registerDeviceIfNeeded(String userId, String userAgent, String fingerprint) {
    var existingDevice = deviceService.getUserDevices(userId).stream()
            .filter(d -> d.getDeviceIdentifier().equals(fingerprint))
            .findFirst();
    if (existingDevice.isPresent()) {
        var device = existingDevice.get();
        deviceService.registerDevice(userId, device.getName(), device.getType(),
                device.getOs(), extractBrowser(userAgent), fingerprint);
        return device.getDeviceId();
    }
    var device = deviceService.registerDevice(userId, "Unknown Device", DeviceType.UNKNOWN,
            extractOs(userAgent), extractBrowser(userAgent), fingerprint);
    return device.getDeviceId();
}
```

---

## Concurrent Session Limit

When a user exceeds 10 active sessions:

1. `countActiveByUserId(userId)` checks current active count
2. If >= 10, the session with the oldest `lastActivityAt` is identified
3. That session is revoked (oldest session eviction)
4. The new session is created

This ensures users cannot exceed the concurrent session cap.

---

## Session Expiry

- Sessions expire 60 minutes after creation
- `Session.isExpired()` compares `expiresAt` against `Instant.now()`
- The `cleanupExpired()` method (for scheduled tasks) deletes all expired sessions
- Expired sessions are filtered out of active session queries by the repository

---

## Revocation

- Logout calls `sessionService.revokeSession()` which sets `revoked = true`
- `revokeAllSessions()` revokes all sessions for a given user
- Revoked sessions are excluded from active session queries
- Admin endpoints can revoke any session

---

## Audit Logging

All session-related events are audited:

| Event | Trigger | Audit Details |
|-------|---------|---------------|
| `LOGIN_SUCCESS` | Session created | "Login from {ip}" |
| `LOGOUT` | Session revoked | "User logout" |
| `TOKEN_REFRESH` | Token refresh | "Token refreshed" |
| `OTP_LOGIN_SUCCESS` | OTP session created | "OTP login" |

---

## Database Schema

```sql
CREATE TABLE sessions (
    session_id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    device_id VARCHAR(36),
    ip_address VARCHAR(45),
    device_fingerprint VARCHAR(255),
    workspace_context VARCHAR(36),
    issued_at TIMESTAMP WITH TIME ZONE NOT NULL,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    last_activity_at TIMESTAMP WITH TIME ZONE NOT NULL,
    revoked BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_sessions_user_id ON sessions (user_id);
CREATE INDEX idx_sessions_expires_at ON sessions (expires_at);
CREATE INDEX idx_sessions_revoked ON sessions (revoked);
```
