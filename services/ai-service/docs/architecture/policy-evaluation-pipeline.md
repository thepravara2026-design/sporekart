# Policy Evaluation Pipeline

## Overview

The policy evaluation pipeline processes an `EvaluationRequest` through 7 sequential steps, producing an `EvaluationResult` with decision, violations, and timing metrics.

## Pipeline Steps

```
 1. Build Context
 2. Load Policies
 3. Evaluate Each Policy
    3a. Match Policy (module/scope check)
    3b. Evaluate Rules (condition matching)
    3c. Collect Violations
 4. Determine Decision
 5. Build Result
 6. Record Audit
 7. Record Metrics
```

### Step 1: Build Context (`PolicyEngineImpl.buildContext`)

Constructs a `PolicyContext` from the `EvaluationRequest` and matched `Policy`:
- Resource map from request payload
- Subject map with userId and roles
- Environment map with timestamp
- Scope from policy definition

### Step 2: Load Policies (`PolicyResolverImpl.resolvePolicies`)

Queries `PolicyRepository` for active, non-deleted policies:
- Filters by `is_active = true` and `status = ACTIVE`
- Sorts by `priority` descending (higher priority first)
- Returns domain `Policy` records via `toDomain()` mapping

### Step 3: Evaluate Each Policy (`PolicyEvaluatorImpl.evaluate`)

For each active policy matching the request:

#### 3a. Match Policy (`PolicyEvaluatorImpl.matches`)
- Policy must be active
- If policy has a module filter, it must match the request module
- Policies without module filter match all requests

#### 3b. Evaluate Rules (`PolicyEvaluatorImpl.evaluateRules`)
- Filters active rules, sorted by `order`
- For each rule, evaluates the expression string against the request:
  - `module` keyword match → score 10
  - `action` keyword match → score 10
  - `role` keyword match → score 5 (per matching role)
  - `parameter` value match → score 15 (per matching param)
- If score > 0, rule is considered matched → produces violation

#### 3c. Collect Violations
- Matched rules produce `PolicyViolation` with severity INFO
- Each violation carries rule name, message, expression details, and decision

### Step 4: Determine Decision (`PolicyEngineImpl.determineDecision`)

Based on violation severity aggregation:
- No violations → ALLOW
- BLOCKING or CRITICAL violations → DENY
- ERROR violations → REVIEW
- WARNING violations → LOG
- Otherwise → ALLOW

### Step 5: Build Result (`PolicyEngineImpl.evaluate`)
- Constructs `EvaluationResult` with final decision, evaluations, violations, timing
- `passed = true` when final decision is ALLOW

### Step 6: Record Audit (`PolicyAuditServiceImpl.recordAudit`)
- Writes `PolicyAudit` to `ai_policy_audit` table via `PolicyAuditEntity`
- Stores: action, decision, violations (as string), details, userId, processing time, success

### Step 7: Record Metrics (`PolicyMetricsServiceImpl`)
- Increments evaluation counter and adds elapsed time to total
- Records each violation severity
- Results available via `getMetrics()`: evaluationCount, violationCount, averageEvaluationTimeMs

## Condition Evaluation (`ConditionEvaluator`)

14 operators supported:

| Operator | Behavior |
|----------|----------|
| EQUALS | Case-insensitive string equality |
| NOT_EQUALS | Negation of EQUALS |
| CONTAINS | Substring match (case-insensitive) |
| NOT_CONTAINS | Negation of CONTAINS |
| GREATER_THAN | Numeric or Comparable comparison |
| LESS_THAN | Numeric or Comparable comparison |
| GREATER_EQUALS | >= comparison |
| LESS_EQUALS | <= comparison |
| IN | Value containment in collection string |
| NOT_IN | Negation of IN |
| EXISTS | Field is not null |
| NOT_EXISTS | Field is null |
| MATCHES | Regex match |
| STARTS_WITH | String prefix match |
| ENDS_WITH | String suffix match |

Field resolution order: module → action → userId → roles → payload → context resource

Negate flag inverts the result: `result = condition.negate() != evaluatedResult`

## Rule Matching (`RuleEngine`)

Score-based matching algorithm in `RuleEngine.match()`:
- Module keyword match: +10 points
- Action keyword match: +10 points
- Role keyword match: +5 points per matching role
- Parameter value match: +15 points per matching parameter
- Threshold: score > 0 = matched

Returns `RuleMatchResult(ruleId, matched, score, details)` — an inner record.

## Conflict Resolution (`RuleEngine.resolveConflict`)

7 strategies for resolving conflicting rules:

| Strategy | Behavior |
|----------|----------|
| HIGHEST_PRIORITY_WINS | Lowest order value wins |
| LOWEST_PRIORITY_WINS | Highest order value wins |
| DENY_OVERRIDES | Any DENY decision wins |
| ALLOW_OVERRIDES | Any ALLOW decision wins |
| Most/Lest specific, REQUIRE_ALLOW | Default to first rule |

Decision-level conflict resolution (`PolicyDecisionServiceImpl.resolveConflict`):
- DENY_OVERRIDES: any DENY → DENY, otherwise ALLOW
- ALLOW_OVERRIDES: any ALLOW → ALLOW, otherwise DENY
- Default: REVIEW

## Decision Strategies (`PolicyDecisionServiceImpl.decide`)

- Empty evaluations → ALLOW
- Any DENY evaluation → DENY
- Any CHALLENGE evaluation → CHALLENGE
- Any REVIEW evaluation → REVIEW
- Otherwise → ALLOW

## Policy Filtering (`RuleEngine.filterApplicable`)

- Only active policies with ACTIVE status
- Module must match or be null/blank
- Sorted by priority descending

## Policy Lifecycle (`PolicyLifecycleManagerImpl`)

Valid state transitions:
- DRAFT → ACTIVE, DRAFT → ARCHIVED
- ACTIVE → INACTIVE, ACTIVE → ARCHIVED
- INACTIVE → ACTIVE, INACTIVE → ARCHIVED
- ARCHIVED → DEPRECATED

Version management with sequential version numbers.

## Expression Compilation (`PolicyCompilerImpl`)

Current implementation is a pass-through stub:
- `compile()` sets language="simple" and compiled=true
- `validate()` checks non-null/blank
- `parse()` returns uncompiled expression
- No actual expression language parsing or bytecode compilation
