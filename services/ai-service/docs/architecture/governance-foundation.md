# Governance Platform Architecture

## Overview

The Enterprise AI Governance Platform provides centralized policy management, configuration management, RBAC, audit logging, usage quotas, and compliance checking for all AI services. It follows the same DDD/Hexagonal architecture pattern established by other AI platform modules (Prompt, Knowledge, Semantic, Conversation, Content, Assistant).

## Pipeline Flow

### Policy Enforcement Pipeline

```
Client Request
    │
    ▼
┌─────────────────────────────────────────────────────┐
│                 API Gateway                          │
│  (Rate Limiting, Authentication, Request Validation) │
└─────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────┐
│           Governance Security Filter                  │
│  ┌─────────────┐  ┌──────────┐  ┌───────────────┐   │
│  │ Access       │  │ Policy   │  │ Quota         │   │
│  │ Control      │  │ Enforcer │  │ Checker       │   │
│  │ (RBAC)       │  │          │  │               │   │
│  └─────────────┘  └──────────┘  └───────────────┘   │
└─────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────┐
│              AI Module Processing                     │
│  (Prompt, Knowledge, Semantic, Conversation, etc.)    │
└─────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────┐
│           Governance Audit Recorder                   │
│  ┌─────────────┐  ┌──────────────┐  ┌────────────┐  │
│  │ Event       │  │ Compliance   │  │ Kafka       │  │
│  │ Logger      │  │ Checker      │  │ Publisher   │  │
│  └─────────────┘  └──────────────┘  └────────────┘  │
└─────────────────────────────────────────────────────┘
    │
    ▼
┌─────────────────────────────────────────────────────┐
│              Response (with audit metadata)           │
└─────────────────────────────────────────────────────┘
```

### Configuration Management Flow

```
External Config Source (YAML/JSON)
    │
    ▼
┌──────────────────────┐     ┌──────────────────────┐
│  Config Import       │────▶│  Validation &        │
│  Service             │     │  Scope Enforcement   │
└──────────────────────┘     └──────────────────────┘
                                      │
                                      ▼
                    ┌──────────────────────────────────┐
                    │         Config Repository         │
                    │  (DB + Redis Cache)               │
                    └──────────────────────────────────┘
                                      │
                                      ▼
                    ┌──────────────────────────────────┐
                    │  Change Log Recording             │
                    │  + Kafka Event Publish            │
                    └──────────────────────────────────┘
```

## Module Structure

```
com.sporekart.ai.governance
├── domain/
│   ├── GovernancePolicy.java
│   ├── ConfigEntry.java
│   ├── AuditRecord.java
│   ├── PermissionAssignment.java
│   ├── UsageQuota.java
│   ├── RateLimitRule.java
│   ├── ComplianceReport.java
│   ├── PolicyViolation.java
│   ├── RoleDefinition.java
│   ├── ChangeLog.java
│   ├── GovernanceDashboard.java
│   ├── PolicyType.java
│   ├── PolicySeverity.java
│   ├── PolicyStatus.java
│   ├── ConfigScope.java
│   └── AuditEventType.java
├── api/
│   ├── PolicyManager.java
│   ├── ConfigurationService.java
│   ├── AuditService.java
│   ├── AccessControlService.java
│   ├── QuotaManager.java
│   └── ComplianceChecker.java
├── application/
│   ├── PolicyManagerImpl.java
│   ├── ConfigurationServiceImpl.java
│   ├── AuditServiceImpl.java
│   ├── AccessControlServiceImpl.java
│   ├── QuotaManagerImpl.java
│   ├── ComplianceCheckerImpl.java
│   ├── ConfigImportExportService.java
│   └── GovernanceMonitoringService.java
├── infrastructure/
│   ├── persistence/
│   │   ├── GovernancePolicyEntity.java
│   │   ├── ConfigEntryEntity.java
│   │   ├── AuditRecordEntity.java
│   │   ├── PermissionAssignmentEntity.java
│   │   ├── UsageQuotaEntity.java
│   │   ├── ComplianceReportEntity.java
│   │   ├── GovernancePolicyRepository.java
│   │   ├── ConfigEntryRepository.java
│   │   ├── AuditRecordRepository.java
│   │   ├── PermissionAssignmentRepository.java
│   │   ├── UsageQuotaRepository.java
│   │   └── ComplianceReportRepository.java
│   ├── GovernanceRedisCacheService.java
│   ├── GovernanceKafkaEventPublisher.java
│   └── GovernanceSecurityManager.java
├── interfaces/
│   └── rest/
│       ├── GovernanceController.java
│       └── dto/
│           ├── PolicyRequest.java
│           ├── PolicyResponse.java
│           ├── ConfigEntryRequest.java
│           ├── ConfigEntryResponse.java
│           ├── AuditQueryRequest.java
│           ├── AuditRecordResponse.java
│           ├── ComplianceReportResponse.java
│           ├── QuotaResponse.java
│           ├── RoleDefinitionRequest.java
│           ├── RoleDefinitionResponse.java
│           └── GovernanceErrorResponse.java
└── config/
    └── GovernanceConfig.java
```

