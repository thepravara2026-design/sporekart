# Compliance Framework Architecture

## Multi-Layer Design

```
┌──────────────────────────────────────────────────────────┐
│                     Interfaces Layer                      │
│  ComplianceController (REST)                             │
│  /api/v1/compliance/*                                    │
├──────────────────────────────────────────────────────────┤
│                    Application Layer                      │
│  ComplianceEngineImpl          ComplianceRegistryImpl     │
│  ComplianceAssessmentService   ComplianceValidatorImpl    │
│  ComplianceEvidenceService     ComplianceReportingImpl    │
│  ComplianceAuditServiceImpl    ComplianceMetricsImpl      │
│  ComplianceHealthServiceImpl   ComplianceConfigImpl       │
├──────────────────────────────────────────────────────────┤
│                      Engine Layer                         │
│  RuleEvaluationEngine   CompliancePipeline                │
│  ComplianceResult       ComplianceRequest                 │
├──────────────────────────────────────────────────────────┤
│                      Domain Layer                         │
│  8 Enums · 13 Records                                     │
│  ComplianceFramework · ComplianceRule · ComplianceControl  │
│  ComplianceAssessment · ComplianceEvidence                │
│  ComplianceViolation · ComplianceFinding                  │
│  ComplianceReport · ComplianceException                   │
│  ComplianceAudit · ComplianceMetadata                     │
│  ComplianceRequirement · ComplianceScope                  │
├──────────────────────────────────────────────────────────┤
│                   Infrastructure Layer                     │
│  ┌────────────┐  ┌──────────────┐  ┌──────────────────┐  │
│  │ JPA (8)    │  │ Redis (5)    │  │ Kafka (8 events) │  │
│  │ + 8 repos  │  │ namespaces   │  │ compliance-events│  │
│  └────────────┘  └──────────────┘  └──────────────────┘  │
│  ┌────────────────┐  ┌────────────────────────────────┐   │
│  │ Monitoring     │  │ Security (CMP_4xx exceptions)  │   │
│  │ (Micrometer)   │  │ RBAC with compliance perms     │   │
│  └────────────────┘  └────────────────────────────────┘   │
├──────────────────────────────────────────────────────────┤
│                      Config Layer                         │
│  ComplianceConfig (@ConfigurationProperties)              │
│  Cache TTLs · Framework Defaults · Feature Flags          │
└──────────────────────────────────────────────────────────┘
```

## Compliance Pipeline

```
Governed Request
       │
       ▼
Applicable Compliance Rules
       │
       ▼
Evidence Collection
       │
       ▼
Compliance Validation
       │
       ▼
Compliance Result
       │
       ├── COMPLIANT ──► Compliance Report ──► Audit ──► Metrics ──► Continue
       │
       └── NON_COMPLIANT ──► Violation Detection
                                    │
                                    ├── Exception Allowed ──► Exception Flow
                                    │
                                    └── Blocked ──► Reject
                                                │
                                                ▼
                                          Audit + Metrics
```

## Supported Frameworks

| Framework | Type | Evaluation Method | Status |
|-----------|------|-------------------|--------|
| Internal AI Governance | Internal | Rule-based validation | Implemented |
| Responsible AI | Internal | Principle-based checks | Implemented |
| GDPR | External | Architectural mapping | Stub |
| ISO/IEC 42001 | External | Architectural mapping | Stub |
| ISO 27001 | External | Control mapping | Stub |
| SOC 2 | External | Trust service criteria | Stub |

## RBAC with Compliance Permissions

| Role | Permissions |
|------|-------------|
| AI_ADMINISTRATOR | Full CRUD on all compliance resources |
| AI_COMPLIANCE_OFFICER | Manage frameworks, rules, assessments, exceptions |
| AI_AUDITOR | Read-only access + audit export |
| AI_OPERATOR | Submit validation requests, view own results |
| AI_VIEWER | Read-only access to dashboards and reports |

### Compliance Exception Codes

| Code | Description |
|------|-------------|
| CMP_400 | Invalid compliance request |
| CMP_401 | Unauthorized compliance operation |
| CMP_403 | Forbidden — insufficient permissions |
| CMP_404 | Compliance resource not found |
| CMP_409 | Conflict — invalid state transition |
| CMP_422 | Unprocessable — validation rule failure |
| CMP_429 | Rate limit exceeded |
| CMP_500 | Internal compliance engine error |

## Integration Points

- **Governance Platform** — Resolves applicable policies for compliance rules
- **Policy Engine** — Policy decisions feed into compliance evaluation
- **Decision Engine** — Compliance results inform decision outcomes
- **Approval Platform** — Compliance exceptions routed for approval
- **Prompt Platform** — Prompt templates validated against AI governance rules
- **Knowledge Platform** — Document compliance status tracked
- **Monitoring Platform** — Compliance metrics exported to Grafana/Prometheus
