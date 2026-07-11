# Prompt Management Database Schema

**Version:** 1.0.0
**Last Updated:** 2026-07-11
**Migration:** V13__sprint17_ai_prompt_management.sql

---

## Overview

The Prompt Management Platform uses 6 tables under the `ai_prompt_*` namespace. All tables use UUID primary keys, audit columns (created_at, updated_at, is_deleted, deleted_at), and appropriate indexes.

---

## Table: ai_prompt_categories

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| name | VARCHAR(100) | NOT NULL, UNIQUE | Category name |
| description | TEXT | | Category description |
| icon | VARCHAR(50) | | Icon identifier |
| display_order | INT | DEFAULT 0 | Sort order |
| is_active | BOOLEAN | NOT NULL, DEFAULT TRUE | Active flag |
| created_by | UUID | | Creator user ID |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Creation timestamp |
| updated_by | UUID | | Last modifier |
| updated_at | TIMESTAMPTZ | | Modification timestamp |
| is_deleted | BOOLEAN | NOT NULL, DEFAULT FALSE | Soft delete flag |
| deleted_at | TIMESTAMPTZ | | Deletion timestamp |

**Indexes:**
- `idx_prompt_categories_active` — Partial index on `is_active` WHERE NOT is_deleted

---

## Table: ai_prompt_templates

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| category_id | UUID | NOT NULL, FK → ai_prompt_categories(id) | Parent category |
| name | VARCHAR(255) | NOT NULL | Template name |
| description | TEXT | | Template description |
| template_text | TEXT | NOT NULL | Prompt template with `{{variable}}` placeholders |
| status | VARCHAR(50) | NOT NULL, DEFAULT 'DRAFT' | Lifecycle status |
| current_version | INT | NOT NULL, DEFAULT 1 | Latest version number |
| tags | TEXT[] | | Array of tags |
| is_active | BOOLEAN | NOT NULL, DEFAULT TRUE | Active flag |
| created_by | UUID | | Creator user ID |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Creation timestamp |
| updated_by | UUID | | Last modifier |
| updated_at | TIMESTAMPTZ | | Modification timestamp |
| is_deleted | BOOLEAN | NOT NULL, DEFAULT FALSE | Soft delete flag |
| deleted_at | TIMESTAMPTZ | | Deletion timestamp |

**Constraints:**
- UNIQUE (name, category_id)

**Indexes:**
- `idx_prompt_templates_category` — Partial index on `category_id` WHERE NOT is_deleted
- `idx_prompt_templates_status` — Partial index on `status` WHERE NOT is_deleted
- `idx_prompt_templates_active` — Partial index on `is_active` WHERE NOT is_deleted

---

## Table: ai_prompt_versions

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| template_id | UUID | NOT NULL, FK → ai_prompt_templates(id) | Parent template |
| version_number | INT | NOT NULL | Sequential version number |
| template_text | TEXT | NOT NULL | Snapshot of template text |
| status | VARCHAR(50) | NOT NULL, DEFAULT 'DRAFT' | Version status |
| change_notes | TEXT | | What changed in this version |
| activation_date | TIMESTAMPTZ | | When version became active |
| created_by | UUID | | Creator user ID |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Creation timestamp |
| approved_by | UUID | | Approver user ID |
| approved_at | TIMESTAMPTZ | | Approval timestamp |
| is_deleted | BOOLEAN | NOT NULL, DEFAULT FALSE | Soft delete flag |
| deleted_at | TIMESTAMPTZ | | Deletion timestamp |

**Constraints:**
- UNIQUE (template_id, version_number)

**Indexes:**
- `idx_prompt_versions_template` — Partial index on `template_id` WHERE NOT is_deleted
- `idx_prompt_versions_status` — Partial index on `status` WHERE NOT is_deleted

---