## Hexagonal Architecture Layers

### Domain Layer (Inner Hexagon)

The domain layer contains pure Java records and enums with no framework dependencies. Records serve as domain models and aggregate roots. Enums define closed sets of values for policy types, severities, statuses, config scopes, and audit event types. Domain objects have no JPA annotations, no Spring annotations, and no infrastructure imports.

### API Layer (Ports)

Defines 6 port interfaces that serve as contracts between the application layer and the outside world. Inbound ports (`PolicyManager`, `ConfigurationService`, `AuditService`) are implemented by application services and consumed by the REST controller. Outbound ports (`QuotaManager`, `ComplianceChecker`, `AccessControlService`) are implemented by application services and consumed by infrastructure adapters.

### Application Layer (Use Cases)

Contains 8 application service classes that implement the port interfaces. Services orchestrate domain logic, validate inputs, enforce business rules, delegate to repositories, publish events, and coordinate cross-cutting concerns. No framework dependencies in pure business logic; Spring `@Service` and `@Transactional` annotations only for dependency injection and transaction management.

### Infrastructure Layer (Adapters)

**Persistence:** 6 JPA entity classes map domain records to database tables. 6 Spring Data JPA repository interfaces provide CRUD operations, custom queries, and pagination support.

**Cache:** `GovernanceRedisCacheService` manages 5 Redis cache namespaces with configurable TTLs. Cache-aside pattern: read-through on miss, write-through on update, invalidation on delete.

**Events:** `GovernanceKafkaEventPublisher` publishes 12 event types to the `governance-events` topic. Events carry correlation IDs for traceability across the platform.

**Security:** `GovernanceSecurityManager` integrates with Spring Security to enforce RBAC, validate request authorization, apply rate limit policies, and record audit events.

### Interfaces Layer (REST)

Single `GovernanceController` exposes 16 REST endpoints under `/api/v1/governance/*`. Request/response DTOs are defined in the `dto/` sub-package. Error responses follow RFC 9457 Problem Details format.

### Config Layer

`GovernanceConfig` is a Spring `@ConfigurationProperties` class that externalizes governance configuration: cache TTLs, default policies, quota periods, audit retention, encryption keys reference, and rate limit defaults.

## Integration Points

### Integration with AI Gateway

The governance module integrates with the AI Gateway at the security filter level. Before an AI request reaches its target module, the gateway invokes the `GovernanceSecurityManager` to:
1. Authenticate the request (JWT validation)
2. Authorize the operation (RBAC check via `AccessControlService`)
3. Enforce rate limits (via `QuotaManager`)
4. Apply blocking policies (via `PolicyManager`)

### Integration with All AI Modules

Each AI module (Prompt, Knowledge, Semantic, Conversation, Content, Assistant, Provider) integrates with governance for:
- **Configuration:** Modules read runtime configuration from `ConfigurationService` instead of local properties
- **Audit:** Modules call `AuditService.recordEvent()` for auditable operations
- **Compliance:** Modules participate in compliance checks by invoking `ComplianceChecker`
- **Quota:** Modules report usage via `QuotaManager.incrementUsage()`

### Integration with Kafka

The governance module both produces and consumes Kafka events:
- **Produces:** 12 event types on `governance-events` topic
- **Consumes:** Gateway events (for audit correlation), Provider events (for quota tracking)

### Integration with Redis

The governance module uses Redis for caching:
- Policy definitions cached with 30-minute TTL
- Configuration entries cached with 60-minute TTL
- RBAC permission sets cached with 15-minute TTL
- Usage quota counters cached with 5-minute TTL (with periodic DB persistence)
- Recent audit records cached with 10-minute TTL for fast query

### Integration with Monitoring

The governance module exposes 10 Micrometer metrics:
- `governance.policy.evaluations` (counter) — Total policy evaluation calls
- `governance.policy.violations` (counter) — Total policy violations
- `governance.audit.records.created` (counter) — Total audit records created
- `governance.config.changes` (counter) — Total configuration changes
- `governance.rate.limits.exceeded` (counter) — Total rate limit violations
- `governance.quota.exceeded` (counter) — Total quota exceeded events
- `governance.active.policies` (gauge) — Currently active policies count
- `governance.quota.utilization` (gauge) — Current quota usage percentage
- `governance.cache.hit.ratio` (gauge) — Redis cache hit ratio
- `governance.compliance.status` (gauge) — Overall compliance status (0=pass, 1=warning, 2=fail)

## Technology Stack

| Component | Technology |
|-----------|------------|
| Language | Java 17 |
| Framework | Spring Boot 3.x, Spring Modulith |
| Database | PostgreSQL (H2 for tests) |
| Cache | Redis |
| Messaging | Kafka |
| ORM | Spring Data JPA / Hibernate |
| Security | Spring Security, JWT |
| Monitoring | Micrometer, Spring Actuator |
| Testing | JUnit 5, Mockito, ArchUnit, Testcontainers |
