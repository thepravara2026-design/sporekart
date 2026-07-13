# Governance Security Model

## Overview

The governance module implements a defense-in-depth security strategy covering RBAC, configuration authorization, audit logging, immutable audit trails, input/output validation, and rate limiting hooks. Security is enforced at multiple layers: framework (Spring Security), application (service layer), and infrastructure (filter chain).

## Role-Based Access Control (RBAC)

### Role Hierarchy

The governance module defines 4 built-in roles with hierarchical permission inheritance:

| Role | Hierarchy Level | Description |
|------|----------------|-------------|
| `AI_ADMINISTRATOR` | 100 | Full access to all governance operations |
| `AI_OPERATOR` | 70 | Manage policies, view audit, manage quotas |
| `AI_COMPLIANCE_OFFICER` | 50 | View policies, run compliance checks, view audit |
| `AI_VIEWER` | 30 | Read-only access to governance data |

**Inheritance rules:**
- A role at a higher hierarchy level inherits all permissions of lower-level roles
- `AI_ADMINISTRATOR` inherits from all roles below
- `AI_OPERATOR` inherits from `AI_COMPLIANCE_OFFICER` and `AI_VIEWER`
- `AI_COMPLIANCE_OFFICER` inherits from `AI_VIEWER`
- `AI_VIEWER` has no inheritance

### Permission Model

Permissions follow the format `{RESOURCE}:{ACTION}`:

| Permission | Allowed Roles |
|------------|---------------|
| `POLICY:*` | ADMINISTRATOR |
| `POLICY:CREATE` | ADMINISTRATOR, OPERATOR |
| `POLICY:READ` | ADMINISTRATOR, OPERATOR, COMPLIANCE_OFFICER, VIEWER |
| `POLICY:UPDATE` | ADMINISTRATOR, OPERATOR |
| `POLICY:ACTIVATE` | ADMINISTRATOR, OPERATOR |
| `POLICY:DEACTIVATE` | ADMINISTRATOR, OPERATOR |
| `POLICY:DELETE` | ADMINISTRATOR |
| `CONFIG:*` | ADMINISTRATOR |
| `CONFIG:CREATE` | ADMINISTRATOR, OPERATOR |
| `CONFIG:READ` | ADMINISTRATOR, OPERATOR, COMPLIANCE_OFFICER, VIEWER |
| `CONFIG:UPDATE` | ADMINISTRATOR, OPERATOR |
| `CONFIG:DELETE` | ADMINISTRATOR |
| `AUDIT:*` | ADMINISTRATOR |
| `AUDIT:READ` | ADMINISTRATOR, OPERATOR, COMPLIANCE_OFFICER |
| `AUDIT:EXPORT` | ADMINISTRATOR, OPERATOR |
| `QUOTA:*` | ADMINISTRATOR |
| `QUOTA:READ` | ADMINISTRATOR, OPERATOR, VIEWER |
| `QUOTA:RESET` | ADMINISTRATOR, OPERATOR |
| `ROLE:*` | ADMINISTRATOR |
| `ROLE:CREATE` | ADMINISTRATOR |
| `ROLE:READ` | ADMINISTRATOR, OPERATOR |
| `ROLE:ASSIGN` | ADMINISTRATOR |
| `COMPLIANCE:*` | ADMINISTRATOR |
| `COMPLIANCE:READ` | ADMINISTRATOR, OPERATOR, COMPLIANCE_OFFICER, VIEWER |
| `COMPLIANCE:RUN` | ADMINISTRATOR, COMPLIANCE_OFFICER |

### Permission Resolution Algorithm

```
1. Resolve user's assigned roles
2. Expand roles to include inherited permissions (hierarchy-based)
3. Collect all ALLOW and DENY permissions from all assignments
4. Apply DENY overrides — if any DENY matches, deny access
5. If no ALLOW matches, deny access
6. Grant access
```

### RBAC Enforcement Points

| Layer | Enforcement | Mechanism |
|-------|-------------|-----------|
| HTTP | Endpoint authorization | `@PreAuthorize` on controller methods |
| Service | Method-level security | `@Secured` on application service methods |
| Repository | Data filtering | `@PostFilter` for list results |
| UI | Feature visibility | Permission check in response DTOs |

## Configuration Authorization

### Access Control for Configuration Operations

Configuration entries have scope-based access control:

| ConfigScope | Who Can Modify | Who Can Read |
|-------------|----------------|--------------|
| `GLOBAL` | AI_ADMINISTRATOR only | All authenticated users |
| `MODULE` | AI_ADMINISTRATOR, AI_OPERATOR | All authenticated users |
| `PROVIDER` | AI_ADMINISTRATOR, AI_OPERATOR | All authenticated users |
| `MODEL` | AI_ADMINISTRATOR, AI_OPERATOR | All authenticated users |
| `TENANT` | AI_ADMINISTRATOR, tenant admin | Tenant-scoped users |
| `USER` | User themselves, AI_ADMINISTRATOR | User and administrators |

