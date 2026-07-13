# Governance Domain Model

## Overview

The governance domain model consists of 5 enums and 11 Java records organized around the Governance Policy aggregate root. The domain follows DDD principles with clear aggregate boundaries, value objects, and domain events.

## Enums

### PolicyType

Defines the category of governance policy.

| Value | Description |
|-------|-------------|
| `RATE_LIMIT` | Controls request rate thresholds per module/user |
| `USAGE_QUOTA` | Defines usage consumption limits over time periods |
| `CONTENT_FILTER` | Content filtering and moderation rules |
| `ACCESS_CONTROL` | Access permission rules for AI operations |
| `AUDIT_CONFIG` | Audit logging behavior configuration |
| `COMPLIANCE_RULE` | Compliance requirements and standards |
| `MODEL_GOVERNANCE` | AI model usage and deployment governance |
| `DATA_RETENTION` | Data retention and purge policies |

### PolicySeverity

| Value | Description |
|-------|-------------|
| `INFO` | Informational policy, no enforcement |
| `WARNING` | Warning on violation, request proceeds |
| `CRITICAL` | Critical violation, request proceeds with alert |
| `BLOCKING` | Request is rejected on violation |

### PolicyStatus

| Value | Description |
|-------|-------------|
| `DRAFT` | Initial creation, not yet active |
| `ACTIVE` | Policy is actively enforced |
| `INACTIVE` | Policy exists but is not enforced |
| `ARCHIVED` | Policy is retired and archived |

### ConfigScope

| Value | Description |
|-------|-------------|
| `GLOBAL` | Applies to entire AI platform |
| `MODULE` | Scoped to a specific AI module |
| `PROVIDER` | Scoped to a specific AI provider |
| `MODEL` | Scoped to a specific AI model |
| `TENANT` | Scoped to a tenant organization |
| `USER` | Scoped to a specific user |

### AuditEventType

| Value | Description |
|-------|-------------|
| `CONFIG_UPDATED` | Configuration entry was created or modified |
| `POLICY_CREATED` | A new governance policy was created |
| `POLICY_ACTIVATED` | A policy was activated |
| `POLICY_DEACTIVATED` | A policy was deactivated |
| `PERMISSION_GRANTED` | A permission was granted to a role |
| `PERMISSION_REVOKED` | A permission was revoked from a role |
| `RATE_LIMIT_EXCEEDED` | A rate limit threshold was exceeded |
| `QUOTA_EXCEEDED` | A usage quota limit was exceeded |
| `COMPLIANCE_VIOLATION` | A compliance rule was violated |
| `ROLE_ASSIGNED` | A role was assigned to a user |
| `ROLE_REVOKED` | A role was revoked from a user |
| `ACCESS_DENIED` | An access attempt was denied |
| `CONFIG_EXPORTED` | Configuration was exported |
| `CONFIG_IMPORTED` | Configuration was imported |

## Records (Domain Models)

### GovernancePolicy (Aggregate Root)

The central aggregate root for the governance domain. Contains all policy definition attributes and maintains versioning for optimistic locking.

```java
public record GovernancePolicy(
    UUID id,
    String name,
    PolicyType type,
    PolicySeverity severity,
    PolicyStatus status,
    Map<String, Object> rules,
    ConfigScope scope,
    String moduleTarget,
    Instant createdAt,
    Instant updatedAt,
    int version
) {}
```

**Relationships:**
- One `GovernancePolicy` has many `ComplianceReport` results
- One `GovernancePolicy` has many `PolicyViolation` records
- Policy rules reference `ConfigScope` and `PolicyType` enums

**Invariants:**
- Policy name must be unique within a scope
- Status transitions: DRAFT → ACTIVE → INACTIVE → ARCHIVED (no backwards transitions)
- BLOCKING severity policies cannot transition to DRAFT after activation
- `moduleTarget` is required when `scope` is MODULE, PROVIDER, or MODEL

### ConfigEntry

Represents a single configuration key-value pair with metadata.

