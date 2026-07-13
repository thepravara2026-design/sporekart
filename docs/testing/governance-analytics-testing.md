# Governance Analytics Testing

## Test Files (27 Files)

### Domain Layer (2 files)

| File | Tests | Description |
|------|-------|-------------|
| `governance/analytics/domain/AnalyticsRecordTest.java` | 15 | Domain record creation, equality, validation for all 15 records |
| `governance/analytics/domain/AnalyticsEnumTest.java` | 6 | Enum values, parsing, serialization for all 6 enums |

### Application Layer (11 files)

| File | Tests | Description |
|------|-------|-------------|
| `governance/analytics/application/GovernanceAnalyticsServiceImplTest.java` | 8 | Metric collection, analytics query, pipeline orchestration |
| `governance/analytics/application/GovernanceReportingServiceImplTest.java` | 7 | Report generation lifecycle, status transitions, template application |
| `governance/analytics/application/DashboardServiceImplTest.java` | 6 | Dashboard CRUD, widget management, cache refresh |
| `governance/analytics/application/MetricsAggregationServiceImplTest.java` | 8 | Time-window aggregation, statistical measures, group-by logic |
| `governance/analytics/application/TrendAnalysisServiceImplTest.java` | 6 | Trend direction detection, period comparison, percentage change |
| `governance/analytics/application/KPIServiceImplTest.java` | 8 | KPI calculation, status determination, target comparison |
| `governance/analytics/application/ExportServiceImplTest.java` | 6 | Export generation, format conversion, file management |
| `governance/analytics/application/SnapshotServiceImplTest.java` | 5 | Snapshot creation, comparison, listing |
| `governance/analytics/application/ScheduledReportServiceImplTest.java` | 6 | Schedule CRUD, frequency resolution, next-run calculation |
| `governance/analytics/application/AnalyticsAuditServiceImplTest.java` | 5 | Audit recording, query, export |
| `governance/analytics/application/AnalyticsConfigurationServiceImplTest.java` | 4 | Configuration management, defaults, reload |

### Engine Layer (3 files)

| File | Tests | Description |
|------|-------|-------------|
| `governance/analytics/engine/KpiCalculatorTest.java` | 20 | All 10 KPI calculations, target comparison, status determination, edge cases (zero values, empty data, boundary thresholds) |
| `governance/analytics/engine/MetricsAggregatorTest.java` | 15 | Time-window aggregation, statistical measures (count, sum, avg, min, max, p50, p95, p99), group-by, edge cases (empty set, single value, outliers) |
| `governance/analytics/engine/AnalyticsResultTest.java` | 6 | Result model aggregation, pagination, serialization |

### Infrastructure Layer (8 files)

| File | Tests | Description |
|------|-------|-------------|
| `governance/analytics/infrastructure/persistence/GovernanceMetricRepositoryTest.java` | 6 | Metric CRUD, findByTypeAndTimeRange, soft delete |
| `governance/analytics/infrastructure/persistence/GovernanceDashboardRepositoryTest.java` | 5 | Dashboard CRUD, findByOwner, soft delete |
| `governance/analytics/infrastructure/persistence/GovernanceReportRepositoryTest.java` | 6 | Report CRUD, findByTypeAndStatus, findByTimeRange |
| `governance/analytics/infrastructure/persistence/GovernanceKpiRepositoryTest.java` | 5 | KPI CRUD, findByName, findByStatus |
| `governance/analytics/infrastructure/kafka/AnalyticsKafkaEventPublisherTest.java` | 7 | Event publishing and serialization for all 7 event types |
| `governance/analytics/infrastructure/redis/AnalyticsRedisCacheServiceTest.java` | 5 | Cache operations, namespace isolation, TTL for all 5 namespaces |
| `governance/analytics/infrastructure/monitoring/AnalyticsMonitoringServiceTest.java` | 5 | Metric recording, counters, timers, gauges |
| `governance/analytics/infrastructure/security/AnalyticsExceptionTest.java` | 4 | Exception codes, error response mapping |

### Config Layer (1 file)

| File | Tests | Description |
|------|-------|-------------|
| `governance/analytics/config/AnalyticsConfigTest.java` | 3 | Configuration properties loading, defaults, validation |

