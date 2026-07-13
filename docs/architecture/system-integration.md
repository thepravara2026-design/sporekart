# System Integration Documentation

## Module Dependency Graph

```
governance (foundation) ──────────────────────────────────────────────────────────┐
    │                                                                            │
    ├──> policy ────────────────────────────────────────────────────────────────┐│
    │       │                                                                   ││
    │       ├──> decision ─────────────────────────────────────────────────────┐││
    │       │       │                                                         │││
    │       │       ├──> approval ────────────────────────────────────────────┐│││
    │       │       │       │                                                ││││
    │       │       │       ├──> compliance ─────────────────────────────────┐││││
    │       │       │       │       │                                       │││││
    │       │       │       │       ├──> risk ──────────────────────────────┐│││││
    │       │       │       │       │       │                              ││││││
    │       │       │       │       │       ├──> analytics ────────────────┐││││││
    │       │       │       │       │       │       │                     │││││││
    │       │       │       │       │       │       ├──> admin ───────────┐│││││││
    │       │       │       │       │       │       │       │            ││││││││
    │       │       │       │       │       │       │       ├──> automation││││││││
    │       │       │       │       │       │       │       │            ││││││││
    ▼       ▼       ▼       ▼       ▼       ▼       ▼       ▼            ▼▼▼▼▼▼▼▼
core (shared kernel — dependency-free foundation)
```

## REST API Integration Points

