# Reporting Runtime

## Purpose
The Reporting Runtime orchestrates report generation, template management, scheduling, export, business intelligence, caching, and telemetry.

## Key Architecture
```java
ReportingService (orchestrator)
  ├── ReportEngine (generation)
  ├── TemplateEngine (templates)
  ├── BusinessIntelligenceRuntime (BI reports)
  ├── ReportRegistryService (query delegation)
  ├── ReportCacheService (caching)
  ├── ReportTelemetryService (metrics)
  ├── MockExportService (export)
  └── MockSchedulerService (scheduling)
```

## Available Endpoints (22 total)

### Health
- `GET /api/v1/reports/health`

### Reports (7 endpoints)
- `GET /` — List all reports
- `GET /{id}` — Get report by ID
- `GET /type/{type}` — Filter by type
- `GET /category/{category}` — Filter by category
- `GET /status/{status}` — Filter by status
- `POST /generate` — Generate all seeded reports
- `POST /generate/{category}` — Generate for category

### Templates (5 endpoints)
- `GET /templates` — List all templates
- `GET /templates/{id}` — Get template by ID
- `GET /templates/active` — Get active templates
- `POST /templates/generate` — Generate seeded templates

### Schedules (7 endpoints)
- `GET /schedules` — List all schedules
- `GET /schedules/{id}` — Get by ID
- `GET /schedules/active` — Get active
- `POST /schedules` — Create schedule
- `POST /schedules/{id}/pause` — Pause
- `POST /schedules/{id}/resume` — Resume
- `POST /schedules/{id}/execute` — Execute now
- `DELETE /schedules/{id}` — Delete

### Exports (3 endpoints)
- `GET /exports` — List all exports
- `GET /exports/{id}` — Get by ID
- `POST /{reportId}/export/{format}` — Export report

### BI Reports (3 endpoints)
- `GET /bi` — List all BI reports
- `GET /bi/{id}` — Get by ID
- `POST /bi/generate` — Generate BI reports

### Cache (2 endpoints)
- `GET /cache` — Cache info
- `DELETE /cache` — Clear cache

### Telemetry (2 endpoints)
- `GET /telemetry` — Current metrics
- `GET /telemetry/history` — Metric history with recent activity