### Interface Layer (2 files)

| File | Tests | Description |
|------|-------|-------------|
| `governance/analytics/interfaces/rest/AnalyticsControllerTest.java` | 14 | REST endpoint behavior for all 10 endpoints |
| `governance/analytics/interfaces/rest/AnalyticsDtoTest.java` | 5 | DTO serialization, validation, error mapping |

## Coverage Targets

| Layer | Target | Current |
|-------|--------|---------|
| Domain | 95% | ~95% |
| Application Services | 90% | ~90% |
| Engine | 92% | ~92% |
| Infrastructure Persistence | 85% | ~85% |
| Infrastructure Kafka/Redis | 80% | ~80% |
| Controllers | 85% | ~85% |
| Config | 90% | ~90% |
| **Overall** | **85%** | **~88%** |

## Test Strategy

### Unit Tests
- All domain records and enums tested for creation, equality, null handling
- Application services tested with mocked dependencies
- Engine classes tested against known inputs and expected outputs
- KpiCalculator tested with various metric combinations and edge cases
- MetricsAggregator tested for all aggregation functions and time windows
- Repository tests use `@DataJpaTest` with embedded H2

### Integration Tests
- Kafka publisher tests verify record serialization and topic routing
- Redis cache tests verify namespace isolation and TTL behavior
- Controller tests use `@WebMvcTest` with mocked service layer
- Pipeline integration tests exercise end-to-end analytics flow

### Test Fixtures

Common test utilities:
- `AnalyticsTestDataFactory` — Creates domain records with defaults
- `MetricSampleProvider` — Provides sample metric data for various types
- `KpiTestSeeder` — Seeds test KPIs with known targets and thresholds
- `ReportTestBuilder` — Builds sample reports for testing

### Test Configuration

```yaml
# application-test.yml
governance:
  analytics:
    pipeline:
      fail-closed: false
      timeout: 5s
    cache:
      metrics-ttl: 60s
      dashboard-ttl: 30s
      kpis-ttl: 60s
      trends-ttl: 120s
      reports-ttl: 120s
    monitoring:
      enabled: false
```

### Running Tests

```bash
# All governance analytics tests
mvn test -pl ai-service -Dtest="com.sporekart.ai.governance.analytics.*"

# Specific layer
mvn test -pl ai-service -Dtest="com.sporekart.ai.governance.analytics.application.*"

# Engine tests
mvn test -pl ai-service -Dtest="com.sporekart.ai.governance.analytics.engine.*"

# Single test class
mvn test -pl ai-service -Dtest="com.sporekart.ai.governance.analytics.engine.KpiCalculatorTest"
```

## Key Test Scenarios

| Scenario | Test Coverage |
|----------|--------------|
| Metric collection with all metric types | GovernanceAnalyticsService |
| Metric aggregation by time window (5min, 1h, 24h) | MetricsAggregator, MetricsAggregationService |
| KPI calculation for all 10 KPIs | KpiCalculator, KPIService |
| KPI status transitions (ON_TRACK → AT_RISK → CRITICAL) | KpiCalculator, KPIService |
| Trend direction detection (UP, DOWN, STABLE, VOLATILE) | TrendAnalysisService |
| Report generation for all 14 report types | GovernanceReportingService |
| Export format conversion (JSON, CSV) | ExportService |
| Scheduled report frequency resolution | ScheduledReportService |
| Dashboard widget data assembly | DashboardService |
| Snapshot creation and comparison | SnapshotService |
| Edge case — empty metric set | MetricsAggregator, KpiCalculator |
| Edge case — all metrics at zero | KpiCalculator, TrendAnalysisService |
| Edge case — single data point for trend | TrendAnalysisService |
| Edge case — KPI target boundaries | KpiCalculator |
| Audit recording for all analytics operations | AnalyticsAuditService |
| Cache hit/miss behavior | AnalyticsRedisCacheService |
| Kafka event publishing for each event type | AnalyticsKafkaEventPublisher |
| DTO serialization for all request/response types | AnalyticsDtoTest |
| Error response mapping for all ANL_4xx codes | AnalyticsExceptionTest, ControllerTest |
