# Decision Engine Architecture

## Module Structure

```
com.sporekart.ai.decision
├── api/                    # Port interfaces (10)
│   ├── DecisionEngine.java
│   ├── DecisionResolver.java
│   ├── DecisionEvaluator.java
│   ├── DecisionReasoningService.java
│   ├── DecisionExplanationService.java
│   ├── DecisionAuditService.java
│   ├── DecisionMetricsService.java
│   ├── DecisionHealthService.java
│   ├── DecisionConfigurationService.java
│   └── DecisionRegistryService.java
├── application/            # Service implementations (10)
│   ├── DecisionEngineImpl.java
│   ├── DecisionResolverImpl.java
│   ├── DecisionEvaluatorImpl.java
│   ├── DecisionReasoningServiceImpl.java
│   ├── DecisionExplanationServiceImpl.java
│   ├── DecisionAuditServiceImpl.java
│   ├── DecisionMetricsServiceImpl.java
│   ├── DecisionHealthServiceImpl.java
│   ├── DecisionConfigurationServiceImpl.java
│   └── DecisionRegistryServiceImpl.java
├── config/                 # Configuration (1)
│   └── DecisionConfig.java
├── domain/                 # Domain layer (18: 4 enums + 14 records)
│   ├── DecisionStatus.java        (enum)
│   ├── DecisionAction.java        (enum)
│   ├── ConflictStrategy.java      (enum)
│   ├── DecisionConfidence.java    (enum)
│   ├── DecisionRequest.java       (record)
│   ├── DecisionContext.java       (record)
│   ├── DecisionResult.java        (record)
│   ├── DecisionReason.java        (record)
│   ├── DecisionExplanation.java   (record)
│   ├── DecisionAudit.java         (record)
│   ├── DecisionEvidence.java      (record)
│   ├── DecisionOverride.java      (record)
│   ├── DecisionMetadata.java      (record)
│   ├── DecisionRegistry.java      (record)
│   ├── DecisionRule.java          (record)
│   ├── DecisionStatistics.java    (record)
│   ├── DecisionLifecycle.java     (record)
│   └── DecisionConfig.java        (record)
├── infrastructure/
│   ├── persistence/               # 6 entities + 6 repositories
│   ├── kafka/DecisionKafkaEventPublisher.java
│   ├── redis/DecisionRedisCacheService.java
│   ├── monitoring/DecisionMonitoringService.java
│   └── security/DecisionException.java
├── interfaces/
│   └── rest/
│       ├── DecisionController.java
│       └── dto/                   # 10 DTO records
```

## Hexagonal Layers

| Layer | Role |
|-------|------|
| **Domain** | Immutable records and enums — no dependencies on any framework |
| **API** | Port interfaces defining the contract for each service |
| **Application** | Implements API contracts, orchestrates pipeline, contains all business logic |
| **Config** | @ConfigurationProperties for module settings |
| **Infrastructure/Persistence** | JPA entities and repositories mapping to 6 database tables |
| **Infrastructure/Kafka** | Event publisher for 8 decision event types |
| **Infrastructure/Redis** | Cache service with 5 namespaces |
| **Infrastructure/Monitoring** | Micrometer counters, timer, and gauges |
| **Infrastructure/Security** | DecisionException with DEC_400/DEC_404/DEC_500 codes |
| **Interfaces/REST** | REST controller with 8 endpoints, 10 DTO records, RFC 9457 error handling |

## Pipeline Flow (resolve → evaluate → reason → explain → audit → metrics)

```
DecisionRequest
    │
    ▼
┌──────────────────┐
│ 1. Resolve       │  DecisionResolverImpl.resolveContext()
│    Context       │  Builds DecisionContext from request payload,
│                  │  subject (userId/roles), environment (timestamp)
└──────┬───────────┘
       │
       ▼
┌──────────────────┐
│ 2. Evaluate      │  DecisionEvaluatorImpl.evaluate()
│    Action        │  Delegates to DecisionReasoningServiceImpl
│                  │  resolveDecision() for action determination
└──────┬───────────┘
       │
       ▼
┌──────────────────┐
│ 3. Reason        │  DecisionEvaluatorImpl.evaluateConfidence()
│    & Confidence  │  generateReasons() via reasoning service
│                  │  calculateConfidence() with weight thresholds
└──────┬───────────┘
       │
       ▼
┌──────────────────┐
│ 4. Explain       │  DecisionExplanationServiceImpl
│    & Evidence    │  generateExplanation() / gatherEvidence()
│                  │  generateExplanationText() / generateSummary()
└──────┬───────────┘
       │
       ▼
┌──────────────────┐
│ 5. Audit         │  DecisionAuditServiceImpl.recordAudit()
│    Record        │  Persists to ai_decision_audit table
└──────┬───────────┘
       │
       ▼
┌──────────────────┐
│ 6. Metrics       │  DecisionMetricsServiceImpl.recordDecision()
│    Record        │  Updates AtomicLong counters (total, allowed,
│                  │  denied, escalated, approval, conflict)
└──────┬───────────┘
       │
       ▼
DecisionResult (returned to caller)
```

## Supported Decisions

The engine evaluates decisions across any module/action combination. The `DecisionAction` enum defines 10 supported outcomes:

| Decision Action | Meaning |
|----------------|---------|
| `ALLOW` | Request is permitted |
| `DENY` | Request is denied |
| `REQUIRE_APPROVAL` | Request requires manual approval |
| `LIMIT_RESPONSE` | Response is limited/truncated |
| `REDACT_CONTENT` | Content must be redacted |
| `ESCALATE_TO_ADMIN` | Escalated to administrator |
| `RETRY` | Operation should be retried |
| `FALLBACK_PROVIDER` | Use fallback provider |
| `BLOCK_REQUEST` | Request is blocked entirely |
| `CUSTOM_EXTENSION` | Custom extension action |

Decision statuses track lifecycle: PENDING → EVALUATING → {ALLOWED, DENIED, ESCALATED, APPROVED, FAILED, REJECTED}.

## Integration with Policy Engine

The Decision Engine does not directly call the Policy Engine. Policy evaluation results are provided as input to the decision request:

- `matchedPolicyIds` — List of policy IDs that matched
- `matchedRuleIds` — List of rule IDs that triggered
- `policyResults` — Map of policy evaluation summaries

The Decision Engine uses these pre-computed policy results to build context, generate explanations, and record audit trails. The Decision Engine handles rule-based decision resolution independently using `DecisionRule` records from `ai_decision_rules`.

### Conflict Resolution (8 strategies)

| Strategy | Implementation |
|----------|----------------|
| `DENY_OVERRIDES` | DENY if any DENY, else ALLOW |
| `ALLOW_OVERRIDES` | ALLOW if any ALLOW, else DENY |
| `SAFE_DEFAULT` | Always DENY |
| `FAIL_CLOSED` | Always BLOCK_REQUEST |
| `MOST_RECENT` | Last action in list |
| `PRIORITY_BASED` | Falls through to default (first element) |
| `WEIGHTED` | Falls through to default (first element) |
| `CUSTOM` | Falls through to default (first element) |

### Confidence Calculation

```
totalWeight >= 100  → CERTAIN
totalWeight >= 75   → HIGH
totalWeight >= 50   → MEDIUM
totalWeight >= 25   → LOW
otherwise           → VERY_LOW
```

Empty/no rules returns LOW.
