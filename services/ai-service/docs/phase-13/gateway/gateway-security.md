# AI Gateway Security

## Security Architecture

```mermaid
graph TB
    REQ[Request] --> AM[Authentication Methods]
    AM --> AK[API Key]
    AM --> BT[Bearer Token]
    AM --> TV[Tenant Validation]
    AM --> IP[IP Whitelist]
    AK --> AUTH{Authenticated?}
    BT --> AUTH
    TV --> AUTH
    IP --> AUTH
    AUTH -->|yes| AZ[Authorization]
    AZ --> RBAC[Role-Based Access]
    AZ --> TV2[Tenant Isolation]
    RBAC --> PASS[Access Granted]
    TV2 --> PASS
    AUTH -->|no| DENY[Access Denied]
    PASS --> AUDIT[Audit Log]
    PASS --> DS[Data Masking]
    PASS --> IS[Input Sanitization]
```

## Security Hooks

| Hook | Order | Purpose |
|------|-------|---------|
| ApiKeyAuthHook | 1 | API key-based authentication |
| BearerTokenHook | 2 | JWT/bearer token authentication |
| TenantValidationHook | 3 | Multi-tenant isolation |
| RoleBasedAccessHook | 4 | RBAC enforcement |
| IpWhitelistHook | 5 | IP-based access control |
| RateLimitSecurityHook | 6 | Security rate limiting |
| AuditLogHook | 7 | Security audit logging |
| DataMaskingHook | 8 | PII data masking |
| InputSanitizationHook | 9 | Input validation & sanitization |

## Security Hook Interface

```mermaid
classDiagram
    class SecurityHook {
        <<interface>>
        +boolean onAuthenticate(PipelineContext)
        +boolean onAuthorize(PipelineContext)
        +void onSecurityAudit(PipelineContext)
        +void onSecurityViolation(PipelineContext)
        +String name()
        +int order()
    }
    class SecurityManager {
        <<interface>>
        +AuthenticationResult authenticate(PipelineContext)
        +void authorize(PipelineContext, AuthenticationResult)
        +void audit(PipelineContext, AuthenticationResult)
        +void registerHook(SecurityHook)
        +boolean isSecurityEnabled()
    }
    SecurityManager --> SecurityHook
```

## Authentication Flow

```mermaid
sequenceDiagram
    participant CTX as Pipeline
    participant SM as SecurityManager
    participant H1 as API Key Hook
    participant H2 as Bearer Token Hook
    participant H3 as Tenant Hook

    CTX->>SM: authenticate(context)
    SM->>H1: onAuthenticate()
    H1-->>SM: false
    SM->>H2: onAuthenticate()
    H2-->>SM: true
    SM->>SM: Build AuthenticationResult
    SM->>H3: onAuthenticate()
    SM-->>CTX: AuthenticationResult
```

## Authorization Flow

```mermaid
sequenceDiagram
    participant SM as SecurityManager
    participant RBAC as Role-Based Access Hook
    participant TV as Tenant Validation Hook
    participant AUDIT as Audit Log

    SM->>RBAC: onAuthorize()
    RBAC-->>SM: true
    SM->>TV: onAuthorize()
    TV-->>SM: true
    SM->>SM: Check tenant isolation
    SM->>AUDIT: onSecurityAudit()
    SM-->>Pipeline: Access granted
```
