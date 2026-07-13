# Governance Database Schema

## Overview

Flyway Migration: `V20__sprint18_governance_platform.sql`

Database: PostgreSQL (H2 compatible for tests)

The governance schema consists of 6 tables designed for policy management, configuration storage, immutable audit logging, RBAC, usage tracking, and compliance reporting.

## Tables

### 1. `governance_policies`

Stores governance policy definitions with lifecycle management.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| `id` | UUID | PK, DEFAULT gen_random_uuid() | Unique policy identifier |
| `name` | VARCHAR(255) | NOT NULL, UNIQUE per scope | Policy display name |
| `type` | VARCHAR(50) | NOT NULL | PolicyType enum value |
| `severity` | VARCHAR(20) | NOT NULL, DEFAULT 'WARNING' | PolicySeverity enum value |
| `status` | VARCHAR(20) | NOT NULL, DEFAULT 'DRAFT' | PolicyStatus enum value |
| `rules` | TEXT | NOT NULL | JSON map of policy rules |
| `scope` | VARCHAR(30) | NOT NULL | ConfigScope enum value |
| `module_target` | VARCHAR(100) | NULLABLE | Target module/provider/model |
| `created_at` | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Creation timestamp |
| `updated_at` | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Last update timestamp |
| `version` | INTEGER | NOT NULL, DEFAULT 1 | Optimistic locking version |

**Indexes:**
- `idx_gov_policies_type` ON `type`
- `idx_gov_policies_status` ON `status`
- `idx_gov_policies_scope` ON `scope`
- `idx_gov_policies_name_scope` UNIQUE ON `name, scope`

**Constraints:**
- `ck_gov_policies_severity` CHECK (`severity` IN ('INFO', 'WARNING', 'CRITICAL', 'BLOCKING'))
- `ck_gov_policies_status` CHECK (`status` IN ('DRAFT', 'ACTIVE', 'INACTIVE', 'ARCHIVED'))
- `ck_gov_policies_type` CHECK (`type` IN ('RATE_LIMIT', 'USAGE_QUOTA', 'CONTENT_FILTER', 'ACCESS_CONTROL', 'AUDIT_CONFIG', 'COMPLIANCE_RULE', 'MODEL_GOVERNANCE', 'DATA_RETENTION'))

---

### 2. `governance_config_entries`

Stores key-value configuration entries with metadata, scoping, and encryption support.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| `id` | UUID | PK, DEFAULT gen_random_uuid() | Unique config entry identifier |
| `scope` | VARCHAR(30) | NOT NULL | ConfigScope enum value |
| `module` | VARCHAR(100) | NULLABLE | Module/provider/model name |
| `key` | VARCHAR(255) | NOT NULL | Configuration key |
| `value` | TEXT | NOT NULL | Configuration value |
| `data_type` | VARCHAR(20) | NOT NULL, DEFAULT 'STRING' | Data type hint |
| `description` | TEXT | NULLABLE | Human-readable description |
| `tags` | TEXT | NULLABLE | JSON array of tags |
| `encrypted` | BOOLEAN | NOT NULL, DEFAULT FALSE | Whether value is encrypted |
| `version` | INTEGER | NOT NULL, DEFAULT 1 | Optimistic locking version |
| `created_at` | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Creation timestamp |
| `updated_at` | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Last update timestamp |

**Indexes:**
- `idx_gov_config_scope` ON `scope`
- `idx_gov_config_module` ON `module`
- `idx_gov_config_key` ON `key`
- `idx_gov_config_scope_module_key` UNIQUE ON `scope, module, key`

**Constraints:**
- `ck_gov_config_scope` CHECK (`scope` IN ('GLOBAL', 'MODULE', 'PROVIDER', 'MODEL', 'TENANT', 'USER'))
- `ck_gov_config_type` CHECK (`data_type` IN ('STRING', 'INTEGER', 'BOOLEAN', 'JSON', 'YAML'))

