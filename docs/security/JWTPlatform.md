# JWT Platform

**Version:** 2.0.0
**Last Updated:** 2026-07-23
**Module:** identity-service

---

## Algorithm

| Property | Value |
|----------|-------|
| Algorithm | HMAC-SHA256 (HS256) |
| Library | Nimbus JOSE + JWT (`com.nimbusds`) |
| Key requirement | Minimum 32 bytes (256 bits) |
| Signing | `MACSigner` |
| Verification | `MACVerifier` |

---

## Token Types

### Access Token

| Claim | Value | Description |
|-------|-------|-------------|
| `sub` | User UUID | Subject — the authenticated user |
| `iss` | `https://identity.sporekart.example` | Issuer (configurable) |
| `iat` | Unix timestamp (backdated 30s) | Issued at with clock skew tolerance |
| `exp` | `iat + 15 minutes` | Expiration |
| `jti` | UUID v4 | Unique token ID |
| `type` | `"access"` | Token type discriminator |
| `roles` | `["CUSTOMER", "GROWER"]` | User's role list |
| `sessionId` | Session UUID | Active session reference |
| `deviceId` | Device UUID | Active device reference |
| `permissions` | `["PRODUCT_READ", ...]` | Resolved permission list |

### Refresh Token

| Claim | Value | Description |
|-------|-------|-------------|
| `sub` | User UUID | Subject |
| `iss` | `https://identity.sporekart.example` | Issuer |
| `iat` | Unix timestamp | Issued at |
| `exp` | `iat + 7 days (10080 min)` | Expiration |
| `jti` | UUID v4 | Unique token ID |
| `type` | `"refresh"` | Token type discriminator |

---

## Token Lifecycle

```
Registration
    │
    ▼
┌──────────────────┐
│   POST /auth/register
│   (creates user)
└──────────────────┘
    │
    ▼  login
┌──────────────────────────────────────────┐
│   POST /auth/login                       │
│   → Access Token (15min)                │
│   → Refresh Token (7 days)              │
│   → Session created (60min)             │
│   → Device auto-registered              │
│   → Workspace associated                │
└──────────────────────────────────────────┘
    │
    ├──► Use access token for API calls
    │       │
    │       ├──► Access token expires
    │       │
    │       ▼
    │   POST /auth/refresh
    │   → Old refresh token blacklisted
    │   → New access token issued
    │   → New refresh token issued (rotation)
    │
    ├──► POST /auth/logout
    │   → Access token blacklisted
    │   → Session revoked
    │
    └──► Token expiry (7 days)
        → Re-authentication required
```

---

## Token Generation (`JwtService`)

### Access Token

```java
public String generateAccessToken(String subject, List<String> roles, Map<String, Object> extraClaims) {
    var now = Instant.now();
    var skew = properties.getClockSkewSeconds();
    var claims = new JWTClaimsSet.Builder()
            .subject(subject)
            .issuer(properties.getIssuer())
            .issueTime(Date.from(now.minusSeconds(skew)))  // Backdated for clock skew
            .expirationTime(Date.from(now.plusSeconds(properties.getExpirationMinutes() * 60)))
            .jwtID(UUID.randomUUID().toString())
            .claim("type", "access")
            .claim("roles", roles);
    if (extraClaims != null) {
        extraClaims.forEach(claimsBuilder::claim);
    }
    return sign(claims.build());
}
```

### Refresh Token

```java
public String generateRefreshToken(String subject) {
    var now = Instant.now();
    var claims = new JWTClaimsSet.Builder()
            .subject(subject)
            .issuer(properties.getIssuer())
            .issueTime(Date.from(now))
            .expirationTime(Date.from(now.plusSeconds(properties.getRefreshExpirationMinutes() * 60)))
            .jwtID(UUID.randomUUID().toString())
            .claim("type", "refresh")
            .build();
    return sign(claims);
}
```

---

## Token Validation

### Validation Flow (`JwtService.validateAndParse()`)

