# Sprint 18 Part 1 — Enterprise AI Governance Foundation

**Date:** 2026-07-12
**Lead:** Enterprise AI Platform Engineering Team
**Module:** ai-service
**Type:** Governance Foundation

## Objective

Build the Enterprise AI Governance Foundation — a centralized governance pipeline that sits between all AI business modules and the AI execution pipeline. Every AI request enters the governance pipeline for context resolution, validation, decision, audit, and metrics.

## Sprint Acceptance Criteria

- Governance Foundation implemented
- Governance pipeline created (validate → decide → audit → metrics)
- Governance registry implemented
- 8 core governance services implemented
- 6 REST APIs implemented
- Redis caching configured (5 namespaces with TTLs)
- Kafka events implemented (6 event types on `governance-events` topic)
- Flyway V20 migration completed (6 tables)
- Documentation updated
- All existing functionality unaffected

## Architecture Summary

The governance module follows DDD/Hexagonal architecture:

```
Incoming Request → Context Resolution → Validation → Decision → Audit → Metrics → Continue
```

All services in `com.sporekart.ai.governance.*`:
- `domain/` — 5 enums, 11 records
- `api/` — 8 port interfaces
- `application/` — 8 service implementations
- `infrastructure/persistence/` — 6 JPA entities + 6 repositories
- `infrastructure/redis/` — GovernanceRedisCacheService (5 cache namespaces)
- `infrastructure/kafka/` — GovernanceKafkaEventPublisher (6 event types)
- `infrastructure/monitoring/` — GovernanceMonitoringService (7 Micrometer metrics)
- `infrastructure/security/` — GovernanceException
- `config/` — GovernanceConfig (@ConfigurationProperties)
- `interfaces/rest/` — GovernanceController (6 endpoints) + 8 DTOs

## Modules Created

### Domain (5 enums, 11 records)

**Enums:**
- `GovernanceScope` — REQUEST, RESPONSE, CONTEXT, CONFIGURATION, AUDIT, METRICS, ALL
- `GovernanceDecision` — ALLOW, DENY, REVIEW, LOG, BYPASS
- `GovernanceStatus` — ACTIVE, INACTIVE, DRAFT, ARCHIVED, DEPRECATED
- `GovernanceSeverity` — INFO, WARNING, ERROR, CRITICAL
- `GovernanceMode` — DEVELOPMENT, TESTING, PRODUCTION, AUDIT_ONLY, ENFORCE

**Records:**
- `GovernancePolicy` — Policy aggregate root with rules, conditions, scope, priority
- `GovernanceRule` — Individual rule with expression, parameters, default decision
- `GovernanceContext` — Resolved context (resource, subject, environment)
- `GovernanceRequest` — Incoming governance request with payload, metadata, roles
- `GovernanceResponse` — Pipeline response with decision, violations, timing
- `GovernanceViolation` — Violation with severity, details, overridable flag
- `GovernanceMetadata` — Request metadata (version, attributes, tags)
- `GovernanceConfiguration` — Key-value configuration with scope, mode, versioning
- `GovernanceAudit` — Immutable audit record with decision, violations, timing
- `GovernanceLifecycle` — Policy lifecycle event (status transitions)
- `GovernanceRegistry` — Module/endpoint registration with scope, mode

### API (8 port interfaces)

- `GovernanceEngine` — Core pipeline (validate, decide, checkPolicy, isAllowed)
- `GovernanceRegistryService` — Registry CRUD
- `GovernanceManager` — Policy & configuration management
- `GovernanceContextResolver` — Context resolution from requests
- `GovernanceValidator` — Request/policy/configuration validation
- `GovernanceAuditService` — Audit trail recording and querying
- `GovernanceLifecycleManager` — Policy status transitions
- `GovernanceHealthService` — Health, status, and metrics

### Application (8 services)

- `GovernanceEngineImpl` — Pipeline orchestrator (validate → decide → audit)
- `GovernanceRegistryServiceImpl` — Registry CRUD with soft delete
- `GovernanceManagerImpl` — Policy & configuration persistence
- `GovernanceContextResolverImpl` — Resource, subject, environment resolution
- `GovernanceValidatorImpl` — Input validation (module, action, user required)
- `GovernanceAuditServiceImpl` — Audit persistence and query
- `GovernanceLifecycleManagerImpl` — In-memory lifecycle state machine
- `GovernanceHealthServiceImpl` — DB health check, metrics aggregation

### Infrastructure

