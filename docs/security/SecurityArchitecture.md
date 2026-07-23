# Security Architecture

## Defense in Depth

SporeKart employs a layered security model across infrastructure, gateway, services, and data layers.

```
[Nginx] → [Gateway (Spring Cloud)] → [Microservices] → [Databases]
    |               |                       |                |
    ├─ TLS          ├─ JWT validation       ├─ RBAC          ├─ Encrypted at rest
    ├─ Rate limit   ├─ AuthZ filter         ├─ Input valid.  ├─ No default creds
    ├─ Sec headers  ├─ CORS                 ├─ AI security   └─ Fail-fast on missing env
    └─ CSP          └─ Audit log            └─ PII redaction
```

## 3-Layer Security Header Strategy

| Layer            | Mechanism                | Headers Managed              |
|------------------|--------------------------|------------------------------|
| Nginx            | `security-headers.conf`  | HSTS, X-Content-Type-Options, X-Frame-Options, Referrer-Policy |
| Gateway          | `SecurityHeaderFilter`   | CSP, Permissions-Policy, Cache-Control |
| Spring Security  | `ServerHttpSecurity`     | CSP merge, CSRF disable      |

- HSTS: `max-age=31536000; includeSubDomains; preload`
- CSP: `default-src 'self'`
- CSRF: disabled at all services (API-only behind gateway)

## Authentication & Authorization Flow

1. User submits credentials via `/api/auth/login`
2. Identity-service validates credentials, returns signed JWT (HMAC-SHA256)
3. Client includes JWT in `Authorization: Bearer <token>` header
4. Gateway extracts token via `JwtAuthenticationFilter`
5. Gateway validates signature via `NimbusReactiveJwtDecoder`
6. Gateway authorizes via `AuthorizationFilter` using JWT `roles` claim
7. Downstream services trust gateway-validated tokens (no re-validation)

### Session Strategy

Stateless sessions. No `JSESSIONID`. All state carried in JWT.

## AI Security

Six dedicated services protect AI endpoints:

- **AssistantSecurityService** – 12 prompt-injection patterns + PII redaction (email, phone, SSN, credit card, bank account, PAN, Aadhaar)
- **SemanticSecurityService** – Semantic analysis of prompt intent
- **ConversationSecurityService** – Cross-turn conversation safety
- **PromptValidationService** – Schema and constraint validation
- **ContentSecurityService** – Response content filtering
- **WorkflowSecurityService** – Multi-step workflow guardrails

## Rate Limiting

| Layer   | Implementation | Scope      |
|---------|---------------|------------|
| Nginx   | 3-tier limits (conn/burst/zone) | Per IP |
| Gateway | Redis-backed rate limiter | Per user / per route |
| AI Service | In-memory rate limiter | Per API key |

## Secrets Management

- Zero hardcoded secrets in tracked source files
- All secrets via environment variables with fail-fast on missing values
- Database credentials have no defaults
- `.gitignore` excludes `*.env` files
- Integration target: AWS Secrets Manager