### Configuration Encryption

Sensitive configuration values (API keys, secrets, credentials) are stored encrypted:
- Entries with `encrypted = true` have their `value` field encrypted at the application layer using AES-256-GCM before persistence
- Decryption occurs at read time only for authorized users
- Encryption keys are referenced by the `GovernanceConfig` properties and loaded from the environment/vault
- Encrypted values are never returned in full in API responses (return `*****` masked)

### Configuration Change Authorization Workflow

```
1. User submits configuration change
2. System verifies user has CONFIG:{ACTION} permission
3. System verifies user has scope-level access to the target scope
4. System validates configuration value against schema (data type, constraints)
5. System logs the change in change log
6. System publishes ConfigUpdated event
7. System invalidates the relevant cache entries
```

## Audit Logging

### Audit Event Categories

All governance operations are audited. The audit system captures:

| Category | Events Recorded | Detail Level |
|----------|----------------|--------------|
| Policy Lifecycle | CREATE, UPDATE, ACTIVATE, DEACTIVATE, ARCHIVE | Full before/after diff |
| Configuration | CREATE, UPDATE, DELETE, EXPORT, IMPORT | Full value (masked if encrypted) |
| Access Control | PERMISSION_GRANTED, PERMISSION_REVOKED, ROLE_ASSIGNED, ROLE_REVOKED | Role/permission details |
| Security Events | ACCESS_DENIED, RATE_LIMIT_EXCEEDED, QUOTA_EXCEEDED | Request context |
| Compliance | COMPLIANCE_VIOLATION | Violation details |

### Audit Record Structure

Every audit record captures:
- `eventType` — Standardized event type from `AuditEventType` enum
- `actorId` — Authenticated user or service principal
- `actorRole` — Role at time of action (recorded at time of event, not resolved at query)
- `resource` — Affected resource in URI format (e.g., `governance:policy:{uuid}`)
- `action` — Action performed
- `outcome` — SUCCESS, FAILURE, or DENIED
- `details` — JSON payload with context-specific data
- `timestamp` — Immutable event timestamp (set at creation)
- `correlationId` — Links to request tracing across the platform
- `sourceIp` — Originating IP address
- `userAgent` — Client user agent

## Immutable Audit Trail

### Append-Only Enforcement

The `governance_audit_records` table is enforced as append-only at the database level:

1. **Application layer:** No UPDATE or DELETE methods are exposed in the `AuditRecordRepository`
2. **Database trigger:** A `BEFORE UPDATE OR DELETE` trigger on `governance_audit_records` raises an exception preventing modification
3. **Flyway migration:** The trigger is created by the V20 migration and cannot be bypassed without direct database access
4. **ORM constraint:** JPA entity has no setter methods for immutable fields

### Audit Data Integrity

- All audit records are signed with an HMAC hash stored in a companion verification table (optional, future enhancement)
- Audit export includes a hash manifest for tamper detection
- Database backup includes audit tables with WAL-level integrity

### Retention Policy

| Audit Age | Storage Tier | Accessibility |
|-----------|--------------|---------------|
| 0-90 days | Primary table (hot) | Full query via API |
| 91-365 days | Archived table (warm) | Export only |
| 1+ years | Object storage (cold) | Manual restore |

## Input Validation

### Request Validation Rules

| Field Type | Validation Rule |
|------------|-----------------|
| Policy name | 1-255 chars, alphanumeric + spaces/hyphens/underscores |
| Policy rules | Valid JSON, max 10KB |
| Config key | 1-255 chars, lowercase alphanumeric with dots |
| Config value | Max 64KB, type-checked against `data_type` |
| Audit query parameters | Min/max date range enforced (max 90 days) |
| Import payload | Max 5MB, valid JSON or YAML |
| Role name | 1-100 chars, uppercase alphanumeric with underscores |
| Permission expression | Must match `{RESOURCE}:{ACTION}` format |

### Injection Prevention

| Vector | Protection |
|--------|------------|
| SQL Injection | Parameterized queries via JPA/Hibernate |
| NoSQL Injection | N/A (relational DB only) |
| Command Injection | No shell execution in governance code |
| LDAP Injection | No LDAP queries |
| XSS | JSON response encoding via Spring Boot default |
| Path Traversal | Resource values validated against pattern |

### JSON Schema Validation

Policy rules and config values are validated against JSON schemas on write:

```json
{
  "RATE_LIMIT": {
    "type": "object",
    "properties": {
      "maxRequests": { "type": "integer", "minimum": 1 },
      "windowSeconds": { "type": "integer", "minimum": 1, "maximum": 86400 },
      "burstSize": { "type": "integer", "minimum": 1 }
    },
    "required": ["maxRequests", "windowSeconds"]
  },
  "CONTENT_FILTER": {
    "type": "object",
    "properties": {
      "blockedPatterns": { "type": "array", "items": { "type": "string" } },
      "blockedCategories": { "type": "array", "items": { "type": "string" } },
      "action": { "type": "string", "enum": ["BLOCK", "FLAG", "LOG"] }
    },
    "required": ["action"]
  }
}
```

