# Assistant Platform Database Schema

**Version:** 1.0.0
**Migration:** V19__sprint17_assistant_platform.sql
**Engine:** PostgreSQL (H2 compatible for dev/test)

---

## Tables

### assistant_copilot_profiles

Stores copilot definition and configuration for each of the 12 domain copilots.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| assistant_type | VARCHAR(50) | NOT NULL, UNIQUE | Copilot type (CUSTOMER, PRODUCT, TRAINING, etc.) |
| name | VARCHAR(255) | NOT NULL | Display name |
| description | TEXT | | Description of copilot capabilities |
| enabled | BOOLEAN | NOT NULL, DEFAULT TRUE | Whether copilot is active |
| capabilities | TEXT | | JSON array of supported action names |
| supported_intents | TEXT | | JSON array of supported IntentCategory values |
| configuration | TEXT | | JSON configuration object (timeout, retry, cache) |
| created_by | VARCHAR(255) | NOT NULL | Creator user ID |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Last update timestamp |
| is_deleted | BOOLEAN | NOT NULL, DEFAULT FALSE | Soft delete flag |

**Indexes:** `idx_assistant_profiles_type` (assistant_type), `idx_assistant_profiles_enabled` (enabled)

---

### assistant_intents

Records all resolved user intents from the Intent Engine.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| session_id | UUID | NOT NULL | FK to assistant_sessions |
| user_id | VARCHAR(255) | NOT NULL | User who submitted the utterance |
| utterance | TEXT | NOT NULL | Original user utterance |
| category | VARCHAR(50) | NOT NULL | IntentCategory (INQUIRY, COMMAND, SEARCH, etc.) |
| confidence | DOUBLE | NOT NULL, DEFAULT 0.0 | Resolution confidence score (0-1) |
| entities | TEXT | | JSON object of extracted entities |
| resolved_copilot | VARCHAR(50) | | Resolved AssistantType |
| status | VARCHAR(50) | NOT NULL | PENDING, RESOLVED, AMBIGUOUS, UNKNOWN |
| alternatives | TEXT | | JSON array of alternative copilot resolutions |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Creation timestamp |

**Indexes:** `idx_assistant_intents_session` (session_id), `idx_assistant_intents_user` (user_id), `idx_assistant_intents_category` (category), `idx_assistant_intents_status` (status), `idx_assistant_intents_created_at` (created_at)

---

### assistant_task_plans

Stores task execution plans created from resolved intents.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| intent_id | UUID | NOT NULL | FK to assistant_intents |
| session_id | UUID | NOT NULL | FK to assistant_sessions |
| status | VARCHAR(50) | NOT NULL, DEFAULT 'PENDING' | PENDING, IN_PROGRESS, COMPLETED, FAILED, ROLLED_BACK |
| priority | VARCHAR(20) | NOT NULL, DEFAULT 'MEDIUM' | LOW, MEDIUM, HIGH, CRITICAL |
| step_count | INTEGER | NOT NULL, DEFAULT 0 | Number of steps in plan |
| error_detail | TEXT | | Error message if plan failed |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Creation timestamp |
| completed_at | TIMESTAMP | | Plan completion timestamp |

**Indexes:** `idx_assistant_task_plans_intent` (intent_id), `idx_assistant_task_plans_session` (session_id), `idx_assistant_task_plans_status` (status), `idx_assistant_task_plans_priority` (priority)

---

### assistant_task_steps

Stores individual task step execution records.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| plan_id | UUID | NOT NULL | FK to assistant_task_plans |
| sequence | INTEGER | NOT NULL | Step order in plan |
| copilot_type | VARCHAR(50) | NOT NULL | Copilot assigned to this step |
| action | VARCHAR(100) | NOT NULL | Action to execute |
| input_data | TEXT | | JSON input parameters |
| output_data | TEXT | | JSON output result |
| status | VARCHAR(50) | NOT NULL, DEFAULT 'PENDING' | PENDING, IN_PROGRESS, COMPLETED, FAILED, SKIPPED |
| depends_on | TEXT | | JSON array of step IDs this step depends on |
| error_detail | TEXT | | Error message if step failed |
| latency_ms | INTEGER | | Execution time in milliseconds |
| started_at | TIMESTAMP | | Step start timestamp |
| completed_at | TIMESTAMP | | Step completion timestamp |

**Indexes:** `idx_assistant_task_steps_plan` (plan_id), `idx_assistant_task_steps_copilot` (copilot_type), `idx_assistant_task_steps_status` (status)

