# Governance Automation Database Schema

## Flyway Migration: V28__sprint18_automation_lifecycle.sql

## Tables (9)

### automation_jobs
Job definitions for scheduled and event-driven automation.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| name | VARCHAR(255) | NOT NULL | Job name |
| type | VARCHAR(50) | NOT NULL | Job type (SCHEDULED, MANUAL, EVENT_DRIVEN, RECURRING, TRIGGERED) |
| status | VARCHAR(50) | NOT NULL, DEFAULT 'ACTIVE' | Active/Inactive |
| job_class | VARCHAR(500) | NOT NULL | Fully qualified job class name |
| schedule_config | TEXT | | JSON schedule configuration |
| job_config | TEXT | | JSON job configuration |
| retry_policy_id | UUID | FK -> automation_retry_policies.id | Retry policy reference |
| escalation_policy_id | UUID | FK -> automation_escalation_policies.id | Escalation policy reference |
| enabled | BOOLEAN | NOT NULL, DEFAULT true | Job enabled flag |
| last_run_at | TIMESTAMP | | Last execution timestamp |
| last_run_status | VARCHAR(50) | | Last execution status |
| next_run_at | TIMESTAMP | | Next scheduled execution |
| execution_count | BIGINT | DEFAULT 0 | Total execution count |
| failure_count | BIGINT | DEFAULT 0 | Total failure count |
| created_by | VARCHAR(255) | | Actor UUID |
| is_deleted | BOOLEAN | DEFAULT false | Soft delete flag |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |

**Indexes:**
- `idx_automation_jobs_type` ON (type)
- `idx_automation_jobs_status` ON (status)
- `idx_automation_jobs_next_run` ON (next_run_at) WHERE enabled = true
- `idx_automation_jobs_type_status` ON (type, status)
- `idx_automation_jobs_deleted` ON (is_deleted)

### automation_scheduled_tasks
Scheduled task instances with execution tracking.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| job_id | UUID | FK -> automation_jobs.id, NOT NULL | Job reference |
| scheduled_at | TIMESTAMP | NOT NULL | Scheduled execution time |
| status | VARCHAR(50) | NOT NULL | SCHEDULED, RUNNING, COMPLETED, FAILED |
| execution_window_start | TIMESTAMP | | Execution window start |
| execution_window_end | TIMESTAMP | | Execution window end |
| actual_start_at | TIMESTAMP | | Actual execution start |
| actual_end_at | TIMESTAMP | | Actual execution end |
| result | TEXT | | Execution result JSON |
| error_message | TEXT | | Error details if failed |
| lock_acquired | BOOLEAN | DEFAULT false | Distributed lock flag |
| lock_expires_at | TIMESTAMP | | Lock expiration |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |

**Indexes:**
- `idx_auto_scheduled_tasks_job` ON (job_id)
- `idx_auto_scheduled_tasks_status` ON (status)
- `idx_auto_scheduled_tasks_scheduled` ON (scheduled_at) WHERE status = 'SCHEDULED'

### automation_lifecycle_states
Current lifecycle state for managed entities.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| entity_type | VARCHAR(100) | NOT NULL | Managed entity type |
| entity_id | UUID | NOT NULL | Entity identifier |
| current_state | VARCHAR(50) | NOT NULL | Current lifecycle state |
| previous_state | VARCHAR(50) | | Previous lifecycle state |
| entered_at | TIMESTAMP | NOT NULL | When current state was entered |
| expires_at | TIMESTAMP | | Expiration timestamp |
| metadata | TEXT | | JSON metadata |
| is_deleted | BOOLEAN | DEFAULT false | Soft delete flag |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |

**Indexes:**
- `idx_auto_lifecycle_entity` ON (entity_type, entity_id) UNIQUE WHERE is_deleted = false
- `idx_auto_lifecycle_state` ON (current_state)
- `idx_auto_lifecycle_expires` ON (expires_at) WHERE expires_at IS NOT NULL

### automation_lifecycle_transitions
History of all lifecycle state transitions.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| entity_type | VARCHAR(100) | NOT NULL | Managed entity type |
| entity_id | UUID | NOT NULL | Entity identifier |
| from_state | VARCHAR(50) | NOT NULL | Source state |
| to_state | VARCHAR(50) | NOT NULL | Target state |
| status | VARCHAR(50) | NOT NULL | COMPLETED, FAILED, CANCELLED |
| reason | TEXT | | Transition reason |
| actor | VARCHAR(255) | NOT NULL | Actor UUID or 'system' |
| metadata | TEXT | | JSON metadata |
| duration_ms | BIGINT | | Transition duration |
| error_message | TEXT | | Error details if failed |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |

**Indexes:**
- `idx_auto_transitions_entity` ON (entity_type, entity_id)
- `idx_auto_transitions_entity_time` ON (entity_type, entity_id, created_at DESC)
- `idx_auto_transitions_actor` ON (actor)
- `idx_auto_transitions_status` ON (status)
- `idx_auto_transitions_created` ON (created_at)

