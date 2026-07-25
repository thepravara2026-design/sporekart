# Developer Guide

## Quick Start

### Prerequisites
- Java 21+
- Maven 3.9+

### Build & Test
```bash
cd services/reporting-service
mvn clean test          # 180 tests
mvn clean package -DskipTests
```

### Run
```bash
mvn spring-boot:run
# Service starts on http://localhost:8096
```

### Verify
```bash
curl http://localhost:8096/api/v1/reports/health
curl -X POST http://localhost:8096/api/v1/reports/generate
curl http://localhost:8096/api/v1/reports/
```

## Test Strategy
- **180 unit tests** across 23 test classes
- Domain models: 8 tests per model (Report, ReportTemplate, ReportSchedule, ReportExport, BusinessIntelligenceReport, ReportCache)
- Engines: 8 tests each (ReportEngine, TemplateEngine, BusinessIntelligenceRuntime)
- SDK: ReportBuilder (8), ReportRuntime (8), TemplateRuntime (6), ExportRuntime (4), SchedulerRuntime (7)
- Services: ReportingService (13), ReportRegistryService (12), ReportTelemetryService (10)
- Infrastructure: InMemoryReportRepository (12), ReportCacheService (8), MockExportService (8), MockSchedulerService (10)
- Controller: 23 endpoint tests with @WebMvcTest
- Context: Application context loads test

## Configuration
| Property | Default | Description |
|---|---|---|
| server.port | 8096 | HTTP port |
| report.engine.enabled | true | Enable report generation |
| report.template.enabled | true | Enable template engine |
| report.export.enabled | true | Enable mock export |
| report.export.default-format | PDF | Default export format |
| report.scheduler.enabled | true | Enable mock scheduler |
| report.scheduler.max-schedules | 100 | Max schedule limit |
| report.cache.ttl-seconds | 300 | Cache TTL |
| report.bi.enabled | true | Enable BI runtime |

## Frontend Integration
- Admin module: `frontend/web-app/src/admin/modules/report-center/`
- Route: `/admin/report-center`
- Mock service: `reportCenterMockService.ts`

## Architecture Decisions
1. **No parent POM** — Uses spring-boot-starter-parent directly
2. **No Lombok** — Manual getters/constructors for clarity (can be added later)
3. **No JPA** — In-memory ConcurrentHashMap for zero-config development
4. **No real export** — MockExportService tracks metadata only, no file I/O
5. **No real scheduling** — MockSchedulerService calculates next-run from frequency
6. **CSRF disabled** — Stateless API with Basic auth
