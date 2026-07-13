# Governance Analytics Security

## RBAC Model

| Role | Analytics Permissions |
|------|-----------------------|
| AI_ADMINISTRATOR | Full CRUD on all analytics resources, dashboard/report/export management, KPI configuration, schedule management |
| AI_COMPLIANCE_OFFICER | View all analytics data; manage compliance reports, audit exports |
| AI_AUDITOR | Read-only access to all analytics, reports, KPIs, dashboards, exports |
| AI_OPERATOR | View own-scope analytics; generate reports, export data, manage personal dashboards |
| AI_VIEWER | Read-only access to dashboards, published reports, KPI overview |

### Permission Verification Points

| Operation | Required Role | Endpoint |
|-----------|---------------|----------|
| Collect metric | AI_OPERATOR or higher | POST /api/v1/governance/analytics/metrics |
| Query metrics | AI_VIEWER or higher | GET /api/v1/governance/analytics/metrics |
| Aggregate metrics | AI_OPERATOR or higher | POST /api/v1/governance/analytics/aggregate |
| List KPIs | AI_VIEWER or higher | GET /api/v1/governance/analytics/kpis |
| Calculate KPIs | AI_OPERATOR or higher | POST /api/v1/governance/analytics/kpis/calculate |
| Configure KPIs | AI_ADMINISTRATOR | PUT /api/v1/governance/analytics/kpis/{id} |
| Analyze trends | AI_VIEWER or higher | GET /api/v1/governance/analytics/trends |
| Generate report | AI_OPERATOR or higher | POST /api/v1/governance/analytics/reports |
| List reports | AI_VIEWER or higher | GET /api/v1/governance/analytics/reports |
| Get report details | AI_VIEWER or higher | GET /api/v1/governance/analytics/reports/{id} |
| Export report | AI_OPERATOR or higher | POST /api/v1/governance/analytics/export |
| List exports | AI_VIEWER or higher | GET /api/v1/governance/analytics/exports |
| Download export | AI_OPERATOR or higher | GET /api/v1/governance/analytics/exports/{id}/download |
| View dashboard | AI_VIEWER or higher | GET /api/v1/governance/analytics/dashboard/{id} |
| Manage dashboards | AI_OPERATOR or higher | POST/PUT/DELETE /api/v1/governance/analytics/dashboard |
| Manage schedules | AI_ADMINISTRATOR | POST/PUT/DELETE /api/v1/governance/analytics/schedules |

## Dashboard Authorization

- **Public dashboards** — Visible to all authenticated users with AI_VIEWER or higher
- **Private dashboards** — Visible only to the owner and AI_ADMINISTRATOR
- **Role-based dashboards** — Visibility controlled by role assignments
- Widget-level authorization — Sensitive widgets may require elevated roles to view

### Dashboard Access Levels

| Level | Description | Default Role |
|-------|-------------|--------------|
| PUBLIC | All authenticated users | AI_VIEWER |
| RESTRICTED | Specific roles only | AI_OPERATOR |
| PRIVATE | Owner + AI_ADMINISTRATOR | Owner only |
| CONFIDENTIAL | AI_ADMINISTRATOR only | AI_ADMINISTRATOR |

## Report Authorization

- **Report generation** — Requires AI_OPERATOR or higher; generates within user's scope
- **Report access** — Reports inherit access level from report type:
  - EXECUTIVE_SUMMARY, GOVERNANCE_HEALTH, DAILY, WEEKLY, MONTHLY — AI_VIEWER or higher
  - POLICY, DECISION, APPROVAL, OPERATIONAL — AI_OPERATOR or higher
  - COMPLIANCE, RISK, TRUST, AUDIT_SUMMARY — AI_COMPLIANCE_OFFICER, AI_AUDITOR, or AI_ADMINISTRATOR
  - CUSTOM — Defined by creator
- **Scheduled reports** — Requires AI_ADMINISTRATOR to create/modify schedules
- **Report deletion** — AI_ADMINISTRATOR only (soft delete)

## Export Authorization

- **Export initiation** — Requires at least AI_OPERATOR; exports respect source report access level
- **Export download** — Requires AI_OPERATOR or higher for own exports; AI_ADMINISTRATOR for all
- **Export format restrictions** — AI_VIEWER cannot export; AI_OPERATOR exports JSON/CSV; AI_ADMINISTRATOR exports any format
- **Export retention** — Exports retained per governance policy; AI_AUDITOR can request extended retention

## Immutable Audit Trail

All analytics operations are recorded in the immutable audit trail (governance audit system, not a separate table):

| Audit Event | Description |
|-------------|-------------|
| METRIC_COLLECTED | Metric data point recorded |
| METRICS_AGGREGATED | Metrics aggregated for time window |
| KPI_CALCULATED | KPI calculated with result |
| KPI_CONFIGURED | KPI target or threshold updated |
| TREND_ANALYZED | Trend analysis generated |
| REPORT_GENERATED | Report generation completed |
| REPORT_SCHEDULED | Report schedule created/updated |
| REPORT_DELETED | Report soft-deleted |
| EXPORT_INITIATED | Export job started |
| EXPORT_COMPLETED | Export job completed |
| EXPORT_DOWNLOADED | Export file downloaded |
| DASHBOARD_CREATED | Dashboard created |
| DASHBOARD_UPDATED | Dashboard configuration changed |
| DASHBOARD_DELETED | Dashboard soft-deleted |

### Audit Properties

- **Immutable** — No UPDATE or DELETE on audit records (enforced at database level)
- **Timestamped** — Millisecond precision timestamps
- **Actor-tracked** — User or system principal recorded for every event
- **Context-rich** — Full event details captured as JSON

## Analytics Exception Codes (ANL_4xx)

| Code | HTTP Status | Description |
|------|-------------|-------------|
| ANL_400 | 400 | Invalid analytics request |
| ANL_401 | 401 | Authentication required |
| ANL_403 | 403 | Insufficient analytics permissions |
| ANL_404 | 404 | Analytics resource not found |
| ANL_409 | 409 | Invalid state transition |
| ANL_422 | 422 | Analytics pipeline error |
| ANL_429 | 429 | Rate limit exceeded |
| ANL_500 | 500 | Internal analytics engine error |

## Configuration

```yaml
governance:
  analytics:
    security:
      audit-immutable: true
      require-auth: true
      dashboard-default-access: PUBLIC
      report-access:
        EXECUTIVE_SUMMARY: AI_VIEWER
        COMPLIANCE: AI_COMPLIANCE_OFFICER
        RISK: AI_COMPLIANCE_OFFICER
        AUDIT_SUMMARY: AI_AUDITOR
      export:
        min-role: AI_OPERATOR
        admin-role: AI_ADMINISTRATOR
```

## Security Enforcement Points

1. **API Gateway** — All `/api/v1/governance/analytics/*` requests authenticated
2. **Controller** — Method-level `@PreAuthorize` annotations on all analytics endpoints
3. **Service Layer** — Role and permission checks in analytics application services
4. **Repository Layer** — Soft-delete aware queries prevent data loss
5. **Database Layer** — Audit trail immutability enforced via triggers
6. **Configuration** — KPI and schedule mutations gated by role checks
