# Risk Pipeline

## Pipeline Steps

```
Governed Request → Collect Metadata → Identify Risk Factors → Calculate Risk Score → Determine Risk Level → Calculate Trust Score → Calculate Confidence → Generate Recommendation → Audit → Metrics → Continue Decision Pipeline
```

### 1. Governed Request

- Receive pre-governed request from Compliance framework
- Request includes: module, provider, prompt, knowledge context, user identity, roles
- Validate required fields are present for risk evaluation
- Reject if critical metadata is missing

### 2. Collect Metadata

- Gather request metadata:
  - Provider identity and capabilities
  - Prompt template and variable values
  - Knowledge sources and citations
  - Conversation context (if applicable)
  - User role and permissions
  - Environment and region
  - Workflow identifier
- Enrich with system metadata:
  - Current provider health status
  - Historical accuracy statistics
  - Policy compliance cache
- Store metadata in `risk_metadata` for evidence trail

### 3. Identify Risk Factors

- Analyze metadata against known risk patterns:
  - **Provider Risk** — Unknown provider, degraded health, unsupported capability
  - **Prompt Risk** — Missing variables, template mismatch, injection patterns
  - **Knowledge Risk** — Stale documents, unverified sources, category mismatch
  - **Compliance Risk** — Active violations, restricted scope, missing attestations
  - **Performance Risk** — High latency anticipated, large payload, concurrent usage
  - **Security Risk** — Elevated privileges, sensitive data exposure, cross-tenant access
  - **Operational Risk** — Maintenance window, degraded system, rate limit proximity
- Each factor assigned a category, initial score, and supporting evidence
- Publish `RiskFactorIdentified` event for each factor

### 4. Calculate Risk Score

```
RiskScore = Σ(factorScore_i × factorWeight_i) / Σ(factorWeight_i)
```

- Weighted aggregation of all identified risk factors
- Category weights configurable via `RiskConfigurationService`
- Default weights:
  - PROVIDER: 20%, PROMPT: 15%, KNOWLEDGE: 15%, COMPLIANCE: 20%
  - PERFORMANCE: 10%, SECURITY: 15%, OPERATIONAL: 5%
- Score normalized to 0-100 range
- Breakdown available per category for transparency

### 5. Determine Risk Level

| Score Range | Risk Level | Action Required |
|-------------|------------|-----------------|
| 0-20 | LOW | Standard processing with monitoring |
| 21-40 | MEDIUM | Additional review recommended |
| 41-70 | HIGH | Escalation required, conditional processing |
| 71-100 | CRITICAL | Block or fallback immediately |

- Thresholds configurable via `RiskConfigurationService`
- Level determined by threshold comparison:
  ```java
  RiskLevel classify(int score, RiskThresholds thresholds) {
      if (score <= thresholds.lowMax())    return LOW;
      if (score <= thresholds.mediumMax()) return MEDIUM;
      if (score <= thresholds.highMax())   return HIGH;
      return CRITICAL;
  }
  ```

### 6. Calculate Trust Score

- Evaluate 9 trust factors (see Trust Scoring Framework):
  - Provider Reliability (15%), Knowledge Quality (15%), Semantic Confidence (12%)
  - Prompt Validation (12%), Historical Accuracy (12%), Policy Compliance (10%)
  - Workflow Success (8%), Context Completeness (8%), Output Validation (8%)
- Each factor scored 0-100 with supporting reason
- Overall trust score = weighted average (0-100)
- Publish `TrustEvaluated` event

### 7. Calculate Confidence

- Evaluate 6 confidence factors:
  - Knowledge Match (25%), Semantic Similarity (20%), Prompt Quality (18%)
  - Conversation Context (15%), Workflow Success (12%), Provider Metadata (10%)
- Each factor scored 0-100 with explanation
- Overall confidence = weighted average (0-100)
- Publish `ConfidenceCalculated` event

### 8. Generate Recommendation

Combine risk level, trust score, and confidence to generate recommendation:

| Risk Level | Trust | Confidence | Recommendation |
|------------|-------|------------|----------------|
| LOW | ≥ 70 | ≥ 70 | ALLOW |
| LOW | < 70 | ≥ 70 | ALLOW_WITH_MONITORING |
| MEDIUM | ≥ 70 | ≥ 70 | ALLOW_WITH_REVIEW |
| MEDIUM | < 70 | < 70 | REVIEW |
| HIGH | ≥ 70 | ≥ 70 | ESCALATE |
| HIGH | < 70 | < 70 | ESCALATE_OR_FALLBACK |
| CRITICAL | any | any | BLOCK |
| any | < 40 | any | FALLBACK |

- Recommendation includes: action type, reason summary, fallback suggestions, escalation path
- Publish `RecommendationGenerated` event

### 9. Audit

- Create immutable `RiskAudit` entry
- Include: assessment ID, request details, all scores (risk, trust, confidence), recommendation, evidence references, timestamp
- Append-only — no UPDATE or DELETE permitted
- Store in `risk_audit` table
- Publish audit-related events

### 10. Metrics

- Update risk counters per level, category, outcome
- Track: total assessments, pass/fail rates, average evaluation time, risk level distribution
- Track trust and confidence trends over time
- Expose via Micrometer: counters, timers, gauges

### 11. Continue Decision Pipeline

- Pass recommendation to Decision Engine
- Decision Engine evaluates recommendation alongside policies for final action
- If BLOCKED, return error to caller
- If ALLOWED, continue to next pipeline stage (Prompt → Knowledge → Semantic → Gateway → Provider)

## Pipeline Configuration

```yaml
risk:
  pipeline:
    fail-closed: true
    timeout: 10s
    max-factors: 50
    evidence-retention: 7d
    thresholds:
      low-max: 20
      medium-max: 40
      high-max: 70
    trust:
      provider-reliability-weight: 0.15
      knowledge-quality-weight: 0.15
      semantic-confidence-weight: 0.12
      prompt-validation-weight: 0.12
      historical-accuracy-weight: 0.12
      policy-compliance-weight: 0.10
      workflow-success-weight: 0.08
      context-completeness-weight: 0.08
      output-validation-weight: 0.08
    confidence:
      knowledge-match-weight: 0.25
      semantic-similarity-weight: 0.20
      prompt-quality-weight: 0.18
      conversation-context-weight: 0.15
      workflow-success-weight: 0.12
      provider-metadata-weight: 0.10
```