1. **Null/blank check** — Returns null for empty tokens
2. **Blacklist check** — Rejects blacklisted tokens immediately
3. **Signature verification** — `SignedJWT.verify(verifier)` with HMAC-SHA256
4. **Expiration check** — Applies 30s clock skew tolerance: `exp.getTime() + skewMs < System.currentTimeMillis()`

```java
public SignedJWT validateAndParse(String token) {
    if (token == null || token.isBlank()) return null;
    if (blacklist.contains(token)) {
        log.warn("JWT is blacklisted");
        return null;
    }
    var signedJwt = SignedJWT.parse(token);
    if (!signedJwt.verify(verifier)) {
        log.warn("JWT signature verification failed");
        return null;
    }
    var claims = signedJwt.getJWTClaimsSet();
    var exp = claims.getExpirationTime();
    var skewMs = properties.getClockSkewSeconds() * 1000L;
    if (exp != null && exp.getTime() + skewMs < System.currentTimeMillis()) {
        log.warn("JWT is expired (with clock skew tolerance)");
        return null;
    }
    return signedJwt;
}
```

---

## Token Rotation

On every refresh call:

1. Validate the incoming refresh token
2. **Blacklist** the old refresh token immediately
3. Issue a new access token (fresh 15min TTL)
4. Issue a new refresh token (fresh 7 day TTL)

This ensures that if a refresh token is compromised, it can only be used once before rotation invalidates it.

---

## Blacklisting

### In-Memory Blacklist

```java
private final Set<String> blacklist = ConcurrentHashMap.newKeySet();

public void blacklist(String token) {
    blacklist.add(token);
    if (blacklist.size() > properties.getBlacklistSize()) {
        var iter = blacklist.iterator();
        iter.next();  // Evict oldest entry
        iter.remove();
    }
}
```

### Blacklist Properties

| Property | Default | Description |
|----------|---------|-------------|
| Storage | In-memory `ConcurrentHashMap` | Fast, no external dependency |
| Max size | 10,000 entries | Prevents memory exhaustion |
| Eviction | FIFO | Oldest entry removed when full |
| Scope | Per service instance | Use sticky sessions or Redis for multi-instance |

### When Blacklisting Occurs

| Event | Token Blacklisted |
|-------|------------------|
| Logout | Access token |
| Token refresh | Old refresh token (rotation) |

---

## Clock Skew Handling

- **30 seconds tolerance** configured via `app.security.jwt.clock-skew-seconds`
- `iat` is backdated by 30 seconds to prevent early rejection
- `exp` check includes +30 seconds grace period
- This accommodates normal NTP clock drift between services

---

## Security Considerations

| Concern | Mitigation |
|---------|------------|
| Secret strength | Minimum 32-byte key enforced at startup |
| Token theft | Short access token TTL (15min) + refresh rotation |
| Replay attacks | `jti` unique per token |
| Clock skew | 30-second tolerance on all time checks |
| Blacklist overflow | FIFO eviction at 10,000 entries |
| Algorithm confusion | Explicit HS256, no algorithm negotiation |
| Token type confusion | `type` claim discriminates access vs refresh |
| Logout persistence | In-memory blacklist (volatile on restart) |

---

## Configuration

```yaml
app:
  security:
    jwt:
      secret: ${APP_JWT_SECRET}
      issuer: https://identity.sporekart.example
      expiration-minutes: 15
      refresh-expiration-minutes: 10080
      clock-skew-seconds: 30
      blacklist-size: 10000
```

### Environment Variables

| Variable | Default | Required | Description |
|----------|---------|----------|-------------|
| `APP_JWT_SECRET` | — | Yes | HMAC key (min 32 bytes, base64 recommended) |
| `APP_JWT_ISSUER` | `https://identity.sporekart.example` | No | `iss` claim value |
| `APP_JWT_EXPIRATION_MINUTES` | 15 | No | Access token TTL |
| `APP_JWT_REFRESH_EXPIRATION_MINUTES` | 10080 | No | Refresh token TTL |
| `APP_JWT_CLOCK_SKEW_SECONDS` | 30 | No | Clock drift tolerance |
| `APP_JWT_BLACKLIST_SIZE` | 10000 | No | Max blacklist entries |
