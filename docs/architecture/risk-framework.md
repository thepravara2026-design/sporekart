# Risk Framework Architecture

## Multi-Layer Design

```
┌──────────────────────────────────────────────────────────┐
│                     Interfaces Layer                      │
│  RiskController (REST)                                   │
│  /api/v1/risk/*                                          │
├──────────────────────────────────────────────────────────┤
│                    Application Layer                      │
│  RiskEngineImpl                 RiskAssessmentImpl        │
│  RiskScoringServiceImpl         RiskClassificationImpl    │
│  TrustEngineImpl                TrustScoreServiceImpl     │
│  ConfidenceCalculatorImpl       RiskRecommendationImpl    │
│  RiskAuditServiceImpl           RiskMetricsImpl           │
│  RiskConfigurationServiceImpl                            │
├──────────────────────────────────────────────────────────┤
│                      Engine Layer                         │
│  TrustScoreCalculator   ConfidenceCalculatorEngine        │
│  RiskResult                                              │
├──────────────────────────────────────────────────────────┤
│                      Domain Layer                         │
│  6 Enums · 14 Records                                     │
│  RiskAssessment · RiskScore · RiskFactor · RiskRule       │
│  RiskEvidence · RiskDecision · TrustAssessment            │
│  ConfidenceScore · RiskRecommendation · RiskAudit         │
│  RiskThreshold · RiskMetadata · RiskHistory               │
│  RiskAssessmentRequest                                    │
├──────────────────────────────────────────────────────────┤
│                   Infrastructure Layer                     │
│  ┌────────────┐  ┌──────────────┐  ┌──────────────────┐  │
│  │ JPA (8)    │  │ Redis (5)    │  │ Kafka (7 events) │  │
│  │ + 8 repos  │  │ namespaces   │  │ risk-events      │  │
│  └────────────┘  └──────────────┘  └──────────────────┘  │
│  ┌────────────────┐  ┌────────────────────────────────┐   │
│  │ Monitoring     │  │ Security (RSK_4xx exceptions)  │   │
│  │ (Micrometer)   │  │ RBAC with risk perms           │   │
│  └────────────────┘  └────────────────────────────────┘   │
├──────────────────────────────────────────────────────────┤
│                      Config Layer                         │
│  RiskConfig (@ConfigurationProperties)                    │
│  Cache TTLs · Threshold Config · Feature Flags            │
└──────────────────────────────────────────────────────────┘
```

## Risk Pipeline

```
Governed Request
       │
       ▼
Collect Metadata
       │
       ▼
Identify Risk Factors
       │
       ▼
Calculate Risk Score (0-100)
       │
       ▼
Determine Risk Level
       │
       ▼
Calculate Trust Score (0-100)
       │
       ▼
Calculate Confidence (0-100)
       │
       ▼
Generate Recommendation
       │
       ▼
Audit
       │
       ▼
Metrics
       │
       ▼
Continue Decision Pipeline
```

## Risk Scoring

### Weighted Factor Model

Each risk factor contributes to the overall risk score based on its category weight and severity:

```
RiskScore = Σ(factorScore_i × factorWeight_i) / Σ(factorWeight_i)
```

- Factor scores range 0-100
- Factor weights are configurable per category
- Seven categories: PROVIDER, PROMPT, KNOWLEDGE, COMPLIANCE, PERFORMANCE, SECURITY, OPERATIONAL

### Configurable Thresholds

```yaml
risk:
  thresholds:
    low-max: 20
    medium-max: 40
    high-max: 70
    critical-max: 100
```

## Risk Levels

| Level | Score Range | Description | Action |
|-------|-------------|-------------|--------|
| LOW | 0-20 | Acceptable risk | Allow with monitoring |
| MEDIUM | 21-40 | Moderate risk | Review required |
| HIGH | 41-70 | Significant risk | Escalate to admin |
| CRITICAL | 71-100 | Unacceptable risk | Block immediately |

## RBAC with Risk Authorization

| Role | Permissions |
|------|-------------|
| AI_ADMINISTRATOR | Full CRUD on all risk resources, threshold configuration |
| AI_RISK_OFFICER | Manage risk rules, thresholds, review assessments |
| AI_AUDITOR | Read-only access + audit export |
| AI_OPERATOR | Submit assessments, view own results |
| AI_VIEWER | Read-only access to dashboards and reports |

### Risk Exception Codes

| Code | Description |
|------|-------------|
| RSK_400 | Invalid risk request |
| RSK_401 | Unauthorized risk operation |
| RSK_403 | Forbidden — insufficient permissions |
| RSK_404 | Risk resource not found |
| RSK_409 | Conflict — invalid state transition |
| RSK_422 | Unprocessable — risk evaluation failure |
| RSK_429 | Rate limit exceeded |
| RSK_500 | Internal risk engine error |
