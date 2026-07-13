# Risk Schema — Flyway V25

## Overview

Migration: `V25__sprint18_risk_assessment_trust_framework.sql`
8 tables, H2-compatible (TIMESTAMP, TEXT for JSON, UUID string generation).

## Tables

### risk_assessments

Risk assessment root records.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | VARCHAR(36) | PK | UUID |
| module | VARCHAR(100) | NOT NULL | Source module name |
| provider_id | VARCHAR(100) | | Provider identifier |
| resource_type | VARCHAR(50) | NOT NULL | Type of resource assessed |
| resource_id | VARCHAR(255) | | Resource identifier |
| user_id | VARCHAR(255) | | Requesting user |
| status | VARCHAR(20) | NOT NULL, DEFAULT 'PENDING' | PENDING, IN_PROGRESS, COMPLETED, FAILED |
| risk_level | VARCHAR(20) | | LOW, MEDIUM, HIGH, CRITICAL |
| risk_score | INTEGER | | Overall risk score (0-100) |
| trust_score | INTEGER | | Overall trust score (0-100) |
| confidence_score | INTEGER | | Overall confidence score (0-100) |
| recommendation_type | VARCHAR(20) | | ALLOW, REVIEW, ESCALATE, BLOCK, FALLBACK |
| context_json | TEXT | | Request context (JSON) |
| started_at | TIMESTAMP | | Assessment start timestamp |
| completed_at | TIMESTAMP | | Assessment completion timestamp |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |
| is_deleted | BOOLEAN | NOT NULL, DEFAULT FALSE | Soft delete flag |

**Indexes:** `idx_ra_status` on (status), `idx_ra_risk_level` on (risk_level), `idx_ra_module` on (module)

### risk_factors

Individual risk factor evaluations.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | VARCHAR(36) | PK | UUID |
| assessment_id | VARCHAR(36) | FK → risk_assessments.id | Parent assessment |
| category | VARCHAR(50) | NOT NULL | PROVIDER, PROMPT, KNOWLEDGE, COMPLIANCE, PERFORMANCE, SECURITY, OPERATIONAL |
| name | VARCHAR(255) | NOT NULL | Factor name |
| score | INTEGER | NOT NULL | Factor score (0-100) |
| weight | DECIMAL(5,4) | NOT NULL | Factor weight |
| evidence | TEXT | | Supporting evidence description |
| metadata_json | TEXT | | Factor metadata (JSON) |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| is_deleted | BOOLEAN | NOT NULL, DEFAULT FALSE | Soft delete flag |

**Indexes:** `idx_rf_assessment` on (assessment_id), `idx_rf_category` on (category)

### risk_rules

Risk rule definitions.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | VARCHAR(36) | PK | UUID |
| name | VARCHAR(255) | NOT NULL | Rule name |
| description | TEXT | | Rule description |
| category | VARCHAR(50) | NOT NULL | Risk category |
| expression | TEXT | NOT NULL | Evaluation expression |
| severity | VARCHAR(20) | NOT NULL, DEFAULT 'MEDIUM' | Default severity |
| threshold | INTEGER | NOT NULL | Score threshold |
| weight | DECIMAL(5,4) | NOT NULL, DEFAULT 1.0 | Default weight |
| status | VARCHAR(20) | NOT NULL, DEFAULT 'ACTIVE' | ACTIVE, INACTIVE, DEPRECATED |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| updated_at | TIMESTAMP | NOT NULL | Last update timestamp |
| is_deleted | BOOLEAN | NOT NULL, DEFAULT FALSE | Soft delete flag |

**Indexes:** `idx_rr_category` on (category), `idx_rr_status` on (status)

### risk_evidence

Evidence collected during risk assessment.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | VARCHAR(36) | PK | UUID |
| assessment_id | VARCHAR(36) | FK → risk_assessments.id | Parent assessment |
| factor_id | VARCHAR(36) | FK → risk_factors.id | Related factor |
| source | VARCHAR(100) | NOT NULL | Evidence source |
| type | VARCHAR(50) | NOT NULL | Evidence type |
| value | TEXT | NOT NULL | Evidence value (JSON) |
| collected_at | TIMESTAMP | NOT NULL | Collection timestamp |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| is_deleted | BOOLEAN | NOT NULL, DEFAULT FALSE | Soft delete flag |