---

### 3. `governance_audit_records`

Immutable append-only audit log for all governance operations.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| `id` | UUID | PK, DEFAULT gen_random_uuid() | Unique audit record identifier |
| `event_type` | VARCHAR(50) | NOT NULL | AuditEventType enum value |
| `actor_id` | VARCHAR(255) | NOT NULL | User or service principal ID |
| `actor_role` | VARCHAR(100) | NULLABLE | Role of the actor at time of event |
| `resource` | VARCHAR(500) | NOT NULL | Affected resource identifier |
| `action` | VARCHAR(100) | NOT NULL | Action performed |
| `outcome` | VARCHAR(20) | NOT NULL, DEFAULT 'SUCCESS' | SUCCESS, FAILURE, or DENIED |
| `details` | TEXT | NULLABLE | JSON details about the event |
| `timestamp` | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Event timestamp (immutable) |
| `correlation_id` | VARCHAR(100) | NULLABLE | Request correlation ID |
| `source_ip` | VARCHAR(45) | NULLABLE | Client IP address |
| `user_agent` | VARCHAR(500) | NULLABLE | Client user agent string |

**Indexes:**
- `idx_gov_audit_event_type` ON `event_type`
- `idx_gov_audit_actor_id` ON `actor_id`
- `idx_gov_audit_resource` ON `resource`
- `idx_gov_audit_timestamp` ON `timestamp`
- `idx_gov_audit_correlation_id` ON `correlation_id`
- `idx_gov_audit_actor_event` ON `actor_id, event_type`

**Constraints:**
- `ck_gov_audit_outcome` CHECK (`outcome` IN ('SUCCESS', 'FAILURE', 'DENIED'))
- Trigger: `trg_gov_audit_readonly` — Prevents UPDATE and DELETE operations on this table. Only INSERT is permitted.

**DDL for read-only trigger:**
```sql
CREATE OR REPLACE FUNCTION fn_gov_audit_readonly()
RETURNS TRIGGER AS $$
BEGIN
    RAISE EXCEPTION 'governance_audit_records is append-only: UPDATE and DELETE are prohibited';
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_gov_audit_readonly
    BEFORE UPDATE OR DELETE ON governance_audit_records
    FOR EACH ROW
    EXECUTE FUNCTION fn_gov_audit_readonly();
```

---

### 4. `governance_permission_assignments`

Maps roles to permissions for RBAC enforcement.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| `id` | UUID | PK, DEFAULT gen_random_uuid() | Unique assignment identifier |
| `role` | VARCHAR(100) | NOT NULL | Role name |
| `permission` | VARCHAR(100) | NOT NULL | Permission identifier |
| `resource` | VARCHAR(500) | NOT NULL, DEFAULT '*' | Resource pattern |
| `scope` | VARCHAR(30) | NOT NULL, DEFAULT 'GLOBAL' | ConfigScope for permission |
| `effect` | VARCHAR(10) | NOT NULL, DEFAULT 'ALLOW' | ALLOW or DENY |

**Indexes:**
- `idx_gov_perm_role` ON `role`
- `idx_gov_perm_permission` ON `permission`
- `idx_gov_perm_resource` ON `resource`
- `idx_gov_perm_role_permission` UNIQUE ON `role, permission, resource, scope`

**Constraints:**
- `ck_gov_perm_scope` CHECK (`scope` IN ('GLOBAL', 'MODULE', 'PROVIDER', 'MODEL', 'TENANT', 'USER'))
- `ck_gov_perm_effect` CHECK (`effect` IN ('ALLOW', 'DENY'))

---

### 5. `governance_usage_quotas`

