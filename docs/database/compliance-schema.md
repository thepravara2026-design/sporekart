# Compliance Schema — Flyway V24

## Overview

Migration: `V24__sprint18_compliance_regulatory_framework.sql`
8 tables, H2-compatible (TIMESTAMP, TEXT for JSON, UUID string generation).

## Tables

### compliance_frameworks

Registered compliance frameworks.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | VARCHAR(36) | PK | UUID |
| framework_type | VARCHAR(50) | NOT NULL, UNIQUE | Framework type enum |
| name | VARCHAR(255) | NOT NULL | Display name |
| version | VARCHAR(50) | NOT NULL | Framework version |
| description | TEXT | | Description |
| status | VARCHAR(20) | NOT NULL, DEFAULT 'ACTIVE' | ACTIVE, INACTIVE |
| config_json | TEXT | | Framework configuration (JSON) |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |
| is_deleted | BOOLEAN | NOT NULL, DEFAULT FALSE | Soft delete flag |

**Indexes:** `idx_cf_framework_type` on (framework_type), `idx_cf_status` on (status)

### compliance_rules

Compliance rules belonging to frameworks.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | VARCHAR(36) | PK | UUID |
| framework_id | VARCHAR(36) | FK → compliance_frameworks.id | Parent framework |
| name | VARCHAR(255) | NOT NULL | Rule name |
| description | TEXT | | Rule description |
| scope | VARCHAR(50) | NOT NULL | Compliance scope |
| severity | VARCHAR(20) | NOT NULL, DEFAULT 'ERROR' | Violation severity |
| expression | TEXT | NOT NULL | Evaluation expression |
| control_type | VARCHAR(20) | NOT NULL | PREVENTIVE, DETECTIVE, CORRECTIVE, DIRECTIVE |
| priority | INTEGER | NOT NULL, DEFAULT 0 | Evaluation priority |
| status | VARCHAR(20) | NOT NULL, DEFAULT 'ACTIVE' | ACTIVE, INACTIVE, DEPRECATED |
| config_json | TEXT | | Rule configuration (JSON) |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |
| is_deleted | BOOLEAN | NOT NULL, DEFAULT FALSE | Soft delete flag |

**Indexes:** `idx_cr_framework` on (framework_id), `idx_cr_scope` on (scope), `idx_cr_status` on (status), `idx_cr_severity` on (severity)

### compliance_controls

Compliance controls mapped to rules.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | VARCHAR(36) | PK | UUID |
| rule_id | VARCHAR(36) | FK → compliance_rules.id | Parent rule |
| control_type | VARCHAR(20) | NOT NULL | Control type |
| name | VARCHAR(255) | NOT NULL | Control name |
| description | TEXT | | Control description |
| implementation | TEXT | | Implementation guidance |
| evidence_required | BOOLEAN | NOT NULL, DEFAULT TRUE | Evidence collection required |
| frequency | VARCHAR(50) | | Validation frequency |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |
| is_deleted | BOOLEAN | NOT NULL, DEFAULT FALSE | Soft delete flag |

**Indexes:** `idx_cc_rule` on (rule_id), `idx_cc_type` on (control_type)

### compliance_assessments

Compliance assessment records.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | VARCHAR(36) | PK | UUID |
| framework_id | VARCHAR(36) | FK → compliance_frameworks.id | Framework assessed |
| request_ref | VARCHAR(255) | | External request reference |
| resource_type | VARCHAR(50) | NOT NULL | Type of resource assessed |
| resource_id | VARCHAR(255) | | Resource identifier |
| user_id | VARCHAR(255) | | Requesting user |
| module | VARCHAR(100) | | Module name |
| scope | VARCHAR(50) | NOT NULL | Assessment scope |
| risk_level | VARCHAR(20) | NOT NULL | Risk classification |
| status | VARCHAR(20) | NOT NULL, DEFAULT 'DRAFT' | Assessment status |
| result_json | TEXT | | Assessment result (JSON) |
| started_at | TIMESTAMP | | Start timestamp |
| completed_at | TIMESTAMP | | Completion timestamp |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |
| is_deleted | BOOLEAN | NOT NULL, DEFAULT FALSE | Soft delete flag |

**Indexes:** `idx_ca_framework` on (framework_id), `idx_ca_status` on (status), `idx_ca_scope` on (scope), `idx_ca_resource` on (resource_type, resource_id)

### compliance_evidence

