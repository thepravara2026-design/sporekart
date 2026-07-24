# API Security

## Input Validation

- **Bean Validation** (`jakarta.validation`) on all request DTOs
- Custom validators for domain-specific constraints (e.g., product price ranges, grower certification IDs)
- Enforced at the service boundary; invalid requests rejected with 400 + sanitized error message

## Request Size Limits

| Layer   | Limit       | Scope                   |
|---------|-------------|-------------------------|
| Gateway | 10 MB       | All upstream routes     |
| Gateway | 100 KB      | AI inference payloads   |
| Nginx   | 10 MB       | Client-facing           |

Payloads exceeding limits receive HTTP 413.

## Rate Limiting (3 Tiers)

| Tier     | Nginx                   | Gateway (Redis)          | AI Service (In-memory)     |
|----------|-------------------------|--------------------------|----------------------------|
| General  | 100 req/s per IP        | 1000 req/min per user    | –                          |
| Auth     | 10 req/s per IP         | 5 req/min per IP         | –                          |
| AI       | –                       | –                        | 50 req/min per API key     |

429 responses include `Retry-After` header.

## CORS Configuration

- **Dynamic origin reflection** – validates against configured origin whitelist
- Restricted methods: `GET, POST, PUT, DELETE, PATCH, OPTIONS`
- Restricted headers: `Authorization, Content-Type, X-Requested-With`
- Credentials: allowed
- Preflight cache: 1800 seconds

## Error Message Sanitization

All error responses are sanitized at the gateway to prevent information leakage:

```json
{
  "status": 400,
  "error": "Bad Request"
  // NO stack traces, NO internal paths, NO DB details
}
```

## Public Path Whitelist

Paths accessible without authentication:

```
GET    /api/products/**
POST   /api/auth/register
POST   /api/auth/login
POST   /api/auth/refresh
GET    /actuator/health
GET    /actuator/info
```

All other paths require valid JWT.

## Security Headers (Per-Endpoint)

Enabled globally via the 3-layer header strategy. No endpoint bypasses security headers.