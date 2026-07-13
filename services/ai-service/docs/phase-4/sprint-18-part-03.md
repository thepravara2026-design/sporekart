# Sprint 18 Part 3 — Enterprise AI Decision Engine

**Lead:** Enterprise AI Platform Engineering Team
**Module:** ai-service
**Type:** Decision Engine Architecture & Implementation

## Objective

Build the Enterprise AI Decision Engine — a centralized system for making, explaining, auditing, and recording decisions across all AI services. Implements a domain-driven decision pipeline with configurable conflict resolution strategies, confidence scoring, and immutable audit trails.

## Scope

- Domain model: 4 enums, 14 records under `com.sporekart.ai.decision.domain`
- API layer: 10 port interfaces under `com.sporekart.ai.decision.api`
- Application layer: 10 service implementations + `DecisionConfig` under `com.sporekart.ai.decision.application` and `config`
- Infrastructure: 6 persistence entities + 6 repositories, Kafka publisher, Redis cache, Micrometer monitoring, security exception
- REST API: 8 endpoints under `/api/v1/decisions/*`
- Flyway V22: 6 tables, 18 indexes
- Pipeline: resolve → evaluate → reason → explain → audit → metrics

## Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                    Decision Controller (REST)                    │
│         POST /evaluate  GET /{id}  GET /statistics ...          │
└───────────────────────────┬─────────────────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────────────────┐
│                     Decision Engine (API)                        │
│              evaluate / replay / resolveConflict                 │
└───────────┬──────────┬──────────┬──────────┬────────────────────┘
            │          │          │          │
     ┌──────▼──┐ ┌────▼───┐ ┌───▼────┐ ┌───▼──────┐
     │Resolver │ │Evaluator│ │Reasoner│ │Explainer │
     │(context,│ │(rules,  │ │(conflict│ │(evidence,│
     │ rules,  │ │actions) │ │ strat.) │ │summary)  │
     │registry)│ │         │ │        │ │          │
     └─────────┘ └────────┘ └────────┘ └──────────┘
          │                                        │
     ┌────▼──────────┐                   ┌─────────▼──────┐
     │ Audit Service  │                   │ Metrics Service│
     │ (JPA → DB)     │                   │ (in-memory)    │
     └────────────────┘                   └────────────────┘
