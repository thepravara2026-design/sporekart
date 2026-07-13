# Reporting Platform Architecture

## Report Types (14)

| Type | ID | Description | Default Frequency | Audience |
|------|----|-------------|-------------------|----------|
| Executive Summary | EXECUTIVE_SUMMARY | High-level governance health with key KPIs | Daily | Executives, AI Governance Board |
| Governance Health | GOVERNANCE_HEALTH | Comprehensive governance platform status | Daily | AI Administrators, Operators |
| Policy Report | POLICY | Policy lifecycle, evaluation, violation stats | Weekly | AI Admins, Compliance Officers |
| Decision Report | DECISION | Decision distribution, outcomes, conflict trends | Weekly | AI Administrators, Operators |
| Approval Report | APPROVAL | Approval throughput, SLA compliance, bottlenecks | Weekly | AI Administrators, Reviewers |
| Compliance Report | COMPLIANCE | Compliance pass rates, violations, remediation | Monthly | Compliance Officers, Auditors |
| Risk Report | RISK | Risk distribution, levels, recommendations | Daily | Risk Officers, Administrators |
| Trust Report | TRUST | Trust scores, factor breakdown, trends | Weekly | AI Administrators, Viewers |
| Operational Report | OPERATIONAL | System performance, utilization, availability | Daily | Operators, Administrators |
| Audit Summary | AUDIT_SUMMARY | Audit event aggregation, patterns, anomalies | Monthly | Auditors, Compliance Officers |
| Daily Report | DAILY | Aggregated daily metrics across all domains | Daily | All roles |
| Weekly Report | WEEKLY | Weekly aggregation with trend comparisons | Weekly | All roles |
| Monthly Report | MONTHLY | Monthly aggregation with period comparisons | Monthly | All roles |
| Custom Report | CUSTOM | User-defined metric selection and period | On-demand | Any role with report permission |

## Report Generation Pipeline

```
                    ┌─────────────────────┐
                    │  Receive Parameters │
                    └──────────┬──────────┘
                               │
                    ┌──────────▼──────────┐
                    │   Collect Data      │
                    │  (metrics, KPIs,    │
                    │   trends, snapshots)│
                    └──────────┬──────────┘
                               │
                    ┌──────────▼──────────┐
                    │   Format Report     │
                    │  (apply template,   │
                    │   structure data)   │
                    └──────────┬──────────┘
                               │
                    ┌──────────▼──────────┐
                    │   Save Report       │
                    │  (persist metadata, │
                    │   store data)       │
                    └──────────┬──────────┘
                               │
                    ┌──────────▼──────────┐
                    │   Notify            │
                    │  (Kafka event,      │
                    │   callback, log)    │
                    └─────────────────────┘
```

### Pipeline Steps

1. **Receive Parameters** — Accept report type, time range, filters, format preferences, recipient list
2. **Collect Data** — Query aggregated metrics, KPI values, trend analysis, snapshot comparisons based on report type
3. **Format Report** — Apply report template (structure, sections, visualization data), transform data into requested format
4. **Save Report** — Persist report metadata (id, type, format, timestamp), store report data (inline or file reference), record version
5. **Notify** — Publish Kafka event (`ReportGenerated`), trigger email/Slack notification (future), log completion

## Export Framework

```
                    ┌─────────────────────┐
                    │  Export Request     │
                    │  (format, data,     │
                    │   compression)      │
                    └──────────┬──────────┘
                               │
                    ┌──────────▼──────────┐
                    │  Format Selection   │
                    └──────────┬──────────┘
                               │
              ┌────────────────┼────────────────┐
              ▼                ▼                ▼
     ┌──────────────┐  ┌──────────────┐  ┌──────────────┐
     │  JSON Export │  │  CSV Export  │  │ Excel Export │
     │  (native)    │  │  (native)    │  │  (stub)      │
     └──────────────┘  └──────────────┘  └──────────────┘
              │                │                │
              └────────────────┼────────────────┘
                               │
                    ┌──────────▼──────────┐
                    │     PDF Export      │
                    │      (stub)         │
                    └─────────────────────┘
```

### Export Formats

| Format | Status | Implementation | Notes |
|--------|--------|----------------|-------|
| JSON | Native | Jackson serialization of report data | Full fidelity, nested structure |
| CSV | Native | Apache Commons CSV | Flat structure, header row |
| Excel (XLSX) | Stub | Apache POI (placeholder) | Sheet structure, formatting pending |
| PDF | Stub | iText/Flying Saucer (placeholder) | Page layout, branding pending |

## Scheduled Reports

Scheduled reports are managed via the `governance_report_schedules` table:

| Feature | Description |
|---------|-------------|
| Frequency | DAILY, WEEKLY, MONTHLY, QUARTERLY, YEARLY, CUSTOM (cron expression) |
| Recipients | Email addresses (future: Slack webhooks, webhook URLs) |
| Format | Default output format for the schedule |
| Filters | Pre-configured filters applied at generation time |
| Active flag | Enable/disable schedule without deletion |
| Last run | Timestamp of last successful generation |
| Next run | Calculated next generation time |

### Schedule Resolution

1. System checks for active schedules at configured interval
2. For each due schedule, triggered generation follows the report pipeline
3. Generated report saved and export processed per schedule configuration
4. Schedule updated with last run timestamp and next run calculation

## Report Templates and Versioning

### Templates

Each report type has a configurable template defining:
- Report structure (sections, ordering)
- Metric inclusions (which KPIs, trends, charts)
- Visualization hints (chart types, color schemes)
- Header/footer content (logo, labels, timestamps)

### Versioning

- Each generated report records the report version (incremented on template changes)
- Report templates are versioned independently
- Historical reports remain accessible by their original version
- Template version changes apply to new reports only
