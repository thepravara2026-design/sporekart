# API Authentication

**Version:** 2.0.0
**Last Updated:** 2026-07-23
**Module:** identity-service

---

## Authentication Methods

The SporeKart API uses **Bearer JWT authentication** for all authenticated endpoints. The platform supports two authentication flows:

1. **Password login** — Direct username/password authentication
2. **OTP login** — One-time password verification for multi-factor scenarios

---

## Bearer Token Flow

### Request Format

All authenticated requests must include an `Authorization` header:

```
Authorization: Bearer <access_token>
```

### Token Extraction

The `JwtAuthenticationFilter` (in `SecurityConfig.java`) extracts and validates the token on every request:

```java
private String extractToken(HttpServletRequest request) {
    var header = request.getHeader(AUTHORIZATION_HEADER);
    if (StringUtils.hasText(header) && header.startsWith(BEARER_PREFIX)) {
        return header.substring(BEARER_PREFIX.length());
    }
    return null;
}
```

### Token Validation

Each request validates:
1. Token is not null or blank
2. Token is not blacklisted
3. HMAC-SHA256 signature is valid
4. Token has not expired (with 30s clock skew tolerance)

---

## Endpoints

### Registration

```
POST /auth/register
Content-Type: application/json

Request:
{
  "email": "user@example.com",
  "password": "securePassword123",
  "firstName": "John",
  "lastName": "Doe",
  "channel": "email",
  "phone": "+1234567890"
}

Response: 201 Created
{
  "id": "uuid",
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "status": "PENDING",
  "emailVerified": false,
  "phoneVerified": false,
  "roles": ["CUSTOMER"],
  "createdAt": "..."
}
```

### Login

```
POST /auth/login
Content-Type: application/json

Request:
{
  "username": "user@example.com",
  "password": "securePassword123"
}

Response: 200 OK
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 900,
  "sessionId": "uuid",
  "userId": "uuid",
  "roles": ["CUSTOMER"],
  "permissions": ["PRODUCT_READ", "ORDER_READ", "ORDER_WRITE"],
  "emailVerified": true,
  "phoneVerified": false,
  "workspace": {
    "workspaceId": "uuid",
    "name": "My Farm"
  }
}
```

### Refresh Token

```
POST /auth/refresh
Content-Type: application/json

Request:
{
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}

Response: 200 OK
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 900,
  "sessionId": null,
  "userId": "uuid",
  "roles": ["CUSTOMER"],
  "permissions": ["PRODUCT_READ", "ORDER_READ", "ORDER_WRITE"],
  "emailVerified": true,
  "phoneVerified": false,
  "workspace": { ... }
}
```

### Logout

```
POST /auth/logout
Authorization: Bearer <access_token>

Response: 204 No Content
```

### OTP Generate

```
POST /auth/otp/generate
Content-Type: application/json

Request:
{
  "userId": "uuid",
  "purpose": "LOGIN"
}

Response: 200 OK
{
  "message": "OTP sent",
  "expiresIn": "5 minutes"
}
```

### OTP Verify

```
POST /auth/otp/verify
Content-Type: application/json

Request:
{
  "userId": "uuid",
  "code": "123456",
  "purpose": "LOGIN"
}

Response: 200 OK
{
  "message": "Login successful",
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
  "sessionId": "uuid"
}

Error: 401 Unauthorized
{
  "error": "Invalid or expired OTP"
}
```

---

## Session Management Endpoints

```
# List my active sessions
GET /sessions/me
Authorization: Bearer <access_token>

Response: 200 OK
[
  {
    "sessionId": "uuid",
    "userId": "uuid",
    "deviceId": "uuid",
    "ipAddress": "192.168.1.1",
    "deviceFingerprint": "hash-ip",
    "workspaceContext": "uuid",
    "issuedAt": "2026-07-23T10:00:00Z",
    "expiresAt": "2026-07-23T11:00:00Z",
    "lastActivityAt": "2026-07-23T10:30:00Z",
    "revoked": false
  }
]

# List sessions for a user (requires appropriate role)
GET /sessions?userId=uuid
Authorization: Bearer <access_token>

# Revoke a session
DELETE /sessions/{sessionId}
Authorization: Bearer <access_token>
Response: 204 No Content

# Revoke all my sessions
DELETE /sessions/me
Authorization: Bearer <access_token>
Response: 204 No Content
```

