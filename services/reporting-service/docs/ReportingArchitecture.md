# Enterprise Reporting Architecture

## Overview
The Enterprise Reporting Platform is the centralized reporting engine for SporeKart. Every executive, operational, financial, AI, inventory, marketplace, compliance, and business report is generated through this platform.

## Architecture Principles
- **Single Source of Truth** — No module independently generates reports
- **Hexagonal Architecture** with Ports & Adapters pattern
- **Domain-Driven Design** with immutable domain models
- **Fully mocked** — no production PDF, Excel, scheduling, or email
- **Stateless**, single-process deployment on port 8096

## Service Layer
```
interfaces/ (REST controllers)
    ↓
application/ (services, SDK, engines)
    ↓
domain/ (models, repository ports)
    ↓
infrastructure/ (persistence, cache, export, scheduler)
```

## Core Components
| Component | Responsibility |
|---|---|
| ReportEngine | Generates 17 categories of reports with KPI data |
| TemplateEngine | Manages 14+ reusable report templates |
| BusinessIntelligenceRuntime | Aggregates data from Analytics, Prediction, Decision, Alert engines |
| MockExportService | Simulates PDF/CSV/Excel/JSON export with file metadata |
| MockSchedulerService | Simulates schedule creation with frequency-based next-run calculation |
| ReportCacheService | TTL-based in-memory caching (default 300s) |
| ReportTelemetryService | 9 metrics tracking: requests, exports, schedules, templates, runtime, errors |

## Domain Model
- **Report** — Immutable record with UUID, status lifecycle, KPIs, recommendations, risks
- **ReportTemplate** — Reusable template with sections, category, type, active flag
- **ReportSchedule** — Schedule with frequency, cron expression, export format, next/last run
- **ReportExport** — Export record with format, filename, estimated file size
- **BusinessIntelligenceReport** — Aggregated BI report with data sources from upstream engines
- **ReportCache** — TTL-based cache entry with hit tracking

## Technology Stack
- Java 21 + Spring Boot 3.3.3
- Spring Security (Basic Auth + role-based)
- SpringDoc OpenAPI 2.6.0
- JUnit 5 + Mockito (180 tests)