## Output Validation

### Response Sanitization

- Encrypted config values are masked (`*****`) in API responses
- Audit detail fields are truncated at 10KB in list responses
- Pagination is enforced (max page size 200 for audit, 100 for others)
- Sensitive fields (passwords, tokens) are never returned

### Error Response Format

All errors follow RFC 9457 Problem Details:

```json
{
  "type": "about:blank",
  "title": "Forbidden",
  "status": 403,
  "detail": "Insufficient permissions to perform this operation",
  "instance": "/api/v1/governance/policies",
  "correlationId": "req-abc-123"
}
```

## Rate Limiting Hooks

### Two-Tier Rate Limiting

The governance module implements a second tier of rate limiting that operates after the gateway-level rate limiter:

| Tier | Location | Purpose | Granularity |
|------|----------|---------|-------------|
| 1 | API Gateway | Global request throttling | Per-user, per-endpoint |
| 2 | Governance Module | Policy-based rate enforcement | Per-module, per-operation, configurable |

### Governance Rate Limit Policies

Rate limits are defined as `GovernancePolicy` records with type `RATE_LIMIT`. They support:

| Parameter | Description | Default |
|-----------|-------------|---------|
| `maxRequests` | Maximum requests in window | 1000 |
| `windowSeconds` | Time window in seconds | 60 |
| `burstSize` | Allowed burst above limit | 1200 |
| `enforcement` | `HARD` (block) or `SOFT` (warn) | `HARD` |

### Rate Limit Enforcement Flow

```
1. Request enters governance module
2. GovernanceSecurityManager intercepts request
3. Resolve applicable RATE_LIMIT policies for (module, user, operation)
4. Check current request count against limit
5. If exceeded:
   a. Record AuditRecord with RATE_LIMIT_EXCEEDED
   b. If HARD enforcement: return 429 Too Many Requests
   c. If SOFT enforcement: add warning header, allow request
6. If within limit: increment counter, allow request
7. Publish usage update to Kafka
```

### Rate Limit Response (429 Too Many Requests)

```json
{
  "type": "about:blank",
  "title": "Too Many Requests",
  "status": 429,
  "detail": "Rate limit exceeded for conversation/AI_CHAT. Limit: 100 requests per 60 seconds",
  "instance": "/api/v1/conversation/sessions",
  "correlationId": "req-def-456",
  "retryAfter": 45
}
```

## Security Configuration

### Spring Security Integration

```java
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class GovernanceSecurityConfig {

    @Bean
    public SecurityFilterChain governanceFilterChain(HttpSecurity http) {
        http.securityMatcher("/api/v1/governance/**")
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(GET, "/api/v1/governance/policies").hasAuthority("POLICY:READ")
                .requestMatchers(POST, "/api/v1/governance/policies").hasAuthority("POLICY:CREATE")
                .requestMatchers(PUT, "/api/v1/governance/policies/**").hasAuthority("POLICY:UPDATE")
                .requestMatchers(DELETE, "/api/v1/governance/policies/**").hasAuthority("POLICY:DELETE")
                .requestMatchers(GET, "/api/v1/governance/audit").hasAuthority("AUDIT:READ")
                .requestMatchers(POST, "/api/v1/governance/audit/export").hasAuthority("AUDIT:EXPORT")
                .requestMatchers(GET, "/api/v1/governance/config").hasAuthority("CONFIG:READ")
                .requestMatchers(POST, "/api/v1/governance/config").hasAuthority("CONFIG:CREATE")
                .requestMatchers(GET, "/api/v1/governance/compliance").hasAuthority("COMPLIANCE:READ")
                .requestMatchers(GET, "/api/v1/governance/quotas").hasAuthority("QUOTA:READ")
                .requestMatchers(POST, "/api/v1/governance/quotas/reset").hasAuthority("QUOTA:RESET")
                .requestMatchers(GET, "/api/v1/governance/roles").hasAuthority("ROLE:READ")
                .requestMatchers(POST, "/api/v1/governance/roles").hasAuthority("ROLE:CREATE")
                .requestMatchers(POST, "/api/v1/governance/roles/**").hasAuthority("ROLE:ASSIGN")
                .anyRequest().authenticated()
            );
        return http.build();
    }
}
```

### CORS Configuration

```yaml
governance:
  cors:
    allowed-origins: ${GOV_CORS_ORIGINS:http://localhost:3000}
    allowed-methods: GET, POST, PUT, DELETE
    max-age: 3600
```

### TLS/mTLS

All governance API endpoints require TLS 1.3. mTLS is optional and configurable for machine-to-machine governance operations.
