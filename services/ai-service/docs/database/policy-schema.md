# Policy Engine Database Schema

**Migration:** `V21__sprint18_policy.sql`  
**Tables:** 7  
**Indexes:** 24  

All tables use UUID primary keys, TEXT for JSON data, and soft-delete (`is_deleted` flag). H2-compatible (TIMESTAMP not TIMESTAMPTZ, TEXT not JSONB).

## Table: `ai_policies`

Policies define the rules and conditions for AI service enforcement.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| name | VARCHAR(200) | | Policy name |
| description | TEXT | | Policy description |
| type | VARCHAR(50) | | PolicyType enum value |
| status | VARCHAR(50) | | PolicyStatus enum value |
| severity | VARCHAR(50) | | PolicySeverity enum value |
| scope | VARCHAR(50) | | PolicyScope enum value |
| priority | INT | DEFAULT 0 | Evaluation priority (higher = first) |
| module | VARCHAR(100) | | Target module (null = all) |
| rules | TEXT | | Serialized rules (JSON) |
| conditions | TEXT | | Serialized conditions (JSON) |
| metadata | TEXT | | Serialized metadata (JSON) |
| is_active | BOOLEAN | DEFAULT TRUE | Active flag |
| is_system | BOOLEAN | DEFAULT FALSE | System-generated policy |
| created_by | UUID | | Creator user ID |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Creation time |
| updated_at | TIMESTAMP | | Last update time |
| is_deleted | BOOLEAN | DEFAULT FALSE | Soft-delete flag |

**Indexes:**
- `idx_policies_type` ON type
- `idx_policies_status` ON status
- `idx_policies_scope` ON scope
- `idx_policies_module` ON module
- `idx_policies_created_at` ON created_at

## Table: `ai_policy_rules`

Individual rules within a policy, each with an expression and decision outcome.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| policy_id | UUID | | FK to ai_policies.id |
| name | VARCHAR(200) | | Rule name |
| description | TEXT | | Rule description |
| expression | TEXT | | Rule expression string |
| parameters | TEXT | | Serialized parameters (JSON) |
| decision | VARCHAR(50) | | PolicyDecision enum value |
| rule_order | INT | DEFAULT 0 | Evaluation order |
| is_active | BOOLEAN | DEFAULT TRUE | Active flag |
| created_at | TIMESTAMP | | Creation time |
| updated_at | TIMESTAMP | | Last update time |
| is_deleted | BOOLEAN | DEFAULT FALSE | Soft-delete flag |

**Indexes:**
- `idx_policy_rules_policy` ON policy_id
- `idx_policy_rules_created_at` ON created_at

## Table: `ai_policy_conditions`

Conditions attached to rules, evaluated by `ConditionEvaluator`.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| rule_id | UUID | | FK to ai_policy_rules.id |
| field | VARCHAR(200) | | Target field name |
| operator | VARCHAR(50) | | ConditionOperator enum value |
| condition_value | TEXT | | Value to compare against |
| negate | BOOLEAN | DEFAULT FALSE | Invert result |
| condition_order | INT | DEFAULT 0 | Evaluation order |
| created_at | TIMESTAMP | | Creation time |
| is_deleted | BOOLEAN | DEFAULT FALSE | Soft-delete flag |

**Indexes:**
- `idx_policy_conditions_rule` ON rule_id
- `idx_policy_conditions_created_at` ON created_at

## Table: `ai_policy_versions`

Version history for policies.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| policy_id | UUID | | FK to ai_policies.id |
| version_number | INT | | Sequential version number |
| name | VARCHAR(200) | | Version name |
| description | TEXT | | Version description |
| content | TEXT | | Full policy content snapshot |
| status | VARCHAR(50) | | PolicyStatus at version time |
| change_notes | TEXT | | Description of changes |
| created_by | UUID | | Creator user ID |
| created_at | TIMESTAMP | | Creation time |
| is_deleted | BOOLEAN | DEFAULT FALSE | Soft-delete flag |

**Indexes:**
- `idx_policy_versions_policy` ON policy_id
- `idx_policy_versions_created_at` ON created_at

## Table: `ai_policy_evaluations`

Evaluation results from policy engine execution.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| request_id | UUID | | Evaluation request ID |
| policy_id | UUID | | Evaluated policy ID |
| decision | VARCHAR(50) | | PolicyDecision result |
| violations | TEXT | | Serialized violations (JSON) |
| context | TEXT | | Evaluation context (JSON) |
| evaluation_time_ms | BIGINT | | Time spent in milliseconds |
| rules_evaluated | INT | | Number of rules evaluated |
| rules_passed | INT | | Number of rules passed |
| rules_failed | INT | | Number of rules failed |
| matched | BOOLEAN | | Whether policy matched |
| timestamp | TIMESTAMP | | Evaluation timestamp |
| is_deleted | BOOLEAN | DEFAULT FALSE | Soft-delete flag |

**Indexes:**
- `idx_policy_evaluations_request` ON request_id
- `idx_policy_evaluations_policy` ON policy_id
- `idx_policy_evaluations_decision` ON decision
- `idx_policy_evaluations_timestamp` ON timestamp

## Table: `ai_policy_audit`

Append-only audit log for policy evaluations.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| policy_id | UUID | | Policy UUID (nullable) |
| request_id | UUID | | Evaluation request UUID |
| action | VARCHAR(200) | | Action performed |
| decision | VARCHAR(50) | | PolicyDecision result |
| violations | TEXT | | Serialized violations (JSON string) |
| details | TEXT | | Context details (JSON string) |
| user_id | VARCHAR(100) | | User who triggered evaluation |
| processing_time_ms | BIGINT | | Time in milliseconds |
| success | BOOLEAN | | Whether evaluation succeeded |
| timestamp | TIMESTAMP | | Event timestamp |
| created_at | TIMESTAMP | | Record creation time |
| is_deleted | BOOLEAN | DEFAULT FALSE | Soft-delete flag |

**Indexes:**
- `idx_policy_audit_policy` ON policy_id
- `idx_policy_audit_request` ON request_id
- `idx_policy_audit_decision` ON decision
- `idx_policy_audit_user` ON user_id
- `idx_policy_audit_created_at` ON created_at

## Table: `ai_policy_registry`

Registry for policy modules and their registration status.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| name | VARCHAR(200) | | Registry entry name |
| module | VARCHAR(100) | | Associated module |
| type | VARCHAR(50) | | PolicyType enum value |
| scope | VARCHAR(50) | | PolicyScope enum value |
| is_active | BOOLEAN | DEFAULT TRUE | Active flag |
| is_registered | BOOLEAN | DEFAULT FALSE | Registration flag |
| config | TEXT | | Serialized configuration (JSON) |
| registered_at | TIMESTAMP | | Registration timestamp |
| updated_at | TIMESTAMP | | Last update time |
| is_deleted | BOOLEAN | DEFAULT FALSE | Soft-delete flag |

**Indexes:**
- `idx_policy_registry_module` ON module
- `idx_policy_registry_type` ON type
- `idx_policy_registry_scope` ON scope
- `idx_policy_registry_updated_at` ON updated_at

## Flyway Migration SQL

The complete migration is in `V21__sprint18_policy.sql`. Key characteristics:
- All tables use `CREATE TABLE IF NOT EXISTS`
- All indexes use `CREATE INDEX IF NOT EXISTS`
- UUID primary keys (no auto-increment)
- Soft-delete via `is_deleted BOOLEAN DEFAULT FALSE`
- JSON data stored as TEXT (H2-compatible, no JSONB dependency)
- Timestamps use TIMESTAMP (not TIMESTAMPTZ) for H2 compatibility
