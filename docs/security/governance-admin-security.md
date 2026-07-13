# Governance Admin Security

## RBAC Model

### Roles
| Role | Permissions | Description |
|------|-------------|-------------|
| AI_ADMINISTRATOR | Full admin access | All admin operations |
| AI_CONFIG_ADMIN | Configuration management | CRUD configurations, import/export |
| AI_FEATURE_ADMIN | Feature flag management | Toggle feature flags |
| AI_MODULE_ADMIN | Module management | Enable/disable modules |
| AI_AUDITOR | Read-only audit access | View audit logs only |
| AI_OPERATOR | Operational tasks | Maintenance mode, health checks |

### Role Hierarchy
```
AI_ADMINISTRATOR (inherits all)
├── AI_CONFIG_ADMIN
├── AI_FEATURE_ADMIN
├── AI_MODULE_ADMIN
├── AI_AUDITOR
└── AI_OPERATOR
```

### Least Privilege Principle
- Each role has the minimum permissions required for its function
- No role has overlapping responsibilities
- AI_ADMINISTRATOR is the only role with configuration + feature + module management
- AI_AUDITOR is strictly read-only — no mutation operations

## Role Separation

### Configuration Authorization
- **Config Create/Update/Delete:** Requires AI_CONFIG_ADMIN or AI_ADMINISTRATOR
- **Config Read:** Requires AI_CONFIG_ADMIN, AI_ADMINISTRATOR, or AI_AUDITOR
- **Config Import:** Requires AI_CONFIG_ADMIN + AI_AUDITOR
- **Config Export:** Requires AI_CONFIG_ADMIN or AI_AUDITOR

### Feature Flag Authorization
- **Toggle Feature Flag:** Requires AI_FEATURE_ADMIN or AI_ADMINISTRATOR
- **View Feature Flags:** Requires any authenticated role

### Module Authorization
- **Enable/Disable Module:** Requires AI_MODULE_ADMIN or AI_ADMINISTRATOR
- **View Module Status:** Requires any authenticated role

### Audit Authorization
- **View Audit Logs:** Requires AI_AUDITOR or AI_ADMINISTRATOR
- **Export Audit Logs:** Requires AI_AUDITOR
- **Delete Audit Logs:** Not permitted (immutable)

### Maintenance Mode
- **Enable/Disable:** Requires AI_ADMINISTRATOR only
- **View Status:** Requires any authenticated role

## Immutable Audit Trail

### Enforcement
- Admin audit records (`admin_audit` table) are enforced immutable at the database level
- A BEFORE UPDATE/DELETE trigger on `admin_audit` prevents modification
- Application layer NEVER issues UPDATE or DELETE operations on audit records
- Audit records are append-only

### Audit Record Contents
Every auditable operation records:
- Operation type and target
- Previous state and new state
- Timestamp (server clock, not client-provided)
- Performing user identity (from JWT, not client-provided)

### Audit Retention
- Records are never physically deleted
- Archival to cold storage for >90 day retention
- Query availability guaranteed for 90 days

## Configuration Authorization

### Access Control Rules
1. Users can only modify configurations for modules they are authorized to manage
2. Configuration visibility respects module boundaries
3. Environment-scoped configurations are restricted to authorized environments
4. Production environment requires elevated permissions vs. Development

### Sensitive Configuration Handling
- Encrypted value type for sensitive configurations (API keys, secrets)
- Encrypted values are never returned in plain text via API
- Decryption only occurs within the target module at runtime
- Encryption keys managed externally (not stored in database)

## API Security

### Endpoint Protection
- All `/api/v1/admin/**` endpoints require authentication
- Role-based access enforced via Spring Security method-level annotations
- CSRF protection enabled for state-changing operations
- Rate limiting at 20 requests/second per user for admin endpoints

### Audit Logging
- All admin API calls include correlation ID for tracing
- Failed authorization attempts logged to admin audit
- Suspicious activity detection (repeated 403s) triggers alert

## Security Exception Codes

| Code | HTTP Status | Description |
|------|-------------|-------------|
| ADM_401 | 401 | Authentication required |
| ADM_403 | 403 | Insufficient role/permission |
| ADM_403_ENV | 403 | Not authorized for environment |
| ADM_403_MODULE | 403 | Not authorized for module |
