# Sprint 18 Part 2 — Enterprise AI Policy Engine

**Lead:** Enterprise AI Platform Engineering Team  
**Module:** ai-service  
**Type:** Policy Engine & Enforcement  
**Version:** 2.1.0

## Objective

Build the Enterprise AI Policy Engine — the centralized system for defining, resolving, evaluating, and enforcing policies across all AI services. Provides policy lifecycle management, rule-based evaluation, condition matching, decision strategies, audit logging, and monitoring.

## Scope

Strictly limited to policy infrastructure (policy definition, resolution, evaluation, decision, audit). Does NOT include:
- RBAC roles, encryption, compliance, or usage quotas (Sprint 18 Part 3+)
- Provider-level policy enforcement
- Real-time enforcement filters or circuit breakers

## Architecture

Hexagonal (ports & adapters) architecture under `com.sporekart.ai.policy.*` with 5 layers:
- **domain/** — Pure Java records and enums (no framework dependencies)
- **api/** — Port interfaces defining service contracts
- **application/** — Service implementations orchestrating domain logic
- **engine/** — Rule matching and condition evaluation engines
- **infrastructure/** — Persistence, Kafka, Redis, monitoring, security
- **interfaces/rest/** — REST controller and DTOs

## Modules (24 files)

### Domain Layer — 8 Enums + 14 Records (22 files)

**Enums:**
| Enum | Values |
|------|--------|
| `PolicyStatus` | ACTIVE, INACTIVE, DRAFT, ARCHIVED, DEPRECATED, PENDING_REVIEW |
| `PolicySeverity` | INFO, WARNING, ERROR, CRITICAL, BLOCKING |
| `PolicyDecision` | ALLOW, DENY, REVIEW, LOG, BYPASS, CHALLENGE |
| `PolicyScope` | GLOBAL, MODULE, ROLE, ENVIRONMENT, PROVIDER, PROMPT, CONVERSATION, WORKFLOW, KNOWLEDGE, CUSTOM |
| `PolicyAction` | CREATE, READ, UPDATE, DELETE, EXECUTE, EVALUATE, DEPLOY |
| `ConditionOperator` | EQUALS, NOT_EQUALS, CONTAINS, NOT_CONTAINS, GREATER_THAN, LESS_THAN, GREATER_EQUALS, LESS_EQUALS, IN, NOT_IN, EXISTS, NOT_EXISTS, MATCHES, STARTS_WITH, ENDS_WITH |
| `ConflictStrategy` | HIGHEST_PRIORITY_WINS, LOWEST_PRIORITY_WINS, MOST_SPECIFIC_WINS, LEAST_SPECIFIC_WINS, DENY_OVERRIDES, ALLOW_OVERRIDES, REQUIRE_ALLOW |
| `PolicyType` | GLOBAL, MODULE, ROLE_BASED, ENVIRONMENT, PROVIDER, PROMPT, CONVERSATION, WORKFLOW, KNOWLEDGE, CUSTOM |

**Records:**
| Record | Key Fields |
|--------|-----------|
| `Policy` | id, name, type, status, severity, scope, priority, module, rules, conditions, metadata |
| `PolicyRule` | id, policyId, name, expression, parameters, decision, order |
| `PolicyCondition` | id, ruleId, field, operator, value, negate, order |
| `PolicyContext` | id, requestId, module, action, scope, resource, subject, environment, roles |
| `PolicyEvaluation` | id, requestId, policyId, decision, violations, evaluationTimeMs, rulesEvaluated |
| `PolicyViolation` | id, evaluationId, ruleId, ruleName, message, severity, details, overridable |
| `PolicyVersion` | id, policyId, versionNumber, content, status, changeNotes |
| `PolicyMetadata` | id, policyId, key, value, type |
| `PolicyAudit` | id, policyId, requestId, action, decision, violations, userId, processingTimeMs, success |
| `PolicyRegistry` | id, name, module, type, scope, isActive, isRegistered, config |
| `EvaluationRequest` | id, module, action, payload, context, userId, roles, headers |
| `EvaluationResult` | requestId, finalDecision, evaluations, violations, totalEvaluationTimeMs, passed |
| `PolicyExpression` | expression, bindings, language, compiled |
| `PolicyConfiguration` | id, key, value, description, isActive, version |

### API Layer — 11 Port Interfaces

| Interface | Key Methods |
|-----------|-------------|
| `PolicyEngine` | evaluate(), evaluateWithContext(), isAllowed(), determineDecision() |
| `PolicyEvaluator` | evaluate(), evaluateRules(), resolveDecision(), matches() |
| `PolicyResolver` | resolvePolicies(), resolvePoliciesByModule(), resolvePoliciesByScope(), resolveActivePolicies(), resolvePolicy() |
| `PolicyLifecycleManager` | activatePolicy(), deactivatePolicy(), archivePolicy(), draftPolicy(), createVersion(), getVersions(), canTransition() |
| `PolicyValidator` | validatePolicy(), validateRule(), validateRequest(), isValidPolicy(), isValidRequest() |
| `PolicyCompiler` | compile(), validate(), parse(), validatePolicy(), isCompiled() |
| `PolicyDecisionService` | decide(), resolveConflict(), isAllowed(), requiresReview() |
| `PolicyConfigurationService` | getConfig(), setConfig(), getAllConfigs(), reloadConfig(), isFeatureEnabled() |
| `PolicyAuditService` | recordAudit(), findByPolicyId(), findByRequestId(), findByUserId(), findByDateRange(), findByDecision() |
| `PolicyMetricsService` | recordEvaluation(), recordViolation(), recordCacheHit(), recordCacheMiss(), recordPolicyActivation(), recordPolicyDeactivation(), getMetrics(), getEvaluationCount(), getViolationCount(), getAverageEvaluationTime() |
| `PolicyRegistry` | register(), unregister(), findById(), findByModule(), findByScope(), findAll(), isRegistered() |

### Application Layer — 11 Service Implementations

| Service | Implements | Dependencies |
|---------|-----------|-------------|
| `PolicyEngineImpl` | PolicyEngine | Resolver, Evaluator, DecisionService, AuditService, MetricsService |
| `PolicyEvaluatorImpl` | PolicyEvaluator | DecisionService |
| `PolicyResolverImpl` | PolicyResolver | PolicyRepository |
| `PolicyLifecycleManagerImpl` | PolicyLifecycleManager | PolicyRepository, PolicyVersionRepository |
| `PolicyValidatorImpl` | PolicyValidator | — |
| `PolicyCompilerImpl` | PolicyCompiler | — |
| `PolicyDecisionServiceImpl` | PolicyDecisionService | — |
| `PolicyConfigurationServiceImpl` | PolicyConfigurationService | — (in-memory ConcurrentHashMap) |
| `PolicyAuditServiceImpl` | PolicyAuditService | PolicyAuditRepository |
| `PolicyMetricsServiceImpl` | PolicyMetricsService | — (AtomicLong counters) |
| `PolicyRegistryImpl` | PolicyRegistry | PolicyRegistryRepository |

### Engine Layer — 2 Classes

| Class | Role |
|-------|------|
| `RuleEngine` | Rule matching (score-based), conflict resolution (7 strategies), policy filtering |
| `ConditionEvaluator` | Condition evaluation (14 operators), evaluateAll, evaluateAny |

Inner record: `RuleEngine.RuleMatchResult(ruleId, matched, score, details)`

## Database (Flyway V21)

**7 tables, 24 indexes:**

| Table | Key Columns |
|-------|-----------|
| `ai_policies` | id UUID PK, name, description, type, status, severity, scope, priority, module, rules TEXT, conditions TEXT, metadata TEXT, is_active, is_system, created_by, created_at, updated_at, is_deleted |
| `ai_policy_rules` | id UUID PK, policy_id, name, description, expression TEXT, parameters TEXT, decision, rule_order, is_active, created_at, updated_at, is_deleted |
| `ai_policy_conditions` | id UUID PK, rule_id, field, operator, condition_value TEXT, negate, condition_order, created_at, is_deleted |
| `ai_policy_versions` | id UUID PK, policy_id, version_number, name, description, content TEXT, status, change_notes, created_by, created_at, is_deleted |
| `ai_policy_evaluations` | id UUID PK, request_id, policy_id, decision, violations TEXT, context TEXT, evaluation_time_ms, rules_evaluated, rules_passed, rules_failed, matched, timestamp, is_deleted |
| `ai_policy_audit` | id UUID PK, policy_id, request_id, action, decision, violations TEXT, details TEXT, user_id, processing_time_ms, success, timestamp, created_at, is_deleted |
| `ai_policy_registry` | id UUID PK, name, module, type, scope, is_active, is_registered, config TEXT, registered_at, updated_at, is_deleted |

Indexes: ai_policies (5), ai_policy_rules (2), ai_policy_conditions (2), ai_policy_versions (2), ai_policy_evaluations (4), ai_policy_audit (5), ai_policy_registry (4) = 24 total

## REST API — 10 Endpoints

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/v1/policies` | List policies (filter by module/scope) |
| GET | `/api/v1/policies/{id}` | Get policy by ID |
| POST | `/api/v1/policies` | Create policy |
| PUT | `/api/v1/policies/{id}` | Update policy |
| DELETE | `/api/v1/policies/{id}` | Delete policy (archive soft-delete) |
| POST | `/api/v1/policies/evaluate` | Evaluate a policy request |
| GET | `/api/v1/policies/evaluations` | List evaluations (filter by decision) |
| GET | `/api/v1/policies/violations` | List violations (stub — returns empty) |
| POST | `/api/v1/policies/reload` | Reload policies |
| GET | `/api/v1/policies/health` | Health check with metrics |

Error handling via `PolicyErrorDto` (RFC 9457-style): type, title, status, detail, extensions.

### DTOs (10 records)

EvaluationRequestDto, EvaluationResultDto, EvaluationListDto, PolicyRequestDto, PolicyResponseDto, PolicyListDto, PolicyHealthDto, PolicyReloadDto, PolicyErrorDto, ViolationDto

## Kafka — 8 Event Types

**Topic:** `policy-events` (3 partitions, 1 replica)

| Event Type | Published When |
|-----------|---------------|
| PolicyCreated | Policy created via POST endpoint |
| PolicyUpdated | Policy updated via PUT endpoint |
| PolicyDeleted | Policy deleted via DELETE endpoint |
| PolicyActivated | Policy lifecycle activation |
| PolicyDeactivated | Policy lifecycle deactivation |
| PolicyEvaluated | Policy evaluation completed (POST /evaluate) |
| PolicyViolationDetected | Violation detected during evaluation |
| PolicyEvaluationFailed | Evaluation failure |

Event envelope: `{ id, type, timestamp, source: "policy", details }`

## Redis — 5 Cache Namespaces

| Namespace | Prefix | TTL | Purpose |
|-----------|--------|-----|---------|
| Registry | `policy:registry:` | 300s | Policy registry cache |
| Compiled | `policy:compiled:` | 600s | Compiled expressions cache |
| Metadata | `policy:metadata:` | 300s | Policy metadata cache |
| Evaluation | `policy:evaluation:` | 180s | Evaluation result cache |
| Health | `policy:health:` | 60s | Health check cache |

## Test Coverage — 18 Files

| Test Class | Tests |
|-----------|-------|
| `PolicyEngineImplTest` | Engine orchestration |
| `PolicyEvaluatorImplTest` | Evaluation logic |
| `PolicyDecisionServiceImplTest` | Decision strategies |
| `PolicyLifecycleManagerImplTest` | Lifecycle transitions |
| `PolicyValidatorImplTest` | Validation rules |
| `PolicyCompilerImplTest` | Expression compilation |
| `PolicyConfigurationServiceImplTest` | Config CRUD |
| `PolicyAuditServiceImplTest` | Audit recording & query |
| `PolicyMetricsServiceImplTest` | Metrics recording |
| `PolicyRegistryImplTest` | Registry CRUD |
| `RuleEngineTest` | Rule matching & conflict resolution |
| `ConditionEvaluatorTest` | Condition evaluation (14 operators) |
| `PolicyRecordTest` | Domain record construction |
| `PolicyEnumTest` | Enum values & meta |
| `PolicyConfigTest` | Configuration properties |
| `PolicyRedisCacheServiceTest` | Redis operations |
| `PolicyKafkaEventPublisherTest` | Event publishing |
| `PolicyMonitoringServiceTest` | Micrometer metrics |

Coverage targets: application services >90%, engine >90%, infrastructure >80%.

## Configuration

**application.yml:**
```yaml
ai:
  features:
    policy-enabled: true
    policy-caching: true
    policy-audit: true
    policy-monitoring: true
    policy-evaluation: true
  modules:
    policy:
      enabled: true
      default-decision: ALLOW
      default-conflict-strategy: DENY_OVERRIDES
      cache:
        registry-ttl-seconds: 300
        compiled-ttl-seconds: 600
        metadata-ttl-seconds: 300
        evaluation-ttl-seconds: 180
        health-ttl-seconds: 60
      kafka:
        topic: policy-events
        partitions: 3
        replication-factor: 1
```

**KafkaConfig.java:** `policyEventsTopic()` bean (NewTopic, 3 partitions, 1 replica)

**SecurityConfig.java:** Permits `/api/v1/policies/**`

**PolicyConfig.java:** `@ConfigurationProperties(prefix = "policy")` with CacheConfig and KafkaConfig inner classes

## Key Decisions

- All domain objects are immutable Java records
- Policy evaluation pipeline: resolve → evaluate → decide → audit (synchronous)
- Decision strategies use DENY_OVERRIDES as default conflict resolution
- Condition evaluation supports 14 operators including string, numeric, and collection operations
- Rule matching uses score-based approach (module+action=10pt, role=5pt, param=15pt)
- Audit records are write-once — no immutability trigger at DB level (unlike Governance module)
- Expression compilation is a pass-through stub ("simple" language) — no real expression engine in this sprint
- Configuration is in-memory (ConcurrentHashMap) — not DB-backed
- All file counts, types, and endpoints reflect ONLY Sprint 18 Part 2 implementation

## Risks

- Expression "language" is a passthrough — no AST or bytecode compilation
- Configuration is in-memory — lost on restart
- Rules and conditions stored as TEXT (JSON) — no relational normalization
- List policies has no pagination — limited to in-memory listing
- Violations endpoint returns empty list — stub implementation
- No hard-delete — soft-delete only with is_deleted flag
- Cache invalidation is manual (invalidateAll) — no selective eviction
- No async evaluation — all evaluations are synchronous
- H2-compatible schema (TIMESTAMP not TIMESTAMPTZ, TEXT not JSONB)

## Tech Debt

- In-memory PolicyConfigurationServiceImpl — needs DB persistence
- Expression compilation is a stub — needs actual expression language (e.g., SpEL, MVEL)
- Rules/conditions stored as serialized TEXT — should be normalized or JSONB
- No HTTP caching headers on REST responses
- No pagination or sorting on list endpoints
- No rate limiting on policy API
