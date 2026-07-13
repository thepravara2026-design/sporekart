# Governance Analytics Architecture

## Multi-Layer Architecture

```
┌──────────────────────────────────────────────────────┐
│                   Interfaces Layer                    │
│  REST Controller  │  WebSocket  │  GraphQL (future)   │
├──────────────────────────────────────────────────────┤
│                 Application Layer                     │
│  AnalyticsService  │  ReportingService  │  KPIService │
│  DashboardService  │  ExportService     │  AuditSvc   │
├──────────────────────────────────────────────────────┤
│                    Engine Layer                       │
│  KpiCalculator  │  MetricsAggregator  │  TrendEngine  │
│  AnalyticsResult │  DashboardBuilder   │  ExportEngine │
├──────────────────────────────────────────────────────┤
│                   Domain Layer                        │
│  GovernanceMetric │  GovernanceKPI    │  GovernanceRpt│
│  DashboardWidget  │  ReportSchedule   │  Snapshot     │
│  GovernanceTrend  │  GovernanceExport │  AnalyticsRslt│
├──────────────────────────────────────────────────────┤
│                Infrastructure Layer                   │
│  JPA │ Redis │ Kafka │ Micrometer │ Security │ Config │
└──────────────────────────────────────────────────────┘
```

### Layer Responsibilities

1. **Interfaces Layer** — REST endpoints under `/api/v1/governance/analytics/*`, DTOs for request/response, parameter validation, HTTP status mapping
2. **Application Layer** — Service orchestration, business logic coordination, pipeline management, authorization delegation
3. **Engine Layer** — KPI calculation algorithms, metrics aggregation strategies, trend analysis, dashboard data assembly, export format generation
4. **Domain Layer** — Immutable records for metrics, KPIs, reports, dashboards, snapshots, trends, exports, schedules; enums for types, statuses, formats
5. **Infrastructure Layer** — JPA entities/repositories for persistence, Redis for caching, Kafka for event publishing, Micrometer for monitoring, security for RBAC enforcement, configuration management

## Analytics Pipeline

```
                    ┌─────────────────────┐
                    │   Collect Metrics   │
                    └──────────┬──────────┘
                               │
                    ┌──────────▼──────────┐
                    │  Aggregate Metrics  │
                    └──────────┬──────────┘
                               │
                    ┌──────────▼──────────┐
                    │   Calculate KPIs    │
                    └──────────┬──────────┘
                               │
                    ┌──────────▼──────────┐
                    │  Generate Trends    │
                    └──────────┬──────────┘
                               │
                    ┌──────────▼──────────┐
                    │  Generate Reports   │
                    └──────────┬──────────┘
                               │
                    ┌──────────▼──────────┐
                    │ Publish Dashboard   │
                    └──────────┬──────────┘
                               │
                    ┌──────────▼──────────┐
                    │   Export Reports    │
                    └──────────┬──────────┘
                               │
                    ┌──────────▼──────────┐
                    │       Audit         │
                    └──────────┬──────────┘
                               │
                    ┌──────────▼──────────┐
                    │      Metrics        │
                    └─────────────────────┘
```

### Pipeline Steps

1. **Collect Metrics** — Ingest metric data points from governance operations (policy evaluations, decisions, approvals, compliance checks, risk assessments, trust scores)
2. **Aggregate Metrics** — Group metrics by time windows (5min, 15min, 1h, 6h, 24h, 7d, 30d), calculate statistical measures (count, sum, avg, min, max, p50, p95, p99)
3. **Calculate KPIs** — Run KPI engine to compute 10 governance KPIs from aggregated metrics with target comparisons and status determination
4. **Generate Trends** — Analyze historical metric patterns, calculate period-over-period changes, identify trend directions (UP, DOWN, STABLE, VOLATILE), generate trend forecasts
5. **Generate Reports** — Produce 14 report types with structured data, apply report templates, version reports for auditability
6. **Publish Dashboard Data** — Push aggregated dashboard data to cache for real-time UI consumption, support per-dashboard-widget caching
7. **Export Reports** — Convert reports to requested formats (JSON, CSV, Excel, PDF), track export jobs, manage file retention
8. **Audit** — Record all analytics operations in immutable audit trail, track metric origins for lineage
9. **Metrics** — Monitor analytics pipeline health with Micrometer counters and timers, track cache hit ratios, report generation durations

## Dashboard Capabilities

The governance dashboard provides the following views:

| Dashboard View | Description | Widgets |
|----------------|-------------|---------|
| Governance Overview | High-level health summary across all domains | KPI cards, trend sparklines, status indicators |
| Policy Status | Policy lifecycle metrics and enforcement statistics | Policy count by status, evaluation rate chart, violation timeline |
| Decision Statistics | Decision distribution and outcome analysis | Decision type pie chart, trend by action, conflict rate |
| Approval Metrics | Approval workflow throughput and SLA tracking | Approval funnel, SLA compliance gauge, reviewer workload |
| Compliance Status | Compliance pass rates and framework health | Pass rate gauge, framework summary table, violation trend |
| Risk Distribution | Risk level distribution and factor analysis | Risk level bar chart, factor breakdown, recommendation summary |
| Trust Trends | Trust score trends with factor breakdown | Trust score line chart, factor radar, score distribution |
| Confidence Trends | Confidence score trends with factor analysis | Confidence score line chart, factor comparison, level distribution |
| System Health | Analytics pipeline health and performance | Pipeline status, metric throughput, latency, error rate |
| Recent Events | Latest governance events stream | Event feed, filterable by type, severity, source |
| Top Violations | Most frequent policy/rule violations | Violation ranking, trend, responsible modules |
| Top Policies | Most evaluated/active policies | Policy ranking, evaluation count, enforcement rate |
| Top Risks | Highest risk assessments in period | Risk ranking, score, trust, confidence summary |
| Top Reviewers | Most active approval reviewers | Reviewer ranking, approval count, avg decision time |