```

## Domain Model

### Enums (4)

| Enum | Values |
|------|--------|
| `DecisionStatus` | PENDING, EVALUATING, ALLOWED, DENIED, ESCALATED, APPROVED, FAILED, REJECTED (8) |
| `DecisionAction` | ALLOW, DENY, REQUIRE_APPROVAL, LIMIT_RESPONSE, REDACT_CONTENT, ESCALATE_TO_ADMIN, RETRY, FALLBACK_PROVIDER, BLOCK_REQUEST, CUSTOM_EXTENSION (10) |
| `ConflictStrategy` | PRIORITY_BASED, WEIGHTED, DENY_OVERRIDES, ALLOW_OVERRIDES, MOST_RECENT, SAFE_DEFAULT, FAIL_CLOSED, CUSTOM (8) |
| `DecisionConfidence` | CERTAIN, HIGH, MEDIUM, LOW, VERY_LOW, INCONCLUSIVE (6) |

### Records (14)

| Record | Key Fields |
|--------|-----------|
| `DecisionRequest` | module, action, payload, context, userId, roles, matchedPolicyIds, matchedRuleIds, policyResults |
| `DecisionContext` | requestId, module, action, resource, subject, environment, roles, activePolicyIds |
| `DecisionResult` | requestId, action, status, confidence, summary, reasons, evidence, explanation, processingTimeMs |
| `DecisionReason` | code, message, category, confidence, details |
| `DecisionExplanation` | decisionId, summary, matchedPolicies, triggeredRules, reasoning, evidenceList, explanationText |
| `DecisionEvidence` | source, type, value, relevance, metadata |
| `DecisionAudit` | requestId, decisionId, action, status, confidence, reasons, context, userId, processingTimeMs |
| `DecisionLifecycle` | decisionId, fromStatus, toStatus, triggeredBy, reason |
| `DecisionOverride` | decisionId, originalAction, overrideAction, reason, overriddenBy |
| `DecisionMetadata` | decisionId, version, environment, tags, attributes |
| `DecisionRegistry` | name, module, endpoint, isActive, isRegistered, config |
| `DecisionRule` | name, action, priority, weight, conditions, overrides |
| `DecisionStatistics` | totalDecisions, allowedCount, deniedCount, escalatedCount, averageConfidence, averageLatencyMs |
| `DecisionConfig` | key, value, description, isActive, version |

## API Layer (10 interfaces)

| Interface | Key Methods |
|-----------|-------------|
| `DecisionEngine` | evaluate(), evaluateWithContext(), replay(), resolveConflict(), isAllowed() |
| `DecisionResolver` | resolveContext(), resolveRules(), resolveRegistries(), findById(), findByRequestId() |
| `DecisionEvaluator` | evaluate(), evaluateAction(), evaluateConfidence(), generateReasons() |
| `DecisionReasoningService` | resolveDecision(), resolveConflict(), calculateConfidence(), buildReasons(), requiresOverride() |
| `DecisionExplanationService` | generateExplanation(), generateSummary(), gatherEvidence(), generateExplanationText() |
| `DecisionAuditService` | recordAudit(), findByRequestId(), findByUserId(), findByAction(), findByStatus(), findByDateRange() |
| `DecisionMetricsService` | recordDecision(), recordConflict(), recordReplay(), getStatistics(), getDetailedMetrics() |
| `DecisionHealthService` | checkHealth(), getStatus(), isOperational(), getMetrics() |
| `DecisionConfigurationService` | getConfig(), setConfig(), getAllConfigs(), reloadConfig(), isFeatureEnabled() |
| `DecisionRegistryService` | register(), unregister(), findById(), findByModule(), findAll(), isRegistered() |

## Application Layer (10 impls + DecisionConfig)

| Service | Responsibility |
|---------|----------------|
| `DecisionEngineImpl` | Orchestrates resolve → evaluate → explain → audit → metrics pipeline |
| `DecisionResolverImpl` | Builds DecisionContext from request, queries repositories for rules and registries |
| `DecisionEvaluatorImpl` | Delegates to reasoning service for action/confidence/reasons |
| `DecisionReasoningServiceImpl` | Rule matching (priority/weight sorted), conflict resolution (8 strategies), confidence calculation |
| `DecisionExplanationServiceImpl` | Generates explanation, summary, evidence, explanation text |
| `DecisionAuditServiceImpl` | CRUD for audit records via JPA repository |
| `DecisionMetricsServiceImpl` | In-memory AtomicLong counters for decisions, conflicts, replays |
| `DecisionHealthServiceImpl` | Returns UP/DEVELOPMENT status, delegates to metrics service |
| `DecisionConfigurationServiceImpl` | In-memory ConcurrentHashMap config store |
| `DecisionRegistryServiceImpl` | CRUD for decision registry via JPA repository |
| `DecisionConfig` | @ConfigurationProperties(prefix="decision") with cache TTLs, Kafka config, default action/strategy |

## Pipeline Flow

The `DecisionEngine.evaluate()` orchestrates:
1. **Resolve Context** — `DecisionResolver.resolveContext()` builds full context from request
2. **Evaluate** — `DecisionEvaluator.evaluate()` determines action, confidence, reasons
3. **Explain** — `DecisionExplanationService.generateExplanation()` creates explanation with evidence
4. **Audit** — `DecisionAuditService.recordAudit()` persists immutable audit record
5. **Metrics** — `DecisionMetricsService.recordDecision()` records counters and timing

## Database (6 tables, 18 indexes)

See `docs/database/decision-schema.md`.

| Table | Purpose | Indexes |
|-------|---------|---------|
| `ai_decisions` | Decision results | 5 (request_id, action, status, confidence, created_at) |
| `ai_decision_rules` | Decision rule definitions | 2 (action, created_at) |
| `ai_decision_audit` | Immutable audit trail | 7 (request, decision, user, action, status, confidence, created_at) |
| `ai_decision_explanations` | Generated explanations | 1 (decision_id) |
| `ai_decision_history` | Lifecycle transitions | 1 (decision_id) |
| `ai_decision_registry` | Module/endpoint registry | 2 (module, created_at) |

## REST API (8 endpoints)

| Method | Path | Purpose |
|--------|------|---------|
| `POST` | `/api/v1/decisions/evaluate` | Evaluate a decision request |
| `GET` | `/api/v1/decisions` | List decisions (stub — returns empty) |
| `GET` | `/api/v1/decisions/{id}` | Get decision by ID |
| `GET` | `/api/v1/decisions/history` | Get decision history (stub — returns empty) |
| `GET` | `/api/v1/decisions/explanations` | Get explanations (stub — returns empty) |
| `GET` | `/api/v1/decisions/statistics` | Get decision statistics |
| `GET` | `/api/v1/decisions/health` | Health check |
| `POST` | `/api/v1/decisions/replay` | Replay a decision |

See `docs/api/decision-api.md` for full API reference.

## Kafka Events (8 event types on `decision-events` topic)

| Event | Published By |
|-------|-------------|
| `DecisionEvaluated` | POST /evaluate after engine.evaluate() |
| `DecisionAllowed` | DecisionKafkaEventPublisher |
| `DecisionDenied` | DecisionKafkaEventPublisher |
| `DecisionEscalated` | DecisionKafkaEventPublisher |
| `DecisionExplanationGenerated` | DecisionKafkaEventPublisher |
| `DecisionAuditCreated` | DecisionKafkaEventPublisher |
| `DecisionReplayStarted` | POST /replay |
| `DecisionReplayCompleted` | POST /replay |

Topic configured with 3 partitions, replication factor 1.

## Redis Cache (5 namespaces)

| Namespace | Prefix | TTL |
|-----------|--------|-----|
| Decision Result | `decision:result:` | 300s |
| Decision Metadata | `decision:metadata:` | 300s |
| Registry | `decision:registry:` | 300s |
| Statistics | `decision:stats:` | 120s |
| Explanation | `decision:explanation:` | 300s |

## Test Files (1 file, 4 tests)

See `docs/testing/decision-testing.md`.

| Test Class | Tests | Package |
|------------|-------|---------|
| `DecisionEnumTest` | 4 | `com.sporekart.ai.decision.domain` |

## Configuration Changes

### Files Created
- `com.sporekart.ai.decision.config.DecisionConfig` — @ConfigurationProperties with cache TTLs, Kafka config, default action (ALLOW), default conflict strategy (DENY_OVERRIDES)

### Files Modified
- `config/KafkaConfig.java` — Added `decisionEventsTopic()` bean (3 partitions, 1 replica)
- `infrastructure/security/SecurityConfig.java` — Permitted `/api/v1/decisions/**` endpoints
- `resources/application.yml` — Added decision feature flags (decision-enabled, decision-caching, decision-audit, decision-monitoring) and module config

## Pipeline: resolve → evaluate → reason → explain → audit → metrics

Synchronous pipeline orchestrated by `DecisionEngineImpl`:
1. `resolver.resolveContext(request)` — Build full DecisionContext from request payload
2. `evaluator.evaluate(request, context)` — Delegates to reasoning service for action, confidence, reasons
3. `explanationService.generateExplanation(result, request)` — Constructs DecisionExplanation with evidence, summary, text
4. `auditService.recordAudit(audit)` — Persists immutable DecisionAudit to `ai_decision_audit`
5. `metricsService.recordDecision(action, confidence, timeMs)` — Updates in-memory AtomicLong counters

## Conflict Resolution Strategies

Implemented in `DecisionReasoningServiceImpl.resolveConflict()`:

| Strategy | Behavior |
|----------|----------|
| `DENY_OVERRIDES` | If any DENY, return DENY; else ALLOW |
| `ALLOW_OVERRIDES` | If any ALLOW, return ALLOW; else DENY |
| `SAFE_DEFAULT` | Always return DENY |
| `FAIL_CLOSED` | Return BLOCK_REQUEST |
| `MOST_RECENT` | Return last action in list |
| Other (default) | Return first action in list |

8 strategies defined in enum; 5 implemented in switch; PRIORITY_BASED, WEIGHTED, CUSTOM fall through to default.

## Confidence Calculation

Implemented in `DecisionReasoningServiceImpl.calculateConfidence()`:

| Total Weight | Confidence |
|--------------|------------|
| >= 100 | CERTAIN |
| >= 75 | HIGH |
| >= 50 | MEDIUM |
| >= 25 | LOW |
| < 25 or empty | VERY_LOW |

## Key Decisions

- All domain objects are immutable Java records
- Decision pipeline is synchronous: resolve → evaluate → reason → explain → audit → metrics
- Decision evaluation always returns immediate result (no async)
- Conflict resolution defaults to DENY_OVERRIDES (fail-safe)
- Audit records are append-only via soft-delete (`is_deleted` flag)
- Configuration is in-memory (ConcurrentHashMap) — not DB-backed
- Metrics are in-memory AtomicLong counters — not persisted
- Replay is a stub — logs request and records metrics but returns null result
- No Policy Engine integration at this phase — policy IDs/rule IDs passed as request data
- REST endpoints for list, history, explanations return empty stubs
- Follows same DDD/Hexagonal pattern as Governance, Policy, Knowledge, Prompt, and other modules

## Risks

- In-memory configuration lost on restart — needs DB persistence
- In-memory metrics lost on restart — no historical metrics
- Replay is unimplemented (returns null)
- List, history, explanations endpoints return empty results
- No test coverage for services, controller, or infrastructure (1 enum test only)
- Confidence calculation uses simple weight thresholds — no ML or statistical modeling
- No real Policy Engine integration — relies on caller to provide matched policies/rules
- No async evaluation — all evaluations are synchronous
