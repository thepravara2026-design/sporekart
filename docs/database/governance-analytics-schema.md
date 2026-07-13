# Governance Analytics Schema — Flyway V26

## Overview

Migration: `V26__sprint18_governance_analytics.sql`
7 tables, H2-compatible (TIMESTAMP, TEXT for JSON, UUID string generation).

## Tables

### governance_metrics

Collected metric data points from governance operations.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | VARCHAR(36) | PK | UUID |
| metric_type | VARCHAR(50) | NOT NULL | GOVERNANCE, POLICY, DECISION, APPROVAL, COMPLIANCE, RISK, TRUST, PERFORMANCE, UTILIZATION, AUDIT |
| name | VARCHAR(255) | NOT NULL | Metric name (e.g., policy.evaluation.count) |
| value | DOUBLE | NOT NULL | Metric value |
| unit | VARCHAR(50) | | Measurement unit |
| source | VARCHAR(100) | | Source module or service |
| dimensions_json | TEXT | | Metric dimensions (JSON) |
| recorded_at | TIMESTAMP | NOT NULL | When metric was recorded |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| is_deleted | BOOLEAN | NOT NULL, DEFAULT FALSE | Soft delete flag |

**Indexes:** `idx_gm_type_recorded` on (metric_type, recorded_at), `idx_gm_name_recorded` on (name, recorded_at)

### governance_dashboards

Dashboard definitions with widget configurations.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | VARCHAR(36) | PK | UUID |
| name | VARCHAR(255) | NOT NULL | Dashboard name |
| description | TEXT | | Dashboard description |
| owner_id | VARCHAR(255) | NOT NULL | Dashboard owner |
| widgets_json | TEXT | NOT NULL | Widget configurations (JSON) |
| layout_json | TEXT | | Dashboard layout (JSON) |
| is_default | BOOLEAN | NOT NULL, DEFAULT FALSE | Default dashboard flag |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |
| is_deleted | BOOLEAN | NOT NULL, DEFAULT FALSE | Soft delete flag |

**Indexes:** `idx_gd_owner` on (owner_id)

### governance_reports

Generated report records with metadata and storage reference.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | VARCHAR(36) | PK | UUID |
| report_type | VARCHAR(50) | NOT NULL | EXECUTIVE_SUMMARY, GOVERNANCE_HEALTH, POLICY, DECISION, APPROVAL, COMPLIANCE, RISK, TRUST, OPERATIONAL, AUDIT_SUMMARY, DAILY, WEEKLY, MONTHLY, CUSTOM |
| title | VARCHAR(255) | NOT NULL | Report title |
| format | VARCHAR(20) | NOT NULL | JSON, CSV, EXCEL, PDF |
| status | VARCHAR(20) | NOT NULL, DEFAULT 'PENDING' | PENDING, GENERATING, COMPLETED, FAILED |
| data_json | TEXT | | Report data (JSON) |
| file_reference | VARCHAR(500) | | Storage file reference |
| file_size | BIGINT | | File size in bytes |
| period_from | TIMESTAMP | | Report period start |
| period_to | TIMESTAMP | | Report period end |
| generated_by | VARCHAR(255) | | User or schedule that triggered generation |
| version | INTEGER | NOT NULL, DEFAULT 1 | Report template version |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |
| is_deleted | BOOLEAN | NOT NULL, DEFAULT FALSE | Soft delete flag |

**Indexes:** `idx_gr_type_status` on (report_type, status), `idx_gr_generated` on (created_at)

### governance_kpis

