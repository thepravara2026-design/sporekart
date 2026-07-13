# Decision Engine Database Schema

## Overview

6 tables created by Flyway migration `V22__sprint18_decision.sql`. All tables use UUID primary keys with `is_deleted` soft-delete flag.

## Tables

### ai_decisions

Decision results.

| Column | Type | Description |
|--------|------|-------------|
| id | UUID PK | Primary key |
| request_id | UUID | Original request identifier |
| action | VARCHAR(50) | Decision action (ALLOW, DENY, etc.) |
| status | VARCHAR(50) | Decision status (ALLOWED, DENIED, etc.) |
| confidence | VARCHAR(50) | Confidence level (CERTAIN, HIGH, etc.) |
| summary | TEXT | Decision summary |
| processing_time_ms | BIGINT | Processing time in milliseconds |
| requires_approval | BOOLEAN | Whether approval is required |
| overrideable | BOOLEAN | Whether decision can be overridden |
| timestamp | TIMESTAMP | When decision was made |
| created_at | TIMESTAMP | Record creation timestamp |
| is_deleted | BOOLEAN | Soft delete flag (default FALSE) |

**Indexes:** request_id, action, status, confidence, created_at (5)

### ai_decision_rules

Decision rule definitions.

| Column | Type | Description |
|--------|------|-------------|
| id | UUID PK | Primary key |
| name | VARCHAR(200) | Rule name |
| description | TEXT | Rule description |
| action | VARCHAR(50) | Action taken by this rule |
| priority | INT | Rule priority (default 0) |
| weight | INT | Rule weight for confidence (default 1) |
| conditions | TEXT | Rule conditions (JSON) |
| overrides | TEXT | Rule override config (JSON) |
| is_active | BOOLEAN | Whether rule is active (default TRUE) |
| created_at | TIMESTAMP | Record creation timestamp |
| updated_at | TIMESTAMP | Record last update timestamp |
| is_deleted | BOOLEAN | Soft delete flag (default FALSE) |

**Indexes:** action, created_at (2)

### ai_decision_audit

Immutable audit trail for decisions.

| Column | Type | Description |
|--------|------|-------------|
| id | UUID PK | Primary key |
| request_id | UUID | Original request identifier |
| decision_id | UUID | Decision result identifier |
| action | VARCHAR(50) | Decision action |
| status | VARCHAR(50) | Decision status |
| confidence | VARCHAR(50) | Confidence level |
| reasons | TEXT | Decision reasons (serialized) |
| context | TEXT | Decision context (serialized) |
| user_id | VARCHAR(100) | User who triggered the decision |
| processing_time_ms | BIGINT | Processing time |
| success | BOOLEAN | Whether decision was successful |
| timestamp | TIMESTAMP | When decision was made |
| created_at | TIMESTAMP | Record creation timestamp |
| is_deleted | BOOLEAN | Soft delete flag (default FALSE) |

**Indexes:** request_id, decision_id, user_id, action, status, confidence, created_at (7)

### ai_decision_explanations

Generated decision explanations.

| Column | Type | Description |
|--------|------|-------------|
| id | UUID PK | Primary key |
| decision_id | UUID | Associated decision |
| summary | TEXT | Explanation summary |
| matched_policies | TEXT | Matched policy IDs (serialized) |
| triggered_rules | TEXT | Triggered rule IDs (serialized) |
| confidence | VARCHAR(50) | Confidence level |
| reasoning | TEXT | Reasoning details (serialized) |
| evidence | TEXT | Evidence items (serialized) |
| recommended_action | VARCHAR(200) | Recommended follow-up action |
| audit_metadata | TEXT | Audit metadata (serialized) |
| explanation_text | TEXT | Human-readable explanation |
| is_deleted | BOOLEAN | Soft delete flag (default FALSE) |

**Indexes:** decision_id (1)

### ai_decision_history

Decision lifecycle transitions.

| Column | Type | Description |
|--------|------|-------------|
| id | UUID PK | Primary key |
| decision_id | UUID | Associated decision |
| from_status | VARCHAR(50) | Previous status |
| to_status | VARCHAR(50) | New status |
| triggered_by | VARCHAR(100) | Who triggered the transition |
| reason | TEXT | Reason for transition |
| timestamp | TIMESTAMP | When transition occurred |
| is_deleted | BOOLEAN | Soft delete flag (default FALSE) |

**Indexes:** decision_id (1)

### ai_decision_registry

Module/endpoint registry for decision-aware services.

| Column | Type | Description |
|--------|------|-------------|
| id | UUID PK | Primary key |
| name | VARCHAR(200) | Registry entry name |
| module | VARCHAR(100) | Module identifier |
| endpoint | VARCHAR(500) | Endpoint path |
| is_active | BOOLEAN | Whether entry is active (default TRUE) |
| is_registered | BOOLEAN | Whether registered (default FALSE) |
| config | TEXT | Module config (serialized) |
| registered_at | TIMESTAMP | When registered |
| updated_at | TIMESTAMP | Last update timestamp |
| is_deleted | BOOLEAN | Soft delete flag (default FALSE) |

