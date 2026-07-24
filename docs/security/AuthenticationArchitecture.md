# Authentication Architecture

**Version:** 2.0.0
**Last Updated:** 2026-07-23
**Module:** identity-service

---

## Overview

The SporeKart authentication platform implements a comprehensive authentication system using JWT tokens with session management, device tracking, OTP flows, refresh token rotation, workspace isolation, and a full audit trail. The architecture follows hexagonal (ports/adapters) principles with Spring Boot 3.3.3, JPA/Hibernate, and Flyway migrations.

---

## JWT Lifecycle

### Token Types

| Token | Algorithm | Expiry | Storage | Rotation |
|-------|-----------|--------|---------|----------|
| Access | HMAC-SHA256 (HS256) | 15 minutes | In-memory (client) | Issued per login/refresh |
| Refresh | HMAC-SHA256 (HS256) | 7 days (10080 min) | `refresh_tokens` table | Rotation on every use |

### Claims Structure

**Access Token:**
```json
{
  "sub": "user-uuid",
  "iss": "https://identity.sporekart.example",
  "iat": 1700000000,
  "exp": 1700000900,
  "jti": "unique-token-id",
  "type": "access",
  "roles": ["CUSTOMER", "GROWER"],
  "sessionId": "session-uuid",
  "deviceId": "device-uuid",
  "permissions": ["PRODUCT_READ", "ORDER_READ"]
}
```

**Refresh Token:**
```json
{
  "sub": "user-uuid",
  "iss": "https://identity.sporekart.example",
  "iat": 1700000000,
  "exp": 1700086400,
  "jti": "unique-token-id",
  "type": "refresh"
}
```

### Clock Skew Tolerance

A 30-second clock skew window is applied:
- `iat` (issued at) is backdated by 30s to avoid rejection from clock drift
- `exp` (expiration) check includes +30s tolerance before rejecting

### Blacklisting

- Access tokens are blacklisted on logout via a `ConcurrentHashMap` in `JwtService`
- Blacklist capacity defaults to 10,000 entries (configurable via `app.security.jwt.blacklist-size`)
- When capacity is exceeded, the oldest entry is evicted
- Blacklisted tokens fail validation at `JwtService.validateAndParse()`

### Token Signing

```java
// JwtService.java — Nimbus JOSE + JWT with HMAC-SHA256
var keyBytes = properties.getSecret().getBytes();
if (keyBytes.length < 32) {
    throw new IllegalArgumentException("JWT secret must be at least 32 bytes for HMAC-SHA256");
}
this.signer = new MACSigner(keyBytes);
this.verifier = new MACVerifier(keyBytes);
```

---

## Session Management

### Session Creation

Sessions are created on every successful login (`IdentityService.loginWithSession()`):

```java
var session = sessionService.createSession(
    user.getId(), deviceId, ipAddress, deviceFingerprint,
    workspace.map(Workspace::getWorkspaceId).orElse(null));
```

### Session Properties

| Property | Value |
|----------|-------|
| Duration | 60 minutes (`SESSION_DURATION_MINUTES = 60`) |
| Max concurrent | 10 per user (`MAX_CONCURRENT_SESSIONS = 10`) |
| Primary key | UUID (36 chars) |
| Revocable | Yes — `revoke()` sets `revoked = true` |
| Expiry check | `Instant.now().isAfter(expiresAt)` |
| Activity tracking | `touch()` updates `lastActivityAt` |

### Concurrent Session Limit

When a user exceeds 10 active sessions, the oldest session (by `lastActivityAt`) is automatically revoked:

```java
var activeCount = sessionRepository.countActiveByUserId(userId);
if (activeCount >= MAX_CONCURRENT_SESSIONS) {
    var oldest = sessionRepository.findActiveByUserId(userId).stream()
            .min((a, b) -> a.getLastActivityAt().compareTo(b.getLastActivityAt()));
    oldest.ifPresent(s -> sessionRepository.revokeById(s.getSessionId()));
}
```

### Session CRUD

| Operation | Endpoint | Service Method |
|-----------|----------|----------------|
| List my active sessions | `GET /sessions/me` | `SessionService.getActiveSessions()` |
| List all user sessions | `GET /sessions?userId=` | `SessionService.getUserSessions()` |
| Revoke session | `DELETE /sessions/{sessionId}` | `SessionService.revokeSession()` |
| Revoke all my sessions | `DELETE /sessions/me` | `SessionService.revokeAllSessions()` |
| Cleanup expired | Scheduled | `SessionService.cleanupExpired()` |

