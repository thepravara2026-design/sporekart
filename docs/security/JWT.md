# JWT Implementation

## Algorithm

HMAC-SHA256 (`HS256`). Symmetric key. Minimum 32-byte secret enforced at startup.

## Token Structure

### Access Token (15-minute expiry)

```json
{
  "sub": "550e8400-e29b-41d4-a716-446655440000",
  "iss": "sporekart-identity",
  "iat": 1696000000,
  "exp": 1696000900,
  "jti": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "roles": ["CUSTOMER"]
}
```

### Refresh Token (7-day expiry)

Same structure with extended `exp` and a distinct `jti`. Issued alongside the access token at login.

## Components

### JwtService (`identity-service`)

- `generateToken(UserDetails)` – creates signed access token
- `generateRefreshToken(UserDetails)` – creates signed refresh token
- `validateToken(String token)` – validates signature, expiry, and `jti` blacklist
- `extractUserId(String token)` – reads `sub` claim

### JwtAuthenticationFilter (`gateway`)

- Extracts `Authorization: Bearer <token>` header
- Delegates to `NimbusReactiveJwtDecoder` for cryptographic validation
- Populates `SecurityContext` with authenticated principal
- Rejects: missing header, expired token, invalid signature, `alg: none`

### NimbusReactiveJwtDecoder (`gateway`)

- Configured with the shared `JWT_SECRET` environment variable
- Rejects tokens with `alg: none`
- Rejects tokens with invalid HMAC signature
- Rejects expired tokens

## Key Management

- Secret loaded from `JWT_SECRET` environment variable (fail-fast if missing)
- Minimum length: 32 bytes (256 bits) – enforced by `JwtService` on startup
- Rotation supported via `SecretsManagerRotationFilter` using a key version prefix in the JWT header

## Token Lifecycle

1. **Login**: Identity-service validates credentials → returns access + refresh tokens
2. **API Call**: Gateway validates access token → routes to service
3. **Refresh**: Client sends refresh token to `/api/auth/refresh` → new access token issued
4. **Revocation**: Refresh token `jti` added to Redis blacklist on logout/rotation