Evidence collected during compliance assessment.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | VARCHAR(36) | PK | UUID |
| assessment_id | VARCHAR(36) | FK → compliance_assessments.id | Parent assessment |
| rule_id | VARCHAR(36) | FK → compliance_rules.id | Rule being evidenced |
| source | VARCHAR(100) | NOT NULL | Evidence source |
| type | VARCHAR(50) | NOT NULL | Evidence type |
| value | TEXT | NOT NULL | Evidence value (JSON) |
| hash | VARCHAR(64) | NOT NULL | SHA-256 hash for integrity |
| verified | BOOLEAN | NOT NULL, DEFAULT FALSE | Integrity verification flag |
| collected_at | TIMESTAMP | NOT NULL | Collection timestamp |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| is_deleted | BOOLEAN | NOT NULL, DEFAULT FALSE | Soft delete flag |

**Indexes:** `idx_ce_assessment` on (assessment_id), `idx_ce_rule` on (rule_id), `idx_ce_hash` on (hash)

### compliance_violations

Compliance violations detected during assessment.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | VARCHAR(36) | PK | UUID |
| assessment_id | VARCHAR(36) | FK → compliance_assessments.id | Parent assessment |
| rule_id | VARCHAR(36) | FK → compliance_rules.id | Violated rule |
| evidence_id | VARCHAR(36) | FK → compliance_evidence.id | Related evidence |
| exception_id | VARCHAR(36) | FK → compliance_exceptions.id | Granted exception |
| severity | VARCHAR(20) | NOT NULL | Violation severity |
| message | TEXT | NOT NULL | Violation description |
| resolved | BOOLEAN | NOT NULL, DEFAULT FALSE | Resolution status |
| detected_at | TIMESTAMP | NOT NULL | Detection timestamp |
| resolved_at | TIMESTAMP | | Resolution timestamp |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |
| is_deleted | BOOLEAN | NOT NULL, DEFAULT FALSE | Soft delete flag |

**Indexes:** `idx_cv_assessment` on (assessment_id), `idx_cv_severity` on (severity), `idx_cv_resolved` on (resolved), `idx_cv_exception` on (exception_id)

### compliance_reports

Generated compliance reports.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | VARCHAR(36) | PK | UUID |
| assessment_id | VARCHAR(36) | FK → compliance_assessments.id | Parent assessment |
| framework_id | VARCHAR(36) | FK → compliance_frameworks.id | Framework |
| overall_status | VARCHAR(20) | NOT NULL | COMPLIANT, NON_COMPLIANT |
| total_rules | INTEGER | NOT NULL | Rules evaluated |
| passed | INTEGER | NOT NULL | Rules passed |
| violations | INTEGER | NOT NULL | Violations count |
| report_json | TEXT | | Full report (JSON) |
| generated_at | TIMESTAMP | NOT NULL | Generation timestamp |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| is_deleted | BOOLEAN | NOT NULL, DEFAULT FALSE | Soft delete flag |

**Indexes:** `idx_crpt_assessment` on (assessment_id), `idx_crpt_framework` on (framework_id), `idx_crpt_status` on (overall_status)

### compliance_audit

Immutable audit trail for compliance operations.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | VARCHAR(36) | PK | UUID |
| assessment_id | VARCHAR(36) | FK → compliance_assessments.id | Related assessment |
| action | VARCHAR(50) | NOT NULL | Audit action |
| actor_id | VARCHAR(255) | NOT NULL | User or system actor |
| details_json | TEXT | | Audit details (JSON) |
| occurred_at | TIMESTAMP | NOT NULL | Event timestamp |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |

**Indexes:** `idx_ca_audit_assessment` on (assessment_id), `idx_ca_audit_action` on (action), `idx_ca_audit_actor` on (actor_id), `idx_ca_audit_occurred` on (occurred_at)

**Note:** This table is append-only. UPDATE and DELETE are prohibited at the database level via trigger.

## Entity Relationships

```
compliance_frameworks
    │
    ├── compliance_rules (FK: framework_id)
    │       │
    │       ├── compliance_controls (FK: rule_id)
    │       ├── compliance_evidence (FK: rule_id)
    │       └── compliance_violations (FK: rule_id)
    │
    └── compliance_assessments (FK: framework_id)
            │
            ├── compliance_evidence (FK: assessment_id)
            ├── compliance_violations (FK: assessment_id)
            │       └── compliance_exceptions (FK: exception_id)
            ├── compliance_reports (FK: assessment_id)
            └── compliance_audit (FK: assessment_id)
```

## Index Summary

| Table | Indexes |
|-------|---------|
| compliance_frameworks | 2 |
| compliance_rules | 4 |
| compliance_controls | 2 |
| compliance_assessments | 4 |
| compliance_evidence | 3 |
| compliance_violations | 4 |
| compliance_reports | 3 |
| compliance_audit | 4 |
| **Total** | **26** |