KPI definitions, targets, thresholds, and current values.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | VARCHAR(36) | PK | UUID |
| name | VARCHAR(255) | NOT NULL, UNIQUE | KPI name |
| description | TEXT | | KPI description |
| unit | VARCHAR(50) | | Measurement unit |
| target_value | DOUBLE | NOT NULL | Target value |
| warning_threshold | DOUBLE | | Warning threshold (percentage of target) |
| critical_threshold | DOUBLE | | Critical threshold (percentage of target) |
| current_value | DOUBLE | | Current calculated value |
| status | VARCHAR(20) | | ON_TRACK, AT_RISK, BELOW_TARGET, CRITICAL, NOT_APPLICABLE |
| trend | VARCHAR(20) | | UP, DOWN, STABLE, VOLATILE |
| calculation_expression | TEXT | | KPI calculation logic |
| last_calculated | TIMESTAMP | | Last calculation timestamp |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |
| is_deleted | BOOLEAN | NOT NULL, DEFAULT FALSE | Soft delete flag |

**Indexes:** `idx_gk_name` on (name), `idx_gk_status` on (status)

### governance_snapshots

Point-in-time governance state records.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | VARCHAR(36) | PK | UUID |
| snapshot_type | VARCHAR(50) | NOT NULL | Type of snapshot |
| data_json | TEXT | NOT NULL | Snapshot data (JSON) |
| description | TEXT | | Snapshot description |
| captured_at | TIMESTAMP | NOT NULL | Snapshot capture time |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| is_deleted | BOOLEAN | NOT NULL, DEFAULT FALSE | Soft delete flag |

**Indexes:** `idx_gs_type_captured` on (snapshot_type, captured_at)

### governance_exports

Export job records with format, status, and file path.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | VARCHAR(36) | PK | UUID |
| report_id | VARCHAR(36) | FK → governance_reports.id | Source report |
| format | VARCHAR(20) | NOT NULL | JSON, CSV, EXCEL, PDF |
| status | VARCHAR(20) | NOT NULL, DEFAULT 'PENDING' | PENDING, PROCESSING, COMPLETED, FAILED |
| file_reference | VARCHAR(500) | | Export file path |
| file_size | BIGINT | | Export file size |
| options_json | TEXT | | Export options (JSON) |
| exported_at | TIMESTAMP | | Export completion time |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |
| is_deleted | BOOLEAN | NOT NULL, DEFAULT FALSE | Soft delete flag |

**Indexes:** `idx_ge_status` on (status), `idx_ge_report` on (report_id)

### governance_report_schedules

Scheduled report definitions with frequency and recipients.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | VARCHAR(36) | PK | UUID |
| report_type | VARCHAR(50) | NOT NULL | Report type to generate |
| frequency | VARCHAR(20) | NOT NULL | DAILY, WEEKLY, MONTHLY, QUARTERLY, YEARLY, CUSTOM |
| cron_expression | VARCHAR(100) | | Cron expression for CUSTOM frequency |
| format | VARCHAR(20) | NOT NULL, DEFAULT 'JSON' | Output format |
| recipients_json | TEXT | | Recipient list (JSON) |
| filters_json | TEXT | | Pre-configured filters (JSON) |
| is_active | BOOLEAN | NOT NULL, DEFAULT TRUE | Schedule active flag |
| last_run_at | TIMESTAMP | | Last successful generation |
| next_run_at | TIMESTAMP | | Next scheduled generation |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |
| is_deleted | BOOLEAN | NOT NULL, DEFAULT FALSE | Soft delete flag |

**Indexes:** `idx_grs_active_frequency` on (is_active, frequency)

## Entity Relationships

```
governance_metrics (independent metric data)
governance_dashboards (independent dashboard definitions)
governance_kpis (independent KPI definitions)

governance_reports
    └── governance_exports (FK: report_id)
    └── governance_report_schedules (generates reports)

governance_snapshots (independent point-in-time records)
```

## Index Summary

| Table | Indexes |
|-------|---------|
| governance_metrics | 2 |
| governance_dashboards | 1 |
| governance_reports | 2 |
| governance_kpis | 2 |
| governance_snapshots | 1 |
| governance_exports | 1 |
| governance_report_schedules | 1 |
| **Total** | **9** |