## Table: ai_prompt_variables

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| template_id | UUID | NOT NULL, FK → ai_prompt_templates(id) | Parent template |
| name | VARCHAR(255) | NOT NULL | Variable name (without `{{}}`) |
| var_type | VARCHAR(50) | NOT NULL, DEFAULT 'STRING' | Variable type |
| required | BOOLEAN | NOT NULL, DEFAULT TRUE | Is required |
| default_value | TEXT | | Default value if not provided |
| description | TEXT | | Variable description |
| validation_regex | VARCHAR(500) | | Regex pattern for validation |
| display_order | INT | DEFAULT 0 | Display sort order |
| created_at | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Creation timestamp |
| updated_at | TIMESTAMPTZ | | Modification timestamp |
| is_deleted | BOOLEAN | NOT NULL, DEFAULT FALSE | Soft delete flag |
| deleted_at | TIMESTAMPTZ | | Deletion timestamp |

**Constraints:**
- UNIQUE (template_id, name)

**Indexes:**
- `idx_prompt_variables_template` — Partial index on `template_id` WHERE NOT is_deleted

---

## Table: ai_prompt_audit

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| template_id | UUID | FK → ai_prompt_templates(id) | Related template |
| version_id | UUID | FK → ai_prompt_versions(id) | Related version |
| action | VARCHAR(100) | NOT NULL | Audit action type |
| entity_type | VARCHAR(50) | NOT NULL | Entity type (TEMPLATE, VERSION, CATEGORY) |
| entity_id | UUID | | Entity identifier |
| previous_value | JSONB | | Previous state snapshot |
| new_value | JSONB | | New state snapshot |
| changed_by | UUID | | User who made the change |
| changed_at | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Change timestamp |
| details | TEXT | | Human-readable details |

**Indexes:**
- `idx_prompt_audit_template` — Index on `template_id`
- `idx_prompt_audit_action` — Index on `action`
- `idx_prompt_audit_changed_at` — Index on `changed_at DESC`

---

## Table: ai_prompt_execution_log

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | UUID | PK | Primary key |
| template_id | UUID | FK → ai_prompt_templates(id) | Template used |
| version_number | INT | | Version used |
| prompt_text | TEXT | NOT NULL | Original prompt |
| rendered_text | TEXT | | Rendered output |
| variables | JSONB | | Variables provided |
| duration_ms | BIGINT | | Render duration |
| success | BOOLEAN | NOT NULL, DEFAULT TRUE | Execution success |
| error_message | TEXT | | Error details |
| executed_by | UUID | | Executor user ID |
| source | VARCHAR(100) | | Source module |
| correlation_id | VARCHAR(255) | | Request correlation ID |
| executed_at | TIMESTAMPTZ | NOT NULL, DEFAULT NOW() | Execution timestamp |

**Indexes:**
- `idx_prompt_exec_template` — Index on `template_id`
- `idx_prompt_exec_success` — Index on `success`
- `idx_prompt_exec_at` — Index on `executed_at DESC`
- `idx_prompt_exec_correlation` — Index on `correlation_id`

---

## Entity Relationships

```
ai_prompt_categories (1) ────── (N) ai_prompt_templates (1) ──── (N) ai_prompt_variables
                                          │                               │
                                          │ (1)                           │ (N)
                                          │                               │
                                          │ (N)                           │
                                          └────── ai_prompt_versions ──────┘
                                                    │
                                                    │ (optional)
                                                    ▼
                                          ai_prompt_audit
                                          ai_prompt_execution_log
```

---

## Key Design Decisions

1. **UUID Primary Keys** — Consistent with all Phase 3 tables; supports distributed systems
2. **Soft Delete** — `is_deleted` + `deleted_at` pattern for data recovery
3. **Partial Indexes** — All indexes include `WHERE NOT is_deleted` for query efficiency
4. **JSONB Columns** — `previous_value`, `new_value` in audit table for flexible state tracking
5. **TEXT[] in template tags** — PostgreSQL array type for tag-based filtering
6. **TIMESTAMPTZ** — Timezone-aware timestamps for global deployments
7. **Cascade Reference** — Foreign keys from templates to categories, variables/versions to templates