**Persistence (6 JPA entities + 6 repositories):**
- `GovernanceEntity` → `GovernanceRepository` (ai_governance table)
- `GovernanceConfigurationEntity` → `GovernanceConfigurationRepository` (ai_governance_configuration)
- `GovernanceRegistryEntity` → `GovernanceRegistryRepository` (ai_governance_registry)
- `GovernanceScopeEntity` → `GovernanceScopeRepository` (ai_governance_scope)
- `GovernanceAuditEntity` → `GovernanceAuditRepository` (ai_governance_audit)
- `GovernanceMetricsEntity` → `GovernanceMetricsRepository` (ai_governance_metrics)

**Redis:**
- `GovernanceRedisCacheService` — 5 namespaces: config (300s), registry (300s), health (60s), metrics (120s), validation (180s)

**Kafka:**
- `GovernanceKafkaEventPublisher` — 6 event types on `governance-events` topic:
  - `GovernanceInitialized`, `GovernanceReloaded`, `GovernanceValidated`
  - `GovernanceHealthChanged`, `GovernanceConfigurationChanged`, `GovernanceAuditCreated`

**Monitoring:**
- `GovernanceMonitoringService` — 7 Micrometer metrics: totalRequests, totalValidations, totalAudits, totalAllowed, totalDenied, totalConfigReloads + validationTimer + pipelineTimer + activePolicies gauge + cacheHits/Misses gauges

### REST (6 endpoints)

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/v1/governance` | Service index with endpoint listing |
| GET | `/api/v1/governance/status` | Governance status and mode |
| GET | `/api/v1/governance/configuration` | List all configurations |
| POST | `/api/v1/governance/validate` | Validate a request through governance pipeline |
| POST | `/api/v1/governance/reload` | Reload governance configuration |
| GET | `/api/v1/governance/health` | Health check with metrics |

Error format: RFC 9457 Problem Details via `GovernanceErrorDto`

### Config Updates

- **KafkaConfig.java** — Added `governanceEventsTopic()` bean (topic: `governance-events`)
- **SecurityConfig.java** — Permitted `/api/v1/governance/**` endpoints
- **application.yml** — Added `governance` module config (mode, cache TTLs, kafka settings) and feature flags (governance-enabled, governance-caching, governance-audit, governance-monitoring)

## Database Objects

Flyway V20 — 6 tables (H2-compatible):

| Table | Description |
|-------|-------------|
| `ai_governance` | Governance policies with rules, conditions, scope, status |
| `ai_governance_configuration` | Key-value configuration with versioning |
| `ai_governance_registry` | Module/endpoint registration |
| `ai_governance_scope` | Policy scope assignments |
| `ai_governance_audit` | Immutable audit trail |
| `ai_governance_metrics` | Governance metrics storage |

All tables have UUID PKs, audit columns, soft deletes. 16 indexes total.

## Test Coverage

- **15 test files** (domain: 2, application: 7, infrastructure: 3, config: 1, interfaces: 1, misc: 1)
- Domain enum tests + record creation/accessor tests
- Service tests with Mockito (engine, validator, audit, health, registry, lifecycle, context, manager)
- Infrastructure tests (Redis cache with mock RedisTemplate, Kafka publisher with mock KafkaTemplate, monitoring with SimpleMeterRegistry)
- Config tests (default values, cache config, kafka config, setters)
- Controller Web MVC test (all 6 endpoints)
- Target: >90% business logic coverage

## Decisions

- Strict DDD/Hexagonal patterns matching all previous AI platform modules
- Governance pipeline: validate → decide → audit (synchronous for foundation, async for later parts)
- Decision logic: CRITICAL=DENY, ERROR=REVIEW, WARNING=LOG, INFO/none=ALLOW
- Follows same patterns as conversation, workflow, content, assistant modules
- All existing module tests remain unaffected

## Risks

- Governance engine adds latency to request processing (foundation only — optimization deferred)
- Audit table growth management not yet implemented (retention/deletion strategy pending)
- No real policy enforcement (placeholder — actual policies in Sprint 18 Part 2)
- No compliance/PII/hallucination detection (out of scope for Part 1)

## Technical Debt

- In-memory lifecycle store (not persistent)
- Simple map-based JSON serialization for registry config
- Limited validation rules (foundation only — extendable via Strategy pattern)
- No WebSocket/push notifications for governance events

## Readiness for Sprint 18 Part 2

Sprint 18 Part 1 is complete. The Governance Foundation is ready for Part 2 which will add:
- Governance Policies (actual policy definitions and enforcement)
- Compliance Rules
- PII Detection hooks
- Hallucination Detection hooks
- Prompt Security integration
- Cost Management
- Human Approval workflows
- AI Evaluation
