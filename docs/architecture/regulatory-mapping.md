# Regulatory Framework Mapping

## Overview

The Compliance Framework provides architectural mappings for six regulatory frameworks. Internal frameworks (AI Governance, Responsible AI) have direct rule-based validation, while external frameworks (GDPR, ISO 42001, ISO 27001, SOC 2) use architectural mapping stubs that define the evaluation structure without full implementation.

## Internal AI Governance

**Status:** Implemented (rule-based validation)

| Governance Principle | Compliance Mapping | Evaluation |
|---------------------|--------------------|------------|
| AI Usage Policy | ComplianceRule with scope=GLOBAL | Match provider + model against allowed list |
| Data Handling Policy | ComplianceRule with scope=WORKFLOW | Verify data classification level |
| Model Approval Policy | ComplianceRule with scope=PROVIDER | Check model is approved for use case |
| Deployment Policy | ComplianceRule with scope=MODULE | Validate deployment environment |
| Monitoring Policy | ComplianceRule with scope=GLOBAL | Confirm monitoring is enabled |

## Responsible AI

**Status:** Implemented (principle-based checks)

| Principle | Compliance Control | Evaluation |
|-----------|-------------------|------------|
| Fairness | ComplianceControl (DETECTIVE) | Check for bias in prompt/content (stub) |
| Transparency | ComplianceControl (DIRECTIVE) | Ensure response includes AI disclosure |
| Accountability | ComplianceControl (PREVENTIVE) | Log all AI decisions with audit trail |
| Reliability | ComplianceControl (CORRECTIVE) | Validate model response confidence |
| Privacy | ComplianceControl (PREVENTIVE) | Verify PII not exposed in prompts/content |
| Inclusivity | ComplianceControl (DIRECTIVE) | Check language/accessibility compliance |

## GDPR (General Data Protection Regulation)

**Status:** Architectural mapping (stub)

| GDPR Article | Requirement | Compliance Framework Mapping |
|-------------|-------------|----------------------------|
| Art. 5 | Data processing principles | `ComplianceControl` for purpose limitation, data minimization |
| Art. 6 | Lawful basis for processing | `ComplianceRule` requiring consent or legitimate interest check |
| Art. 7 | Consent management | `ComplianceEvidence` collection for consent records |
| Art. 13-14 | Information obligations | Transparency controls in `ComplianceAssessment` |
| Art. 15 | Right of access | Data retrieval controls mapped to Conversation/Knowledge |
| Art. 16 | Right to rectification | Data correction workflow in governance |
| Art. 17 | Right to erasure | Deletion workflow integration stub |
| Art. 20 | Right to data portability | Export format compliance control |
| Art. 22 | Automated decision-making | Human-in-the-loop requirement via Approval Platform |
| Art. 25 | Data protection by design | Default privacy controls in framework configuration |
| Art. 32 | Security of processing | ISO 27001 controls referenced |
| Art. 35 | Data protection impact assessment | DPIA workflow stub |

## ISO/IEC 42001 (AI Management System)

**Status:** Architectural mapping (stub)

| Clause | Requirement | Compliance Framework Mapping |
|--------|-------------|----------------------------|
| 5.2 | AI policy | `ComplianceFramework` with AI governance rules |
| 6.1 | Risk assessment | `RiskLevel` classification in assessment |
| 6.2 | AI objectives | Metrics tracked in `ComplianceMetricsService` |
| 7.5 | Documented information | `ComplianceReport` and `ComplianceAudit` records |
| 8.1 | Operational planning | `CompliancePipeline` orchestration |
| 8.2 | AI system development | Controls in development phase assessment |
| 8.3 | AI system deployment | Pre-deployment validation gate |
| 9.1 | Monitoring and measurement | `ComplianceMonitoringService` metrics |
| 9.2 | Internal audit | `ComplianceAuditService` immutable logs |
| 10.1 | Nonconformity and corrective action | Violation → Exception workflow |

### AI System Risk Categories (ISO 42001)