```java
public record ConfigEntry(
    UUID id,
    ConfigScope scope,
    String module,
    String key,
    String value,
    String dataType,
    String description,
    Set<String> tags,
    boolean encrypted,
    int version
) {}
```

**Relationships:**
- One `ConfigEntry` has many `ChangeLog` entries tracking its modification history

**Invariants:**
- Key must be unique within scope + module combination
- `encrypted` flag requires value to be stored in encrypted form
- `dataType` validation on write (STRING, INTEGER, BOOLEAN, JSON, YAML)

### AuditRecord

Immutable record representing a single audit event. Once created, audit records must never be updated or deleted.

```java
public record AuditRecord(
    UUID id,
    AuditEventType eventType,
    String actorId,
    String actorRole,
    String resource,
    String action,
    String outcome,
    String details,
    Instant timestamp,
    String correlationId,
    String sourceIp,
    String userAgent
) {}
```

**Relationships:**
- References `AuditEventType` enum
- `correlationId` links to request tracing across the platform

**Invariants:**
- Append-only: records cannot be modified or deleted
- `timestamp` is set at creation time and never changed
- `outcome` must be one of: SUCCESS, FAILURE, DENIED

### PermissionAssignment

Maps roles to permissions for RBAC enforcement.

```java
public record PermissionAssignment(
    UUID id,
    String role,
    String permission,
    String resource,
    ConfigScope scope,
    String effect
) {}
```

**Relationships:**
- References `ConfigScope` enum
- Multiple assignments compose to form a role's complete permission set

**Invariants:**
- `effect` must be ALLOW or DENY
- DENY takes precedence over ALLOW in hierarchical resolution

### UsageQuota

Tracks resource consumption against defined limits.

```java
public record UsageQuota(
    UUID id,
    String module,
    String userId,
    String operation,
    Instant periodStart,
    Instant periodEnd,
    long count,
    long limit
) {}
```

**Relationships:**
- Quota periods align with calendar boundaries
- Multiple quota records track different operations per user per period

**Invariants:**
- `count` must never exceed `limit` when enforced
- Period boundaries are immutable after creation

### RateLimitRule

Defines rate limiting parameters for a specific module/user/operation.

```java
public record RateLimitRule(
    UUID id,
    String module,
    String userId,
    String operation,
    int maxRequests,
    int windowSeconds,
    int burstSize
) {}
```

**Invariants:**
- `maxRequests` must be greater than 0
- `windowSeconds` must be between 1 and 86400
- `burstSize` must be >= `maxRequests`

### ComplianceReport

Captures the result of a compliance check against a policy.

```java
public record ComplianceReport(
    UUID id,
    UUID policyId,
    String status,
    List<PolicyViolation> violations,
    Instant checkedAt,
    String module,
    String details
) {}
```

**Relationships:**
- References one `GovernancePolicy` by `policyId`
- Contains a list of `PolicyViolation` records

**Invariants:**
- `status` must be COMPLIANT, NON_COMPLIANT, or ERROR

### PolicyViolation

Records a single policy violation instance.

```java
public record PolicyViolation(
    UUID id,
    UUID policyId,
    String rule,
    PolicySeverity severity,
    String message,
    Instant timestamp,
    String resource,
    String actor
) {}
```

**Relationships:**
- References one `GovernancePolicy` by `policyId`
- References `PolicySeverity` enum

### RoleDefinition

Defines an RBAC role with hierarchical permissions.

```java
public record RoleDefinition(
    UUID id,
    String name,
    String description,
    Set<String> permissions,
    int hierarchyLevel
) {}
```

**Invariants:**
- Role name must be unique
- `hierarchyLevel` determines inheritance (higher = more privileged)
- Lower hierarchy levels inherit permissions from higher levels

### ChangeLog

Tracks configuration change history for auditability.

```java
public record ChangeLog(
    UUID id,
    UUID configId,
    String previousValue,
    String newValue,
    String changedBy,
    Instant changedAt,
    String reason
) {}
```

**Relationships:**
- References one `ConfigEntry` by `configId`

### GovernanceDashboard

Provides a metrics snapshot for the governance dashboard view.

