# Compliance Pipeline

## Pipeline Steps

```
Rule Resolution → Assessment Creation → Evidence Collection → Rule Evaluation → Report Generation → Audit Recording → Metrics Update → Event Publishing
```

### 1. Resolve Rules

- Receive governed request with framework type, scope, and resource context
- Query `ComplianceRegistry` for applicable rules matching framework + scope
- Filter rules by active status, priority ordering
- Resolve framework-specific rule overrides from configuration
- Return ordered list of `ComplianceRule` objects

### 2. Create Assessment

- Initialize `ComplianceAssessment` with PENDING status
- Capture request metadata: userId, module, resource, framework
- Calculate risk level based on resource sensitivity and framework requirements
- Persist assessment to `compliance_assessments` table
- Publish `ComplianceValidationStarted` event

### 3. Collect Evidence

- Invoke `ComplianceEvidenceService.collectEvidence()` for each rule
- Evidence sources:
  - Request payload and metadata
  - Governance policy configurations
  - Prompt template metadata
  - Knowledge document classifications
  - Conversation history (anonymized)
  - Provider capability information
  - User role and permission context
- Verify evidence completeness and integrity
- Store evidence in `compliance_evidence` table with hash verification

### 4. Evaluate Rules

Each rule is evaluated by `RuleEvaluationEngine`:

```java
// Conceptual evaluation flow
for (ComplianceRule rule : applicableRules) {
    ComplianceContext context = buildContext(request, evidence, rule);
    boolean passed = ruleEvaluationEngine.evaluate(rule, context);

    if (passed) {
        findings.add(ComplianceFinding.compliant(rule));
    } else {
        ViolationSeverity severity = determineSeverity(rule, context);
        findings.add(ComplianceFinding.violation(rule, severity));
        violations.add(new ComplianceViolation(rule, severity, context));
    }
}
```

#### RuleEvaluationEngine

- Expression-based evaluation against the compliance context
- Each rule contains an expression (e.g., `provider.region in ["EU", "EEA"]` for GDPR)
- Engine evaluates expressions using the collected evidence as context
- Unsupported expressions return NON_COMPLIANT by default (fail-closed)
- Expressions are framework-specific and validated at rule creation

#### Violation Detection & Severity Classification

| Severity | Criteria | Action |
|----------|----------|--------|
| INFO | Advisory recommendation | Log + report only |
| WARNING | Minor non-compliance | Report + notification |
| ERROR | Significant non-compliance | Block unless exception granted |
| CRITICAL | Regulatory or legal risk | Block immediately + escalate |

### 5. Generate Report

- Aggregate all findings into `ComplianceReport`
- Include: framework, rules evaluated, evidence references, violations, overall status
- Generate human-readable summary with severity breakdown
- Attach evidence references with integrity hashes
- Persist to `compliance_reports` table
- Publish `ComplianceReportGenerated` event

### 6. Record Audit

- Create immutable `ComplianceAudit` entry
- Include: request details, assessment ID, report reference, timestamp
- Append-only — no UPDATE or DELETE permitted at DB level
- Store in `compliance_audit` table
- Publish `ComplianceAuditRecorded` event

### 7. Update Metrics

- Update compliance counters per framework, scope, severity
- Track: total validations, pass/fail rates, average evaluation time, violation trends
- Expose via Micrometer: counters, timers, gauges

### 8. Publish Events

- `ComplianceValidationCompleted` — validation outcome
- `ComplianceViolationDetected` — each violation (may batch)
- `ComplianceExceptionRequested/Resolved` — exception lifecycle events

## Exception Management Flow

```
Violation Detected
       │
       ▼
Exception Eligible?
    ├── No  ──► Reject Request
    │
    └── Yes ──► Create Exception Request (POST /api/v1/compliance/exceptions)
                     │
                     ▼
              ExceptionStatus: REQUESTED
                     │
                     ▼
              Review & Decision
                ├── Approved ──► Set status APPROVED + expiry
                │                   │
                │                   ▼
                │              Resume Pipeline (COMPLIANT with exemption)
                │
                ├── Rejected ──► Set status REJECTED → Block
                │
                └── Expired  ──► Auto-revoke on expiry
```

### Exception Request Fields

| Field | Description |
|-------|-------------|
| ruleId | The compliance rule being exempted |
| reason | Business justification for exception |
| requestedBy | User requesting the exception |
| approvedBy | Compliance officer who approves |
| expiresAt | Optional expiration timestamp |
| scope | Scope of the exception |
| metadata | Additional context (ticket ref, etc.) |

## Pipeline Configuration

```yaml
compliance:
  pipeline:
    fail-closed: true                  # Deny on evaluation error
    max-evidence-size: 10MB            # Per-evidence limit
    evaluation-timeout: 30s            # Per-rule timeout
    evidence-verification: true        # Hash verification on collect
    auto-approve-exceptions: false     # Require human approval
    default-exception-expiry: 7d       # Default exception TTL
```