### automation_workflow_executions
Workflow execution instances.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| workflow_name | VARCHAR(255) | NOT NULL | Workflow definition name |
| status | VARCHAR(50) | NOT NULL | Execution status |
| trigger_type | VARCHAR(50) | NOT NULL | SCHEDULED, EVENT, MANUAL |
| trigger_source | VARCHAR(255) | | Trigger identifier |
| input | TEXT | | JSON input payload |
| output | TEXT | | JSON output result |
| current_step | VARCHAR(255) | | Current executing step |
| total_steps | INTEGER | NOT NULL | Total steps in workflow |
| completed_steps | INTEGER | DEFAULT 0 | Completed step count |
| failed_steps | INTEGER | DEFAULT 0 | Failed step count |
| started_at | TIMESTAMP | | Execution start |
| completed_at | TIMESTAMP | | Execution completion |
| timeout_at | TIMESTAMP | | Workflow timeout |
| error_message | TEXT | | Error details |
| is_deleted | BOOLEAN | DEFAULT false | Soft delete flag |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |

**Indexes:**
- `idx_auto_workflow_status` ON (status)
- `idx_auto_workflow_trigger` ON (trigger_type, trigger_source)
- `idx_auto_workflow_created` ON (created_at)
- `idx_auto_workflow_deleted` ON (is_deleted)

### automation_workflow_history
Step-level execution history for workflows.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| execution_id | UUID | FK -> automation_workflow_executions.id, NOT NULL | Workflow execution reference |
| step_name | VARCHAR(255) | NOT NULL | Step identifier |
| step_type | VARCHAR(50) | NOT NULL | EXECUTE, COMPENSATE, RETRY |
| status | VARCHAR(50) | NOT NULL | Step execution status |
| input | TEXT | | JSON step input |
| output | TEXT | | JSON step output |
| retry_count | INTEGER | DEFAULT 0 | Number of retries |
| started_at | TIMESTAMP | | Step start |
| completed_at | TIMESTAMP | | Step completion |
| duration_ms | BIGINT | | Step duration |
| error_message | TEXT | | Error details |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |

**Indexes:**
- `idx_auto_workflow_history_exec` ON (execution_id)
- `idx_auto_workflow_history_step` ON (execution_id, step_name)
- `idx_auto_workflow_history_status` ON (status)

### automation_retry_policies
Retry policy configuration.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| name | VARCHAR(255) | NOT NULL | Policy name |
| max_attempts | INTEGER | NOT NULL, DEFAULT 3 | Maximum retry attempts |
| backoff_interval_ms | BIGINT | NOT NULL, DEFAULT 5000 | Base backoff interval |
| backoff_multiplier | DOUBLE | NOT NULL, DEFAULT 2.0 | Exponential backoff multiplier |
| max_backoff_interval_ms | BIGINT | NOT NULL, DEFAULT 300000 | Maximum backoff cap |
| jitter_enabled | BOOLEAN | DEFAULT true | Add jitter to backoff |
| retryable_exceptions | TEXT | | JSON list of retryable exception classes |
| description | TEXT | | Policy description |
| is_deleted | BOOLEAN | DEFAULT false | Soft delete flag |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |

**Indexes:**
- `idx_auto_retry_policies_name` ON (name)
- `idx_auto_retry_policies_deleted` ON (is_deleted)

### automation_escalation_policies
Escalation policy configuration.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| name | VARCHAR(255) | NOT NULL | Policy name |
| levels | TEXT | NOT NULL | JSON array of escalation levels |
| default_action | VARCHAR(100) | NOT NULL | Default escalation action |
| description | TEXT | | Policy description |
| is_deleted | BOOLEAN | DEFAULT false | Soft delete flag |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |

**Indexes:**
- `idx_auto_escalation_policies_name` ON (name)
- `idx_auto_escalation_policies_deleted` ON (is_deleted)

### automation_expiration_policies
Expiration policy configuration for entity lifecycle.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| name | VARCHAR(255) | NOT NULL | Policy name |
| entity_type | VARCHAR(100) | NOT NULL | Target entity type |
| source_state | VARCHAR(50) | NOT NULL | State to monitor for expiration |
| target_state | VARCHAR(50) | NOT NULL | State to transition to on expiration |
| ttl_days | INTEGER | NOT NULL | Days before expiration |
| notification_before_days | INTEGER | DEFAULT 7 | Days before expiration to notify |
| auto_execute | BOOLEAN | DEFAULT true | Auto-execute expiration |
| action_on_expire | VARCHAR(100) | NOT NULL | ARCHIVE, DELETE, SUSPEND, NOTIFY |
| description | TEXT | | Policy description |
| is_deleted | BOOLEAN | DEFAULT false | Soft delete flag |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |

**Indexes:**
- `idx_auto_expiration_entity` ON (entity_type)
- `idx_auto_expiration_source_state` ON (source_state)
- `idx_auto_expiration_deleted` ON (is_deleted)

## Relationships
```
automation_jobs → automation_retry_policies (optional FK)
automation_jobs → automation_escalation_policies (optional FK)
automation_scheduled_tasks → automation_jobs (FK)
automation_workflow_history → automation_workflow_executions (FK)
```