### Database Schema

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
```

---

## Device Tracking

### Device Registration

Devices are auto-registered on login (`IdentityService.registerDeviceIfNeeded()`):

1. A device fingerprint is computed from `userAgent.hashCode() + "-" + ipAddress`
2. If a device with the same fingerprint exists, `lastUsedAt` is updated
3. If not, a new `Device` is created with `DeviceType.UNKNOWN`
4. OS and browser are extracted from the User-Agent header

### Device Properties

| Property | Description |
|----------|-------------|
| deviceId | UUID primary key |
| userId | Owner |
| name | Device name (default "Unknown Device") |
| type | DESKTOP, TABLET, MOBILE, UNKNOWN |
| os | Extracted from User-Agent |
| browser | Extracted from User-Agent |
| deviceIdentifier | Fingerprint hash |
| trusted | Manually mark trusted devices |
| suspicious | Manually mark suspicious devices |

### Device API

| Operation | Endpoint | Service Method |
|-----------|----------|----------------|
| List my devices | `GET /devices/me` | `DeviceService.getUserDevices()` |
| Register device | `POST /devices/register` | `DeviceService.registerDevice()` |
| Mark trusted | `POST /devices/{id}/trust` | `DeviceService.markTrusted()` |
| Remove device | `DELETE /devices/{id}` | `DeviceService.removeDevice()` |

---

## OTP Flow

### OTP Generation

6-digit cryptographically-random OTP codes generated via `SecureRandom`:

```java
private static final SecureRandom RANDOM = new SecureRandom();
var code = String.format("%06d", RANDOM.nextInt(1000000));
```

### OTP Properties

| Property | Value |
|----------|-------|
| Code length | 6 digits |
| Expiry | 5 minutes |
| Max attempts | 5 |
| Rate limit | 3 requests per 60-second window per user per purpose |
| Purposes | LOGIN, EMAIL_VERIFICATION, PHONE_VERIFICATION, PASSWORD_RESET |

### Rate Limiting

OTP generation is rate-limited using a time-window counter:

```java
var since = Instant.now().minusSeconds(RATE_LIMIT_WINDOW_SECONDS);
var recentCount = otpRepository.countRecentByUserIdAndPurpose(userId, purpose, since);
if (recentCount >= MAX_REQUESTS_PER_WINDOW) {
    throw new BusinessException("Too many OTP requests. Please try again later.");
}
```

### OTP Verification

OTP verification checks (in order):
1. OTP exists for user + purpose
2. OTP is not already verified
3. OTP is not expired
4. Max attempts (5) not reached
5. Code matches

On success: marks verified, returns user session with `authMethod: "otp"` claim.

### OTP API

| Operation | Endpoint | Service Method |
|-----------|----------|----------------|
| Generate OTP | `POST /auth/otp/generate` | `OtpService.generateOtp()` |
| Verify OTP | `POST /auth/otp/verify` | `OtpService.verifyOtp()` |

---

## Refresh Token Rotation

### Rotation Flow

1. Client sends refresh token to `POST /auth/refresh`
2. `JwtService.isRefreshToken()` validates the token type is "refresh"
3. `JwtService.extractSubject()` validates signature and expiry
4. The old refresh token is **blacklisted** immediately
5. A new access token and new refresh token are issued
6. `IdentityService.refreshToken()` returns fresh credentials

### Rotation Implementation

```java
@Transactional
public LoginResponse refreshToken(String refreshTokenValue, String ipAddress, String userAgent) {
    if (!jwtService.isRefreshToken(refreshTokenValue)) {
        throw new BusinessException("Invalid refresh token");
    }
    var subject = jwtService.extractSubject(refreshTokenValue);
    if (subject == null) {
        throw new BusinessException("Invalid or expired refresh token");
    }
    jwtService.blacklist(refreshTokenValue); // Rotation: blacklist old token
    
    var user = userRepositoryPort.findById(subject)
            .orElseThrow(() -> new BusinessException("User not found"));
    
    var newAccessToken = jwtService.generateAccessToken(...);
    var newRefreshToken = jwtService.generateRefreshToken(user.getId());
    // ...
}
```

---

## Workspace Isolation

### Workspace Model

| Property | Description |
|----------|-------------|
| workspaceId | UUID primary key |
| name | Unique workspace name |
| ownerId | User who created the workspace |
| createdAt | Creation timestamp |

### Workspace Association

- On login, the user's first workspace is associated with the session via `workspaceContext`
- Workspace is returned in the `LoginResponse` as a metadata map:
  ```json
  {
    "workspaceId": "uuid",
    "name": "My Farm"
  }
  ```
- Sessions carry a `workspaceContext` field for workspace-scoped operations

### Workspace API

| Operation | Endpoint |
|-----------|----------|
| Create workspace | `POST /workspaces` |
| List my workspaces | `GET /workspaces/me` |
| Get workspace | `GET /workspaces/{workspaceId}` |

---

## Audit Trail

### Audited Auth Events

| Event | Trigger | Details |
|-------|---------|---------|
| `LOGIN_SUCCESS` | `POST /auth/login` | "Login from {ip}" |
| `LOGIN_FAILED` | Invalid credentials | "Invalid password from {ip}" |
| `LOGOUT` | `POST /auth/logout` | "User logout" |
| `TOKEN_REFRESH` | `POST /auth/refresh` | "Token refreshed" |
| `OTP_LOGIN_SUCCESS` | `POST /auth/otp/verify` | "OTP login" |

### Audit Event Fields

| Field | Type | Description |
|-------|------|-------------|
| id | Long | Auto-increment PK |
| event_type | VARCHAR(100) | Event type name |
| actor_id | VARCHAR(36) | User UUID who performed action |
| details | TEXT | Human-readable event details |
| ip_address | VARCHAR(45) | Client IP address |
| created_at | TIMESTAMPTZ | Event timestamp |

### Admin Audit API

| Operation | Endpoint |
|-----------|----------|
| List audit events | `GET /admin/audit/events?eventType=` |
| Audit statistics | `GET /admin/audit/stats` |
| List all permissions | `GET /admin/permissions` |

---

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                      API Gateway                                │
│  Security headers, CORS, rate limiting, route authorization     │
└──────────────────────────┬──────────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────────┐
│                    AuthController                               │
│  /auth/register, /auth/login, /auth/logout, /auth/refresh      │
│  /auth/otp/generate, /auth/otp/verify                           │
└──────────────────────────┬──────────────────────────────────────┘
                           │
┌──────────────────────────▼──────────────────────────────────────┐
│                    IdentityService (Application)                │
│  Orchestrates: JwtService + SessionService + DeviceService     │
│               OtpService + PermissionService + AuditService    │
│               WorkspaceService                                  │
└──────────────────────────┬──────────────────────────────────────┘
                           │
              ┌────────────┼────────────────┬────────────────┐
              ▼            ▼                ▼                ▼
┌─────────────────────┐ ┌──────────┐ ┌────────────┐ ┌──────────────┐
│   Jpa*Repository    │ │   JPA    │ │   Redis    │ │    Kafka     │
│   Adapters          │ │ Entities │ │ Caching    │ │ Async Events │
│ (ports/repos)       │ │  (JPA)   │ │            │ │              │
└──────────┬──────────┘ └──────────┘ └────────────┘ └──────────────┘
           │
┌──────────▼──────────────────────────────────────────────────────┐
│                      PostgreSQL                                 │
│  users, sessions, devices, workspaces, workspace_members        │
│  roles, permissions, user_roles, role_permissions               │
│  refresh_tokens, otp_requests, audit_events                     │
└─────────────────────────────────────────────────────────────────┘
```

---

## Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `APP_JWT_SECRET` | (required) | HMAC-SHA256 key (min 32 bytes) |
| `APP_JWT_ISSUER` | `https://identity.sporekart.example` | JWT issuer claim |
| `APP_JWT_EXPIRATION_MINUTES` | 15 | Access token TTL |
| `APP_JWT_REFRESH_EXPIRATION_MINUTES` | 10080 | Refresh token TTL (7 days) |
| `APP_JWT_CLOCK_SKEW_SECONDS` | 30 | Clock skew tolerance |
| `APP_JWT_BLACKLIST_SIZE` | 10000 | Max in-memory blacklist entries |
| `APP_CORS_ALLOWED_ORIGINS` | `http://localhost:3000` | CORS origins |
