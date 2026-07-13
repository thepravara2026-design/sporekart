# Decision Pipeline

## 8-Step Pipeline

The Decision Engine processes requests through a synchronous pipeline orchestrated by `DecisionEngineImpl.evaluate()`:

```
Step 1: Request Reception
  POST /api/v1/decisions/evaluate
  DecisionRequestDto → DecisionRequest domain record
  Fields: module, action, payload, context, userId, roles

Step 2: Context Resolution
  DecisionResolverImpl.resolveContext(request)
  → DecisionContext(id, requestId, module, action,
     resource=payload, subject={userId, roles},
     environment={timestamp}, activePolicyIds)

Step 3: Evaluation
  DecisionEvaluatorImpl.evaluate(request, context)
  → Delegates to DecisionReasoningServiceImpl
     resolveDecision() for action determination
  → calculateConfidence() for confidence scoring
  → buildReasons() for reason generation

Step 4: Reasoning
  DecisionReasoningServiceImpl
  → resolveDecision() — sorts active rules by priority/weight
  → calculateConfidence() — weight-based threshold mapping
  → buildReasons() — creates reason records for outcome + matched rules
  → requiresOverride() — checks REQUIRE_APPROVAL/ESCALATE_TO_ADMIN

Step 5: Explanation Generation
  DecisionExplanationServiceImpl.generateExplanation(result, request)
  → generateSummary() — formatted string with action/status/confidence/time
  → gatherEvidence() — converts reasons to evidence records
  → generateExplanationText() — human-readable explanation
  → buildAuditMetadata() — metadata map for audit trail

Step 6: Audit Recording
  DecisionAuditServiceImpl.recordAudit(audit)
  → Maps DecisionAudit to DecisionAuditEntity
  → Persists to ai_decision_audit table
  → Fields: requestId, decisionId, action, status, confidence,
    reasons, context, userId, processingTimeMs, success

Step 7: Metrics Recording
  DecisionMetricsServiceImpl.recordDecision(action, confidence, timeMs)
  → Increments AtomicLong counters:
    totalDecisions, allowedCount, deniedCount,
    escalatedCount, approvalCount
  → Records processing time

Step 8: Response Building
  DecisionController maps DecisionResult → DecisionResponseDto
  → id, requestId, action, status, confidence, summary,
    reasons (as ReasonDto list), processingTimeMs, requiresApproval
```

## Conflict Resolution Strategies

The `DecisionReasoningServiceImpl.resolveConflict()` method supports 8 strategies (5 implemented, 3 fall through to default):

| # | Strategy | Behavior | Use Case |
|---|----------|----------|----------|
| 1 | `PRIORITY_BASED` | Default — returns first action | Fallback for unimplemented strategies |
| 2 | `WEIGHTED` | Default — returns first action | Fallback for unimplemented strategies |
| 3 | `DENY_OVERRIDES` | DENY if any DENY, else ALLOW | Default — fail-safe mode |
| 4 | `ALLOW_OVERRIDES` | ALLOW if any ALLOW, else DENY | Permissive mode |
| 5 | `MOST_RECENT` | Returns last action in list | Time-based resolution |
| 6 | `SAFE_DEFAULT` | Always DENY | Maximum safety |
| 7 | `FAIL_CLOSED` | Always BLOCK_REQUEST | Extreme fail-safe |
| 8 | `CUSTOM` | Default — returns first action | Placeholder for custom logic |

## Confidence Calculation

Confidence is calculated from matched rule weights using threshold mapping:

```
Weight Range       → Confidence
─────────────────────────────────
totalWeight >= 100 → CERTAIN
totalWeight >= 75  → HIGH
totalWeight >= 50  → MEDIUM
totalWeight >= 25  → LOW
< 25 or empty      → VERY_LOW
Inconclusive       → INCONCLUSIVE
```

### Rule Resolution

`DecisionReasoningServiceImpl.resolveDecision()` determines the decision action:
1. Filters active rules (`DecisionRule.isActive == true`)
2. Sorts by priority (descending), then weight (descending)
3. Returns the `action` of the highest-priority rule
4. Defaults to `ALLOW` if no rules match

## Pipeline Characteristics

- **Synchronous** — all steps execute in the same thread; no async processing
- **Deterministic** — same input produces same output
- **Fail-open** — no rules defaults to ALLOW
- **Fail-safe** — default conflict strategy is DENY_OVERRIDES
- **Auditable** — every decision is recorded with full context
- **Measurable** — every decision increments counters and records timing