```java
public record GovernanceDashboard(
    UUID id,
    int activePolicies,
    long totalViolations,
    int pendingReviews,
    Instant lastUpdated
) {}
```

## Aggregate Design

### GovernancePolicy Aggregate

The `GovernancePolicy` record is the aggregate root. It owns:
- Policy metadata (name, type, severity, status)
- Policy rules (Map<String, Object> for flexible rule definitions)
- Policy scope and targeting

**Aggregate boundary:** The policy aggregate includes the policy itself and its compliance reports. Policy violations are value objects within compliance reports.

**Consistency:** Policy status transitions are atomic operations within the aggregate boundary. All changes to a policy go through the `PolicyManager` application service.

### ConfigEntry Aggregate

The `ConfigEntry` record manages configuration values. It owns its change history through the `ChangeLog` value object.

**Aggregate boundary:** Config entry and its change logs are within the same aggregate.

**Consistency:** Configuration writes are synchronized through optimistic locking using the `version` field.

### AuditRecord (Standalone)

`AuditRecord` is not an aggregate root — it is an append-only event record. There are no consistency guarantees beyond atomic append. This design choice acknowledges that audit records are immutable observations that cannot participate in transactional guarantees with other aggregates.

## Domain Event Types

Governance domain events are published as Kafka events:

| Event | Source | Trigger |
|-------|--------|---------|
| `PolicyCreated` | PolicyManager | New policy persisted |
| `PolicyUpdated` | PolicyManager | Existing policy modified |
| `PolicyActivated` | PolicyManager | Policy status → ACTIVE |
| `PolicyDeactivated` | PolicyManager | Policy status → INACTIVE |
| `PolicyArchived` | PolicyManager | Policy status → ARCHIVED |
| `ConfigCreated` | ConfigurationService | New config entry created |
| `ConfigUpdated` | ConfigurationService | Config entry modified |
| `ConfigDeleted` | ConfigurationService | Config entry removed |
| `ConfigExported` | ConfigImportExportService | Configuration exported |
| `ConfigImported` | ConfigImportExportService | Configuration imported |
| `AuditRecordCreated` | AuditService | New audit record appended |
| `ComplianceViolationDetected` | ComplianceChecker | Policy violation detected |

## Relationship Diagram

```
┌──────────────────┐       ┌──────────────────────┐
│  PolicyType       │       │  GovernancePolicy     │
│  (enum)           │◄──────│  (Aggregate Root)     │
└──────────────────┘       └──────────┬───────────┘
                                      │
                          ┌───────────┴───────────┐
                          │                       │
                 ┌────────▼──────┐       ┌───────▼─────────┐
                 │ Compliance    │       │ PolicyViolation  │
                 │ Report        │       │ (Value Object)   │
                 └───────────────┘       └─────────────────┘

┌──────────────────┐       ┌──────────────────────┐
│  ConfigScope      │       │  ConfigEntry          │
│  (enum)           │◄──────│  (Aggregate Root)     │
└──────────────────┘       └──────────┬───────────┘
                                      │
                          ┌───────────┴───────────┐
                          │  ChangeLog             │
                          │  (Value Object)        │
                          └───────────────────────┘

┌──────────────────┐       ┌──────────────────────┐
│  AuditEventType   │       │  AuditRecord          │
│  (enum)           │◄──────│  (Immutable Event)    │
└──────────────────┘       └──────────────────────┘

┌──────────────────┐       ┌──────────────────────────┐
│  ConfigScope      │       │  PermissionAssignment     │
│  (enum)           │◄──────│  (RBAC Mapping)           │
└──────────────────┘       └──────────────────────────┘

┌──────────────────┐       ┌──────────────────────┐
│  RoleDefinition   │       │  UsageQuota           │
│  (Standalone)     │       │  (Aggregate Root)     │
└──────────────────┘       └──────────────────────┘

┌──────────────────┐       ┌──────────────────────┐
│  RateLimitRule    │       │  GovernanceDashboard  │
│  (Standalone)     │       │  (Snapshot)           │
└──────────────────┘       └──────────────────────┘
```