---

### assistant_sessions

Tracks assistant session lifecycle.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| user_id | VARCHAR(255) | NOT NULL | Owner user ID |
| assistant_type | VARCHAR(50) | NOT NULL | Active copilot type |
| status | VARCHAR(50) | NOT NULL, DEFAULT 'ACTIVE' | ACTIVE, IDLE, CLOSED, EXPIRED |
| context | TEXT | | JSON session context object |
| metadata | TEXT | | JSON metadata (source, referrer, etc.) |
| message_count | INTEGER | NOT NULL, DEFAULT 0 | Total messages in session |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Last activity timestamp |
| expired_at | TIMESTAMP | NOT NULL | Session expiry timestamp |
| is_deleted | BOOLEAN | NOT NULL, DEFAULT FALSE | Soft delete flag |

**Indexes:** `idx_assistant_sessions_user` (user_id), `idx_assistant_sessions_status` (status), `idx_assistant_sessions_type` (assistant_type), `idx_assistant_sessions_expired` (expired_at)

---

### assistant_feedback

Stores user feedback on assistant responses.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| session_id | UUID | NOT NULL | FK to assistant_sessions |
| intent_id | UUID | | FK to assistant_intents (nullable for general feedback) |
| user_id | VARCHAR(255) | NOT NULL | User who submitted feedback |
| rating | INTEGER | NOT NULL | Rating 1-5 |
| comment | TEXT | | Optional user comment |
| categories | TEXT | | JSON array of feedback category tags |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Creation timestamp |

**Indexes:** `idx_assistant_feedback_session` (session_id), `idx_assistant_feedback_user` (user_id), `idx_assistant_feedback_rating` (rating)

---

### assistant_audit_logs

Security audit trail for all assistant operations.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| session_id | UUID | | FK to assistant_sessions (nullable) |
| user_id | VARCHAR(255) | NOT NULL | User who performed the action |
| action | VARCHAR(100) | NOT NULL | Action performed (CHAT, RESOLVE_INTENT, EXECUTE_TASK, etc.) |
| resource | VARCHAR(255) | | Target resource identifier |
| detail | TEXT | | JSON detail of the action |
| ip_address | VARCHAR(45) | | Client IP address |
| user_agent | VARCHAR(500) | | Client user agent string |
| status | VARCHAR(20) | NOT NULL | SUCCESS, FAILURE, BLOCKED |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Creation timestamp |

**Indexes:** `idx_assistant_audit_session` (session_id), `idx_assistant_audit_user` (user_id), `idx_assistant_audit_action` (action), `idx_assistant_audit_created_at` (created_at)

---

### assistant_contexts

Session-level key-value context storage.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| session_id | UUID | NOT NULL | FK to assistant_sessions |
| context_key | VARCHAR(255) | NOT NULL | Context key name |
| context_value | TEXT | NOT NULL | Context value |
| value_type | VARCHAR(50) | NOT NULL, DEFAULT 'STRING' | STRING, NUMBER, BOOLEAN, JSON |
| ttl_seconds | INTEGER | | Time-to-live in seconds |
| created_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Creation timestamp |
| expires_at | TIMESTAMP | | Context expiry timestamp |

**Indexes:** `idx_assistant_contexts_session` (session_id), `idx_assistant_contexts_expires` (expires_at)

**Unique Constraint:** `uq_assistant_contexts_session_key` (session_id, context_key)

---

## Relationships

```
assistant_sessions ──< assistant_contexts
                   │
                   ├──< assistant_intents ──< assistant_task_plans ──< assistant_task_steps
                   │
                   ├──< assistant_feedback
                   │
                   └──< assistant_audit_logs

assistant_copilot_profiles (standalone, referenced by type enum)
assistant_feedback ──> assistant_intents (nullable FK)
```

---

## Summary

| Table | Purpose | Key Indexes |
|-------|---------|-------------|
| assistant_copilot_profiles | Copilot definitions (12 types) | type, enabled |
| assistant_intents | Resolved user intents | session, user, category, status |
| assistant_task_plans | Task execution plans | intent, session, status, priority |
| assistant_task_steps | Individual step executions | plan, copilot, status |
| assistant_sessions | Session lifecycle tracking | user, status, type, expired |
| assistant_feedback | User feedback ratings | session, user, rating |
| assistant_audit_logs | Security audit trail | session, user, action, created |
| assistant_contexts | Session context storage | session, expires |