Tracks resource usage against defined limits per module/user/operation/period.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| `id` | UUID | PK, DEFAULT gen_random_uuid() | Unique quota record identifier |
| `module` | VARCHAR(100) | NOT NULL | Module identifier |
| `user_id` | VARCHAR(255) | NOT NULL | User identifier |
| `operation` | VARCHAR(100) | NOT NULL | Operation type |
| `period_start` | TIMESTAMP | NOT NULL | Period start boundary |
| `period_end` | TIMESTAMP | NOT NULL | Period end boundary |
| `count` | BIGINT | NOT NULL, DEFAULT 0 | Current usage count |
| `limit` | BIGINT | NOT NULL | Maximum allowed usage |
| `created_at` | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Creation timestamp |
| `updated_at` | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Last update timestamp |

**Indexes:**
- `idx_gov_quota_module` ON `module`
- `idx_gov_quota_user` ON `user_id`
- `idx_gov_quota_operation` ON `operation`
- `idx_gov_quota_period` ON `period_start, period_end`
- `idx_gov_quota_lookup` UNIQUE ON `module, user_id, operation, period_start`

**Constraints:**
- `ck_gov_quota_count` CHECK (`count` >= 0)
- `ck_gov_quota_limit` CHECK (`limit` > 0)
- `ck_gov_quota_period` CHECK (`period_end` > `period_start`)

---

### 6. `governance_compliance_reports`

Stores compliance check results and policy violation details.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| `id` | UUID | PK, DEFAULT gen_random_uuid() | Unique report identifier |
| `policy_id` | UUID | NOT NULL, FK → governance_policies(id) | Referenced policy |
| `status` | VARCHAR(20) | NOT NULL | COMPLIANT, NON_COMPLIANT, ERROR |
| `violations` | TEXT | NULLABLE | JSON array of violation details |
| `checked_at` | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Check execution timestamp |
| `module` | VARCHAR(100) | NULLABLE | Module checked |
| `details` | TEXT | NULLABLE | Additional report details |
| `created_at` | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Record creation timestamp |

**Indexes:**
- `idx_gov_report_policy_id` ON `policy_id`
- `idx_gov_report_status` ON `status`
- `idx_gov_report_checked_at` ON `checked_at`
- `idx_gov_report_module` ON `module`

**Constraints:**
- `fk_gov_report_policy` FOREIGN KEY (`policy_id`) REFERENCES `governance_policies`(`id`) ON DELETE CASCADE
- `ck_gov_report_status` CHECK (`status` IN ('COMPLIANT', 'NON_COMPLIANT', 'ERROR'))

## Entity-Relationship Diagram

```
governance_policies (1) ────── (N) governance_compliance_reports
       │
       │ (referenced by policy_id)
       │
governance_config_entries (standalone, no FK dependencies)
governance_audit_records (standalone, append-only)
governance_permission_assignments (standalone, no FK dependencies)
governance_usage_quotas (standalone, no FK dependencies)
```

## Flyway Migration SQL (V20)

