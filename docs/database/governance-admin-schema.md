# Governance Admin Schema

## Migration: V27__sprint18_governance_admin.sql

## Tables (7)

### 1. admin_configuration
Stores all governance module configuration entries.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, DEFAULT gen_random_uuid() | Primary key |
| config_key | VARCHAR(255) | NOT NULL | Configuration key |
| config_module | VARCHAR(50) | NOT NULL | Target module |
| environment | VARCHAR(50) | NOT NULL | Environment profile |
| config_value | TEXT | NOT NULL | Configuration value (string, number, boolean, JSON) |
| value_type | VARCHAR(20) | NOT NULL | String, Number, Boolean, JSON, Encrypted |
| description | TEXT | | Human-readable description |
| status | VARCHAR(20) | NOT NULL DEFAULT 'ACTIVE' | Active, Archived, Deprecated |
| current_version | INTEGER | NOT NULL DEFAULT 1 | Latest version number |
| created_by | VARCHAR(100) | NOT NULL | User who created |
| updated_by | VARCHAR(100) | | User who last updated |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | | Last update timestamp |
| is_deleted | BOOLEAN | NOT NULL DEFAULT FALSE | Soft delete flag |

**Indexes:**
- `idx_admin_config_key_module_env` ON (config_key, config_module, environment) — UNIQUE where is_deleted=false
- `idx_admin_config_module` ON (config_module)
- `idx_admin_config_environment` ON (environment)
- `idx_admin_config_status` ON (status)

### 2. feature_flags
Manages feature flags with global/environment/module scoping.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, DEFAULT gen_random_uuid() | Primary key |
| flag_name | VARCHAR(100) | NOT NULL | Feature flag name |
| enabled | BOOLEAN | NOT NULL DEFAULT FALSE | Flag state |
| scope | VARCHAR(20) | NOT NULL | Global, Environment, Module |
| module | VARCHAR(50) | | Target module (for MODULE scope) |
| environment | VARCHAR(50) | | Target environment (for ENVIRONMENT scope) |
| description | TEXT | | Flag description |
| created_by | VARCHAR(100) | NOT NULL | User who created |
| updated_by | VARCHAR(100) | | User who last updated |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | | Last update timestamp |
| is_deleted | BOOLEAN | NOT NULL DEFAULT FALSE | Soft delete flag |

**Indexes:**
- `idx_feature_flags_name_scope_module_env` ON (flag_name, scope, module, environment) — UNIQUE where is_deleted=false
- `idx_feature_flags_module` ON (module)

### 3. environment_profiles
Defines environment profiles.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, DEFAULT gen_random_uuid() | Primary key |
| env_name | VARCHAR(50) | NOT NULL | Environment name |
| display_name | VARCHAR(100) | | Display name |
| description | TEXT | | Environment description |
| is_active | BOOLEAN | NOT NULL DEFAULT TRUE | Active flag |
| config | TEXT | | JSON configuration overrides |
| created_by | VARCHAR(100) | NOT NULL | User who created |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | | Last update timestamp |
| is_deleted | BOOLEAN | NOT NULL DEFAULT FALSE | Soft delete flag |

**Indexes:**
- `idx_env_profiles_name` ON (env_name) — UNIQUE where is_deleted=false

### 4. configuration_versions
Immutable version history for configuration changes.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, DEFAULT gen_random_uuid() | Primary key |
| config_id | UUID | FK -> admin_configuration.id | Configuration reference |
| version_number | INTEGER | NOT NULL | Sequential version |
| config_value | TEXT | NOT NULL | Value at this version |
| change_type | VARCHAR(20) | NOT NULL | Created, Updated, Rollback |
| changed_by | VARCHAR(100) | NOT NULL | User who made change |
| change_reason | TEXT | | Reason for change |
| diff_from_previous | TEXT | | JSON diff |
| created_at | TIMESTAMP | NOT NULL | Version creation timestamp |

**Indexes:**
- `idx_config_versions_config_id` ON (config_id)
- `idx_config_versions_config_version` ON (config_id, version_number) — UNIQUE

### 5. configuration_snapshots
Point-in-time snapshots of all configurations.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, DEFAULT gen_random_uuid() | Primary key |
| snapshot_name | VARCHAR(100) | NOT NULL | Snapshot name |
| description | TEXT | | Description |
| snapshot_data | TEXT | NOT NULL | JSON snapshot of all configs |
| config_count | INTEGER | NOT NULL | Number of configurations |
| created_by | VARCHAR(100) | NOT NULL | User who created |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| is_deleted | BOOLEAN | NOT NULL DEFAULT FALSE | Soft delete flag |

**Indexes:**
- `idx_config_snapshots_name` ON (snapshot_name) — UNIQUE where is_deleted=false
- `idx_config_snapshots_created_at` ON (created_at)

### 6. admin_audit
Immutable audit trail for all admin operations.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, DEFAULT gen_random_uuid() | Primary key |
| operation_type | VARCHAR(50) | NOT NULL | CONFIG_CREATE, CONFIG_UPDATE, FEATURE_TOGGLE, MODULE_ENABLE, etc. |
| target_type | VARCHAR(50) | NOT NULL | Configuration, FeatureFlag, Module, Environment |
| target_id | VARCHAR(255) | | Identifier of target |
| module | VARCHAR(50) | | Related module |
| environment | VARCHAR(50) | | Related environment |
| details | TEXT | | JSON details of operation |
| previous_state | TEXT | | JSON previous state |
| new_state | TEXT | | JSON new state |
| performed_by | VARCHAR(100) | NOT NULL | User who performed operation |
| performed_at | TIMESTAMP | NOT NULL | Operation timestamp |
| status | VARCHAR(20) | NOT NULL | Success, Failure, DryRun |

**Indexes:**
- `idx_admin_audit_operation_type` ON (operation_type)
- `idx_admin_audit_performed_by` ON (performed_by)
- `idx_admin_audit_performed_at` ON (performed_at)
- `idx_admin_audit_target` ON (target_type, target_id)

### 7. admin_operations
Tracks administrative operations and their status.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK, DEFAULT gen_random_uuid() | Primary key |
| operation_type | VARCHAR(50) | NOT NULL | Import, Export, Rollback, RestoreSnapshot |
| status | VARCHAR(20) | NOT NULL | Pending, InProgress, Completed, Failed |
| initiated_by | VARCHAR(100) | NOT NULL | User who initiated |
| parameters | TEXT | | JSON operation parameters |
| result | TEXT | | JSON operation result |
| error_message | TEXT | | Error details on failure |
| started_at | TIMESTAMP | | Start timestamp |
| completed_at | TIMESTAMP | | Completion timestamp |
| created_at | TIMESTAMP | NOT NULL | Record creation timestamp |

**Indexes:**
- `idx_admin_operations_status` ON (status)
- `idx_admin_operations_type` ON (operation_type)

## Database Trigger
Immutable audit protection — a BEFORE UPDATE/DELETE trigger on `admin_audit` table prevents any modification or deletion of audit records.

## Migration Notes
- H2-compatible for tests (TIMESTAMP not TIMESTAMPTZ, TEXT not JSONB, no gen_random_uuid())
- Soft deletes used on all tables except immutable tables (admin_audit, configuration_versions, configuration_snapshots)
- Configuration versions are append-only — no UPDATE or DELETE allowed