**Indexes:** `idx_rve_assessment` on (assessment_id), `idx_rve_factor` on (factor_id)

### risk_decisions

Decision outputs with recommendations.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | VARCHAR(36) | PK | UUID |
| assessment_id | VARCHAR(36) | FK → risk_assessments.id | Parent assessment |
| recommendation_type | VARCHAR(20) | NOT NULL | ALLOW, REVIEW, ESCALATE, BLOCK, FALLBACK |
| reason | TEXT | | Recommendation reason |
| actions_json | TEXT | | Recommended actions (JSON) |
| fallback_suggestion | TEXT | | Fallback suggestion |
| decided_at | TIMESTAMP | NOT NULL | Decision timestamp |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| is_deleted | BOOLEAN | NOT NULL, DEFAULT FALSE | Soft delete flag |

**Indexes:** `idx_rd_assessment` on (assessment_id), `idx_rd_type` on (recommendation_type)

### risk_trust_scores

Trust assessment results.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | VARCHAR(36) | PK | UUID |
| assessment_id | VARCHAR(36) | FK → risk_assessments.id | Parent assessment |
| overall_score | INTEGER | NOT NULL | Overall trust score (0-100) |
| trust_level | VARCHAR(20) | NOT NULL | LOW, MEDIUM, HIGH, VERY_HIGH |
| factors_json | TEXT | NOT NULL | Per-factor scores and reasons (JSON) |
| evaluated_at | TIMESTAMP | NOT NULL | Evaluation timestamp |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| is_deleted | BOOLEAN | NOT NULL, DEFAULT FALSE | Soft delete flag |

**Indexes:** `idx_rts_assessment` on (assessment_id), `idx_rts_level` on (trust_level)

### risk_confidence_scores

Confidence calculation results.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | VARCHAR(36) | PK | UUID |
| assessment_id | VARCHAR(36) | FK → risk_assessments.id | Parent assessment |
| overall_score | INTEGER | NOT NULL | Overall confidence score (0-100) |
| confidence_level | VARCHAR(20) | NOT NULL | LOW, MEDIUM, HIGH, VERY_HIGH |
| factors_json | TEXT | NOT NULL | Per-factor scores and explanations (JSON) |
| evaluated_at | TIMESTAMP | NOT NULL | Evaluation timestamp |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |
| is_deleted | BOOLEAN | NOT NULL, DEFAULT FALSE | Soft delete flag |

**Indexes:** `idx_rcs_assessment` on (assessment_id), `idx_rcs_level` on (confidence_level)

### risk_audit

Immutable audit trail for risk operations.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | VARCHAR(36) | PK | UUID |
| assessment_id | VARCHAR(36) | FK → risk_assessments.id | Related assessment |
| action | VARCHAR(50) | NOT NULL | Audit action |
| actor_id | VARCHAR(255) | NOT NULL | User or system actor |
| details_json | TEXT | | Audit details (JSON) |
| occurred_at | TIMESTAMP | NOT NULL | Event timestamp |
| created_at | TIMESTAMP | NOT NULL | Creation timestamp |

**Indexes:** `idx_raud_assessment` on (assessment_id), `idx_raud_action` on (action), `idx_raud_occurred` on (occurred_at)

**Note:** This table is append-only. UPDATE and DELETE are prohibited at the database level via trigger.

## Entity Relationships

```
risk_assessments
    │
    ├── risk_factors (FK: assessment_id)
    │       └── risk_evidence (FK: factor_id)
    │
    ├── risk_decisions (FK: assessment_id)
    ├── risk_trust_scores (FK: assessment_id)
    ├── risk_confidence_scores (FK: assessment_id)
    └── risk_audit (FK: assessment_id)

risk_rules (independent reference table)
```

## Index Summary

| Table | Indexes |
|-------|---------|
| risk_assessments | 3 |
| risk_factors | 2 |
| risk_rules | 2 |
| risk_evidence | 2 |
| risk_decisions | 2 |
| risk_trust_scores | 2 |
| risk_confidence_scores | 2 |
| risk_audit | 3 |
| **Total** | **18** |