```sql
-- V20__sprint18_governance_platform.sql

-- 1. governance_policies
CREATE TABLE governance_policies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    severity VARCHAR(20) NOT NULL DEFAULT 'WARNING',
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    rules TEXT NOT NULL,
    scope VARCHAR(30) NOT NULL,
    module_target VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version INTEGER NOT NULL DEFAULT 1
);

CREATE INDEX idx_gov_policies_type ON governance_policies(type);
CREATE INDEX idx_gov_policies_status ON governance_policies(status);
CREATE INDEX idx_gov_policies_scope ON governance_policies(scope);
CREATE UNIQUE INDEX idx_gov_policies_name_scope ON governance_policies(name, scope);

-- 2. governance_config_entries
CREATE TABLE governance_config_entries (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    scope VARCHAR(30) NOT NULL,
    module VARCHAR(100),
    key VARCHAR(255) NOT NULL,
    value TEXT NOT NULL,
    data_type VARCHAR(20) NOT NULL DEFAULT 'STRING',
    description TEXT,
    tags TEXT,
    encrypted BOOLEAN NOT NULL DEFAULT FALSE,
    version INTEGER NOT NULL DEFAULT 1,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_gov_config_scope ON governance_config_entries(scope);
CREATE INDEX idx_gov_config_module ON governance_config_entries(module);
CREATE INDEX idx_gov_config_key ON governance_config_entries(key);
CREATE UNIQUE INDEX idx_gov_config_scope_module_key ON governance_config_entries(scope, COALESCE(module, ''), key);

-- 3. governance_audit_records
CREATE TABLE governance_audit_records (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    event_type VARCHAR(50) NOT NULL,
    actor_id VARCHAR(255) NOT NULL,
    actor_role VARCHAR(100),
    resource VARCHAR(500) NOT NULL,
    action VARCHAR(100) NOT NULL,
    outcome VARCHAR(20) NOT NULL DEFAULT 'SUCCESS',
    details TEXT,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    correlation_id VARCHAR(100),
    source_ip VARCHAR(45),
    user_agent VARCHAR(500)
);

CREATE INDEX idx_gov_audit_event_type ON governance_audit_records(event_type);
CREATE INDEX idx_gov_audit_actor_id ON governance_audit_records(actor_id);
CREATE INDEX idx_gov_audit_resource ON governance_audit_records(resource);
CREATE INDEX idx_gov_audit_timestamp ON governance_audit_records(timestamp);
CREATE INDEX idx_gov_audit_correlation_id ON governance_audit_records(correlation_id);
CREATE INDEX idx_gov_audit_actor_event ON governance_audit_records(actor_id, event_type);

-- 4. governance_permission_assignments
CREATE TABLE governance_permission_assignments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    role VARCHAR(100) NOT NULL,
    permission VARCHAR(100) NOT NULL,
    resource VARCHAR(500) NOT NULL DEFAULT '*',
    scope VARCHAR(30) NOT NULL DEFAULT 'GLOBAL',
    effect VARCHAR(10) NOT NULL DEFAULT 'ALLOW'
);

CREATE INDEX idx_gov_perm_role ON governance_permission_assignments(role);
CREATE INDEX idx_gov_perm_permission ON governance_permission_assignments(permission);
CREATE INDEX idx_gov_perm_resource ON governance_permission_assignments(resource);
CREATE UNIQUE INDEX idx_gov_perm_role_permission ON governance_permission_assignments(role, permission, resource, scope);

-- 5. governance_usage_quotas
CREATE TABLE governance_usage_quotas (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    module VARCHAR(100) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    operation VARCHAR(100) NOT NULL,
    period_start TIMESTAMP NOT NULL,
    period_end TIMESTAMP NOT NULL,
    count BIGINT NOT NULL DEFAULT 0,
    limit_value BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_gov_quota_module ON governance_usage_quotas(module);
CREATE INDEX idx_gov_quota_user ON governance_usage_quotas(user_id);
CREATE INDEX idx_gov_quota_operation ON governance_usage_quotas(operation);
CREATE INDEX idx_gov_quota_period ON governance_usage_quotas(period_start, period_end);
CREATE UNIQUE INDEX idx_gov_quota_lookup ON governance_usage_quotas(module, user_id, operation, period_start);

-- 6. governance_compliance_reports
CREATE TABLE governance_compliance_reports (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    policy_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL,
    violations TEXT,
    checked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    module VARCHAR(100),
    details TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_gov_report_policy_id ON governance_compliance_reports(policy_id);
CREATE INDEX idx_gov_report_status ON governance_compliance_reports(status);
CREATE INDEX idx_gov_report_checked_at ON governance_compliance_reports(checked_at);
CREATE INDEX idx_gov_report_module ON governance_compliance_reports(module);

ALTER TABLE governance_compliance_reports
    ADD CONSTRAINT fk_gov_report_policy
    FOREIGN KEY (policy_id) REFERENCES governance_policies(id)
    ON DELETE CASCADE;
```