---

## Device Management Endpoints

```
# List my devices
GET /devices/me
Authorization: Bearer <access_token>

# Register a device
POST /devices/register
Authorization: Bearer <access_token>
Content-Type: application/json

Request:
{
  "deviceName": "My Laptop",
  "deviceType": "DESKTOP",
  "os": "Windows",
  "browser": "Chrome",
  "deviceIdentifier": "fingerprint-hash"
}

# Mark device as trusted
POST /devices/{deviceId}/trust
Authorization: Bearer <access_token>

# Remove device
DELETE /devices/{deviceId}
Authorization: Bearer <access_token>
```

---

## Workspace Endpoints

```
# Create workspace
POST /workspaces
Authorization: Bearer <access_token>
Content-Type: application/json

Request:
{
  "name": "My Farm"
}

Response: 201 Created

# List my workspaces
GET /workspaces/me
Authorization: Bearer <access_token>

# Get workspace by ID
GET /workspaces/{workspaceId}
Authorization: Bearer <access_token>
```

---

## Admin Endpoints

```
# List all system permissions
GET /admin/permissions
Authorization: Bearer <access_token>
Required role: ADMIN or SUPER_ADMIN

# List audit events (optionally filter by type)
GET /admin/audit/events?eventType=LOGIN_SUCCESS
Authorization: Bearer <access_token>
Required role: ADMIN or SUPER_ADMIN

# Get audit statistics
GET /admin/audit/stats
Authorization: Bearer <access_token>
Required role: ADMIN or SUPER_ADMIN

Response:
{
  "totalLogins": 1234,
  "totalLogouts": 567,
  "totalRefresh": 890,
  "totalOtpLogins": 45
}
```

---

## Error Codes

| HTTP Status | Error Description | When |
|-------------|------------------|------|
| `400 Bad Request` | Validation failure | Invalid request body (missing/incorrect fields) |
| `401 Unauthorized` | Invalid or expired token | Missing/wrong `Authorization` header, expired token |
| `401 Unauthorized` | Invalid credentials | Wrong username or password |
| `401 Unauthorized` | Invalid or expired OTP | Wrong OTP code or OTP expired |
| `403 Forbidden` | Insufficient role | User lacks required role for endpoint |
| `404 Not Found` | Resource not found | User, session, device, workspace not found |
| `409 Conflict` | Duplicate email | Email already registered |
| `409 Conflict` | Workspace name exists | Duplicate workspace name |
| `429 Too Many Requests` | OTP rate limit exceeded | More than 3 OTP requests per minute |
| `500 Internal Server Error` | Server error | Unexpected server failure |

---

## Response Format

### Success
Standard HTTP status codes with JSON body or `204 No Content`.

### Error (Spring Boot Problem Details)
```json
{
  "type": "about:blank",
  "title": "Bad Request",
  "status": 400,
  "detail": "Validation failure message",
  "instance": "/auth/login",
  "timestamp": "2026-07-23T10:00:00Z"
}
```

---

## Security Headers

All API responses include:

| Header | Value |
|--------|-------|
| `X-XSS-Protection` | `1; mode=block` |
| `Content-Security-Policy` | `default-src 'self'` |
| `X-Frame-Options` | `DENY` |
| `Strict-Transport-Security` | `max-age=31536000; includeSubDomains` |

---

## CORS

Default allowed origin: `http://localhost:3000`

Configured via `app.security.cors.allowed-origins` environment variable.

---

## Rate Limiting

| Endpoint | Limit |
|----------|-------|
| `POST /auth/otp/generate` | 3 requests per 60 seconds per user per purpose |
| All authenticated endpoints | Handled by API gateway rate limiting |

---

## Client Integration Example

### Login and Use API (JavaScript)

```javascript
// Login
const loginResponse = await fetch('https://api.sporekart.example/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ username: 'user@example.com', password: '...' })
});
const { accessToken, refreshToken, expiresIn } = await loginResponse.json();

// Use access token for API calls
const profileResponse = await fetch('https://api.sporekart.example/users/me', {
  headers: { 'Authorization': `Bearer ${accessToken}` }
});

// Refresh when token expires
const refreshResponse = await fetch('https://api.sporekart.example/auth/refresh', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ refreshToken })
});
const newTokens = await refreshResponse.json();
```