### Governance Foundation — `/api/v1/governance/*` (16 endpoints)
| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/governance/policies` | Create policy |
| GET | `/api/v1/governance/policies` | List policies |
| GET | `/api/v1/governance/policies/{id}` | Get policy |
| PUT | `/api/v1/governance/policies/{id}` | Update policy |
| DELETE | `/api/v1/governance/policies/{id}` | Delete policy |
| POST | `/api/v1/governance/policies/{id}/activate` | Activate policy |
| POST | `/api/v1/governance/policies/{id}/deactivate` | Deactivate policy |
| POST | `/api/v1/governance/config` | Create config entry |
| GET | `/api/v1/governance/config` | List config entries |
| GET | `/api/v1/governance/config/{key}` | Get config entry |
| PUT | `/api/v1/governance/config/{key}` | Update config entry |
| DELETE | `/api/v1/governance/config/{key}` | Delete config entry |
| POST | `/api/v1/governance/config/import` | Import config |
| GET | `/api/v1/governance/config/export` | Export config |
| GET | `/api/v1/governance/audit` | Query audit records |
| GET | `/api/v1/governance/compliance` | Check compliance |

### Policy Engine — `/api/v1/policies/*` (10 endpoints)
| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/policies/evaluate` | Evaluate policies |
| POST | `/api/v1/policies/evaluate/context` | Evaluate with context |
| POST | `/api/v1/policies/decide` | Determine decision |
| POST | `/api/v1/policies/decide/allowed` | Check if allowed |
| GET | `/api/v1/policies/{id}` | Get policy details |
| POST | `/api/v1/policies` | Create policy |
| PUT | `/api/v1/policies/{id}` | Update policy |
| DELETE | `/api/v1/policies/{id}` | Delete policy |
| POST | `/api/v1/policies/{id}/activate` | Activate policy |
| POST | `/api/v1/policies/{id}/deactivate` | Deactivate policy |

### Decision Engine — `/api/v1/decisions/*` (8 endpoints)
| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/decisions/evaluate` | Evaluate decision |
| POST | `/api/v1/decisions/evaluate/context` | Evaluate with context |
| GET | `/api/v1/decisions/{id}` | Get decision result |
| GET | `/api/v1/decisions/{id}/explanation` | Get decision explanation |
| GET | `/api/v1/decisions/history` | Get decision history |
| POST | `/api/v1/decisions/{id}/replay` | Replay decision |
| GET | `/api/v1/decisions/statistics` | Get decision statistics |
| GET | `/api/v1/decisions/health` | Get decision engine health |

### Approval Platform — `/api/v1/approvals/*` (12 endpoints)
| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/approvals` | Create approval request |
| GET | `/api/v1/approvals/{id}` | Get approval request |
| GET | `/api/v1/approvals` | List approval requests |
| PUT | `/api/v1/approvals/{id}` | Update approval request |
| POST | `/api/v1/approvals/{id}/submit` | Submit for approval |
| POST | `/api/v1/approvals/{id}/approve` | Approve request |
| POST | `/api/v1/approvals/{id}/reject` | Reject request |
| POST | `/api/v1/approvals/{id}/escalate` | Escalate request |
| POST | `/api/v1/approvals/{id}/delegate` | Delegate request |
| POST | `/api/v1/approvals/{id}/cancel` | Cancel request |
| GET | `/api/v1/approvals/{id}/history` | Get approval history |
| GET | `/api/v1/approvals/{id}/metrics` | Get approval metrics |

### Compliance Framework — `/api/v1/compliance/*` (9 endpoints)
| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/compliance/validate` | Validate compliance |
| POST | `/api/v1/compliance/assess` | Create assessment |
| GET | `/api/v1/compliance/assessments/{id}` | Get assessment |
| GET | `/api/v1/compliance/frameworks` | List frameworks |
| GET | `/api/v1/compliance/reports` | List compliance reports |
| GET | `/api/v1/compliance/reports/{id}` | Get compliance report |
| POST | `/api/v1/compliance/exception` | Request exception |
| PUT | `/api/v1/compliance/exception/{id}` | Update exception |
| GET | `/api/v1/compliance/violations` | List violations |

### Risk & Trust — `/api/v1/risk/*` (10 endpoints)
| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/risk/assess` | Create risk assessment |
| GET | `/api/v1/risk/assessments/{id}` | Get risk assessment |
| POST | `/api/v1/risk/evaluate` | Evaluate risk |
| POST | `/api/v1/risk/trust` | Calculate trust score |
| POST | `/api/v1/risk/confidence` | Calculate confidence |
| GET | `/api/v1/risk/assessments` | List assessments |
| GET | `/api/v1/risk/{id}/factors` | Get risk factors |
| GET | `/api/v1/risk/{id}/recommendations` | Get recommendations |
| GET | `/api/v1/risk/history` | Get risk history |
| POST | `/api/v1/risk/audit` | Record risk audit |

### Governance Analytics — `/api/v1/governance/analytics/*` (10 endpoints)
| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/governance/analytics/metrics` | Collect metric |
| GET | `/api/v1/governance/analytics/metrics` | Query metrics |
| GET | `/api/v1/governance/analytics/dashboard` | Get dashboard data |
| GET | `/api/v1/governance/analytics/kpis` | List KPIs |
| GET | `/api/v1/governance/analytics/kpis/{id}` | Get KPI details |
| POST | `/api/v1/governance/analytics/reports` | Generate report |
| GET | `/api/v1/governance/analytics/reports` | List reports |
| GET | `/api/v1/governance/analytics/reports/{id}` | Get report |
| GET | `/api/v1/governance/analytics/trends` | Get trend analysis |
| POST | `/api/v1/governance/analytics/export` | Export report |

### Administration Platform — `/api/v1/admin/*` (11 endpoints)
| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/admin/config` | Create configuration |
| GET | `/api/v1/admin/config` | List configurations |
| GET | `/api/v1/admin/config/{key}` | Get configuration |
| PUT | `/api/v1/admin/config/{key}` | Update configuration |
| DELETE | `/api/v1/admin/config/{key}` | Delete configuration |
| GET | `/api/v1/admin/features` | List feature flags |
| POST | `/api/v1/admin/features/{flag}/toggle` | Toggle feature flag |
| GET | `/api/v1/admin/modules` | List governance modules |
| POST | `/api/v1/admin/modules/{name}/{action}` | Enable/disable module |
| GET | `/api/v1/admin/environments` | List environments |
| POST | `/api/v1/admin/maintenance` | Toggle maintenance mode |

### Automation & Lifecycle — `/api/v1/automation/*` + `/api/v1/governance/lifecycle/*` (10 endpoints)
| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/governance/lifecycle/transition` | Execute lifecycle transition |
| GET | `/api/v1/governance/lifecycle/state/{type}/{id}` | Get lifecycle state |
| GET | `/api/v1/governance/lifecycle/history/{type}/{id}` | Get transition history |
| POST | `/api/v1/automation/jobs` | Create automation job |
| GET | `/api/v1/automation/jobs` | List automation jobs |
| GET | `/api/v1/automation/jobs/{jobId}` | Get job details |
| PUT | `/api/v1/automation/jobs/{jobId}` | Update automation job |
| DELETE | `/api/v1/automation/jobs/{jobId}` | Delete automation job |
| POST | `/api/v1/automation/jobs/{jobId}/execute` | Execute job |
| GET | `/api/v1/automation/scheduled-tasks` | List scheduled tasks |

**Total REST Endpoints: 96**

## Kafka Event Integration (9 Topics)

| Topic | Partitions | Replicas | Event Types | Producer Module | Consumer Modules |
|-------|-----------|----------|-------------|-----------------|------------------|
| `governance-events` | 3 | 1 | 12 | Governance Foundation | Policy, Analytics, Admin, Automation |
| `policy-events` | 3 | 1 | 8 | Policy Engine | Decision, Analytics, Admin, Automation |
| `decision-events` | 3 | 1 | 8 | Decision Engine | Approval, Analytics, Admin, Automation |
| `approval-events` | 3 | 1 | 10 | Approval Platform | Compliance, Analytics, Admin, Automation |
| `compliance-events` | 3 | 1 | 8 | Compliance Framework | Risk, Analytics, Admin, Automation |
| `risk-events` | 3 | 1 | 7 | Risk & Trust | Analytics, Admin, Automation |
| `analytics-events` | 3 | 1 | 7 | Governance Analytics | Admin, Dashboard WebSocket |
| `admin-events` | 3 | 1 | 6 | Administration | Automation, Monitoring |
| `automation-events` | 3 | 1 | 9 | Automation & Lifecycle | Admin, Monitoring |

**Total: 9 topics, 75 event types**

## Redis Cache Integration (45 Namespaces)

| Module | Cache Namespaces | TTL |
|--------|-----------------|-----|
| Governance Foundation | `governance:policies:`, `governance:config:`, `governance:permissions:`, `governance:quotas:`, `governance:audit:` | 5-60 min |
| Policy Engine | `policy:registry:`, `policy:compiled:`, `policy:metadata:`, `policy:evaluation:`, `policy:health:` | 60-600s |
| Decision Engine | `decision:result:`, `decision:metadata:`, `decision:registry:`, `decision:stats:`, `decision:explanation:` | 120-300s |
| Approval Platform | `approval:pending:`, `approval:assignment:`, `approval:config:`, `approval:workflow:`, `approval:statistics:` | 60-300s |
| Compliance Framework | `compliance:framework:`, `compliance:rules:`, `compliance:assessment:`, `compliance:report:`, `compliance:config:` | 180-600s |
| Risk & Trust | `risk:assessment:`, `risk:factors:`, `risk:trust:`, `risk:confidence:`, `risk:config:` | 180-300s |
| Governance Analytics | `analytics:metrics:`, `analytics:dashboard:`, `analytics:kpis:`, `analytics:trends:`, `analytics:reports:` | 120-600s |
| Administration | `admin:config:`, `admin:features:`, `admin:modules:`, `admin:environments:`, `admin:maintenance:` | 60-300s |
| Automation & Lifecycle | `automation:job:`, `automation:schedule:`, `automation:lifecycle:`, `automation:workflow:`, `automation:lock:` | 60-600s |

## Database Integration (Flyway Migrations)

| Migration | Module | Tables | Indexes | Description |
|-----------|--------|--------|---------|-------------|
| V20 | Governance Foundation | 6 | 22 | Governance platform foundation |
| V21 | Policy Engine | 7 | 24 | Policy rules and evaluation |
| V22 | Decision Engine | 6 | 18 | Decision records and audit |
| V23 | Approval Platform | 9 | 24 | Approval workflows and assignments |
| V24 | Compliance Framework | 8 | 26 | Compliance assessments and reports |
| V25 | Risk & Trust | 8 | 18 | Risk and trust scoring |
| V26 | Governance Analytics | 7 | 9 | Metrics, KPIs, dashboards |
| V27 | Administration | 7 | 11 | Admin config and feature flags |
| V28 | Automation & Lifecycle | 9 | 21 | Automation jobs and lifecycle |

**Total: 9 migrations, 67 tables, 173 indexes**

## Security Integration

### SecurityConfig Permitted Paths
```
/api/v1/governance/**
/api/v1/policies/**
/api/v1/decisions/**
/api/v1/approvals/**
/api/v1/compliance/**
/api/v1/risk/**
/api/v1/governance/analytics/**
/api/v1/admin/**
/api/v1/automation/**
/api/v1/governance/lifecycle/**
```

### Exception Error Code Ranges
| Module | Prefix | Range |
|--------|--------|-------|
| Governance Foundation | GOV | 400-500 |
| Policy Engine | POL | 400-500 |
| Decision Engine | DEC | 400-500 |
| Approval Platform | APR | 400-500 |
| Compliance Framework | CMP | 400-500 |
| Risk & Trust | RSK | 400-500 |
| Governance Analytics | ANL | 400-500 |
| Administration | ADM | 400-500 |
| Automation | AUT | 400-500 |

## Configuration Integration (application.yml)

All 9 governance modules configured under `sporekart.ai.governance` prefix:
```yaml
sporekart:
  ai:
    governance:
      enabled: true
      caching: true
      audit: true
      monitoring: true
      policy:
        enabled: true
        caching: true
        evaluation: true
      decision:
        enabled: true
        caching: true
      approval:
        enabled: true
        caching: true
      compliance:
        enabled: true
        validation: true
      risk:
        enabled: true
        classification: true
      analytics:
        enabled: true
      admin:
        enabled: true
      automation:
        enabled: true
        scheduler: true
```