**Indexes:** module, created_at (2)

## Index Summary

| Table | Index Count | Columns |
|-------|-------------|---------|
| ai_decisions | 5 | request_id, action, status, confidence, created_at |
| ai_decision_rules | 2 | action, created_at |
| ai_decision_audit | 7 | request_id, decision_id, user_id, action, status, confidence, created_at |
| ai_decision_explanations | 1 | decision_id |
| ai_decision_history | 1 | decision_id |
| ai_decision_registry | 2 | module, created_at |
| **Total** | **18** | |

## Flyway SQL

Migration: `V22__sprint18_decision.sql`

```sql
CREATE TABLE IF NOT EXISTS ai_decisions (
    id UUID PRIMARY KEY,
    request_id UUID,
    action VARCHAR(50),
    status VARCHAR(50),
    confidence VARCHAR(50),
    summary TEXT,
    processing_time_ms BIGINT,
    requires_approval BOOLEAN DEFAULT FALSE,
    overrideable BOOLEAN DEFAULT FALSE,
    timestamp TIMESTAMP,
    created_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_decision_rules (
    id UUID PRIMARY KEY,
    name VARCHAR(200),
    description TEXT,
    action VARCHAR(50),
    priority INT DEFAULT 0,
    weight INT DEFAULT 1,
    conditions TEXT,
    overrides TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_decision_audit (
    id UUID PRIMARY KEY,
    request_id UUID,
    decision_id UUID,
    action VARCHAR(50),
    status VARCHAR(50),
    confidence VARCHAR(50),
    reasons TEXT,
    context TEXT,
    user_id VARCHAR(100),
    processing_time_ms BIGINT,
    success BOOLEAN,
    timestamp TIMESTAMP,
    created_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_decision_explanations (
    id UUID PRIMARY KEY,
    decision_id UUID,
    summary TEXT,
    matched_policies TEXT,
    triggered_rules TEXT,
    confidence VARCHAR(50),
    reasoning TEXT,
    evidence TEXT,
    recommended_action VARCHAR(200),
    audit_metadata TEXT,
    explanation_text TEXT,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_decision_history (
    id UUID PRIMARY KEY,
    decision_id UUID,
    from_status VARCHAR(50),
    to_status VARCHAR(50),
    triggered_by VARCHAR(100),
    reason TEXT,
    timestamp TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS ai_decision_registry (
    id UUID PRIMARY KEY,
    name VARCHAR(200),
    module VARCHAR(100),
    endpoint VARCHAR(500),
    is_active BOOLEAN DEFAULT TRUE,
    is_registered BOOLEAN DEFAULT FALSE,
    config TEXT,
    registered_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_decision_request_id ON ai_decisions(request_id);
CREATE INDEX IF NOT EXISTS idx_decision_action ON ai_decisions(action);
CREATE INDEX IF NOT EXISTS idx_decision_status ON ai_decisions(status);
CREATE INDEX IF NOT EXISTS idx_decision_confidence ON ai_decisions(confidence);
CREATE INDEX IF NOT EXISTS idx_decision_created_at ON ai_decisions(created_at);

CREATE INDEX IF NOT EXISTS idx_decision_rules_action ON ai_decision_rules(action);
CREATE INDEX IF NOT EXISTS idx_decision_rules_created_at ON ai_decision_rules(created_at);

CREATE INDEX IF NOT EXISTS idx_decision_audit_request ON ai_decision_audit(request_id);
CREATE INDEX IF NOT EXISTS idx_decision_audit_decision_id ON ai_decision_audit(decision_id);
CREATE INDEX IF NOT EXISTS idx_decision_audit_user ON ai_decision_audit(user_id);
CREATE INDEX IF NOT EXISTS idx_decision_audit_action ON ai_decision_audit(action);
CREATE INDEX IF NOT EXISTS idx_decision_audit_status ON ai_decision_audit(status);
CREATE INDEX IF NOT EXISTS idx_decision_audit_confidence ON ai_decision_audit(confidence);
CREATE INDEX IF NOT EXISTS idx_decision_audit_created_at ON ai_decision_audit(created_at);

CREATE INDEX IF NOT EXISTS idx_decision_explanations_decision ON ai_decision_explanations(decision_id);

CREATE INDEX IF NOT EXISTS idx_decision_history_decision ON ai_decision_history(decision_id);

CREATE INDEX IF NOT EXISTS idx_decision_registry_module ON ai_decision_registry(module);
CREATE INDEX IF NOT EXISTS idx_decision_registry_created_at ON ai_decision_registry(created_at);
```

## Notes

- All tables use `IF NOT EXISTS` for idempotent migrations
- TIMESTAMP type (not TIMESTAMPTZ) for H2 compatibility in tests
- TEXT columns for serialized JSON data (reasons, context, conditions, etc.)
- Soft deletes via `is_deleted` flag — no physical row removal
- UUID generation handled by JPA `@GeneratedValue(strategy = GenerationType.UUID)`