| Risk Category | Criteria | Compliance Scope |
|---------------|----------|-----------------|
| Minimal | No significant impact on individuals | Scope=GLOBAL, RiskLevel=LOW |
| Limited | Interaction without significant decisions | Scope=WORKFLOW, RiskLevel=MEDIUM |
| High | Decisions affecting legal/safety rights | Scope=MODULE, RiskLevel=HIGH |
| Unacceptable | Prohibited practices (social scoring, etc.) | RiskLevel=CRITICAL (immediate block) |

## ISO 27001 (Information Security)

**Status:** Architectural mapping (stub)

| Annex A Control | Domain | Compliance Mapping |
|-----------------|--------|-------------------|
| A.5 | Information security policies | `ComplianceControl` (DIRECTIVE) — policy adherence |
| A.6 | Organization of information security | Role-based access via Governance RBAC |
| A.7 | Human resource security | User role verification in assessment |
| A.8 | Asset management | Resource classification in scope |
| A.9 | Access control | Permission check via `ComplianceEvidence` |
| A.10 | Cryptography | TLS/data-at-rest encryption verification stub |
| A.12 | Operations security | Provider security configuration check |
| A.13 | Communications security | Network isolation verification stub |
| A.14 | System acquisition, development | AI system deployment gate |
| A.16 | Incident management | `ComplianceViolation` → escalation workflow |
| A.17 | Business continuity | Provider failover compliance check |
| A.18 | Compliance | Meta-compliance — framework self-audit |

## SOC 2 (Trust Service Criteria)

**Status:** Architectural mapping (stub)

| Criterion | Category | Compliance Mapping |
|-----------|----------|-------------------|
| CC1 | Security — Control Environment | Governance policy framework |
| CC2 | Security — Communication | Transparency controls, audit notifications |
| CC3 | Security — Risk Assessment | `RiskLevel` evaluation in pipeline |
| CC4 | Security — Monitoring | `ComplianceMonitoringService` metrics |
| CC5 | Security — Control Activities | `ComplianceRule` enforcement |
| CC6 | Security — Logical/Physical Access | RBAC + permission verification |
| CC7 | Security — System Operations | Pipeline reliability controls |
| CC8 | Security — Change Management | Version control in compliance audit |
| CC9 | Security — Risk Mitigation | Exception management workflow |
| A1 | Availability | Health check + monitoring controls |
| C1 | Confidentiality | Data classification + encryption checks |
| PI1 | Processing Integrity | Evidence verification + audit trail |
| PI2 | Processing Integrity — Accuracy | Output validation controls (stub) |

## Future Regional Compliance (Adapter Pattern)

```plaintext
┌───────────────────────────────────────────────┐
│            ComplianceRegistry                  │
│  registerFramework(ComplianceFramework)        │
│  getRules(FrameworkType, Scope)                │
│  getControls(FrameworkType, Region)            │
└───────────────────────────────────────────────┘
        │
        ▼
┌───────────────────────────────────────────────┐
│            ComplianceAdapter (Interface)       │
│  + resolveRules(context): List<ComplianceRule> │
│  + collectEvidence(context): List<Evidence>    │
│  + evaluate(rule, context): EvaluationResult   │
│  + getFrameworkInfo(): FrameworkMetadata       │
└───────────────────────────────────────────────┘
        │
        ├── InternalGovernanceAdapter
        ├── ResponsibleAIAdapter
        ├── GDPRAdapter (regional)
        ├── ISO42001Adapter
        ├── ISO27001Adapter
        ├── SOC2Adapter
        ├── CCPAAdapter (future)
        ├── LGPDAdapter (future)
        ├── PIPLAdapter (future)
        └── [Regional]Adapter (future)
```

### Adding a New Framework

1. Define `ComplianceFrameworkType` enum value
2. Implement `ComplianceAdapter` interface
3. Register via `ComplianceRegistry.registerFramework()`
4. Define compliance rules and controls
5. Map to pipeline evaluation steps
