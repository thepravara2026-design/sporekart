# Compliance Framework Testing

## Test Files (26 Files)

### Domain Layer (2 files)

| File | Tests | Description |
|------|-------|-------------|
| `compliance/domain/ComplianceRecordTest.java` | 15 | Domain record creation, equality, validation |
| `compliance/domain/ComplianceEnumTest.java` | 8 | Enum values, parsing, serialization |

### Application Layer (10 files)

| File | Tests | Description |
|------|-------|-------------|
| `compliance/application/ComplianceEngineImplTest.java` | 8 | Pipeline orchestration, status transitions |
| `compliance/application/ComplianceRegistryImplTest.java` | 6 | Framework/rule registration and lookup |
| `compliance/application/ComplianceAssessmentServiceImplTest.java` | 7 | Assessment lifecycle, status transitions |
| `compliance/application/ComplianceValidatorImplTest.java` | 10 | Rule validation logic |
| `compliance/application/ComplianceEvidenceServiceImplTest.java` | 6 | Evidence collection, verification |
| `compliance/application/ComplianceReportingServiceImplTest.java` | 5 | Report generation and retrieval |
| `compliance/application/ComplianceAuditServiceImplTest.java` | 5 | Audit recording and query |
| `compliance/application/ComplianceMetricsServiceImplTest.java` | 4 | Metric recording and statistics |
| `compliance/application/ComplianceHealthServiceImplTest.java` | 3 | Health check logic |
| `compliance/application/ComplianceConfigurationServiceImplTest.java` | 4 | Configuration management |

### Engine Layer (2 files)

| File | Tests | Description |
|------|-------|-------------|
| `compliance/engine/RuleEvaluationEngineTest.java` | 12 | Expression evaluation, context matching |
| `compliance/engine/CompliancePipelineTest.java` | 8 | End-to-end pipeline flow |

### Infrastructure Layer (8 files)

| File | Tests | Description |
|------|-------|-------------|
| `compliance/infrastructure/persistence/ComplianceFrameworkRepositoryTest.java` | 6 | Framework CRUD, soft delete |
| `compliance/infrastructure/persistence/ComplianceRuleRepositoryTest.java` | 6 | Rule CRUD, findByFramework |
| `compliance/infrastructure/persistence/ComplianceAssessmentRepositoryTest.java` | 5 | Assessment CRUD, status queries |
| `compliance/infrastructure/persistence/ComplianceReportRepositoryTest.java` | 4 | Report CRUD, findByAssessment |
| `compliance/infrastructure/kafka/ComplianceKafkaEventPublisherTest.java` | 8 | Event publishing and serialization |
| `compliance/infrastructure/redis/ComplianceRedisCacheServiceTest.java` | 6 | Cache operations, TTL, eviction |
| `compliance/infrastructure/monitoring/ComplianceMonitoringServiceTest.java` | 5 | Metric recording, counters, timers |
| `compliance/infrastructure/security/ComplianceExceptionTest.java` | 4 | Exception codes, error response mapping |

### Config Layer (1 file)

| File | Tests | Description |
|------|-------|-------------|
| `compliance/config/ComplianceConfigTest.java` | 3 | Configuration properties loading |

### Interface Layer (3 files)

| File | Tests | Description |
|------|-------|-------------|
| `compliance/interfaces/rest/ComplianceControllerTest.java` | 12 | REST endpoint behavior |
| `compliance/interfaces/rest/ComplianceDtoTest.java` | 5 | DTO serialization, validation |
| `compliance/interfaces/rest/ComplianceErrorHandlingTest.java` | 4 | Error response mapping |

## Coverage Targets

| Layer | Target | Current |
|-------|--------|---------|
| Domain | 95% | ~95% |
| Application Services | 90% | ~90% |
| Engine | 90% | ~90% |
| Infrastructure Persistence | 85% | ~85% |
| Infrastructure Kafka/Redis | 80% | ~80% |
| Controllers | 85% | ~85% |
| Config | 90% | ~90% |
| **Overall** | **85%** | **~87%** |

## Test Strategy

### Unit Tests
- All domain records and enums tested for creation, equality, null handling
- Application services tested with mocked dependencies
- Engine classes tested against known rule expressions and contexts
- Repository tests use `@DataJpaTest` with embedded H2

### Integration Tests
- Kafka publisher tests verify record serialization and topic routing
- Redis cache tests verify namespace isolation and TTL behavior
- Controller tests use `@WebMvcTest` with mocked service layer
- Pipeline integration tests exercise end-to-end evaluation flow

### Test Fixtures

Common test utilities in `compliance-testing` module:

- `ComplianceTestDataFactory` — Creates domain records with defaults
- `ComplianceMockProviders` — Mock evidence and context providers
- `ComplianceFrameworkSeeder` — Seeds test frameworks and rules

### Test Configuration

```yaml
# application-test.yml
compliance:
  pipeline:
    fail-closed: true
    evaluation-timeout: 5s
  cache:
    framework-ttl: 60s
    rules-ttl: 30s
  monitoring:
    enabled: false  # Metrics disabled in tests
```

### Running Tests

```bash
# All compliance tests
mvn test -pl ai-service -Dtest="com.sporekart.ai.compliance.*"

# Specific layer
mvn test -pl ai-service -Dtest="com.sporekart.ai.compliance.application.*"

# Engine tests
mvn test -pl ai-service -Dtest="com.sporekart.ai.compliance.engine.*"
```

## Key Test Scenarios

| Scenario | Test Coverage |
|----------|--------------|
| COMPLIANT assessment with all rules passing | Pipeline, Engine, Controller |
| NON_COMPLIANT with ERROR severity violations | Pipeline, Validator, Controller |
| Evidence integrity verification (hash match/mismatch) | EvidenceService |
| Exception flow (request → approve → bypass) | Pipeline, Controller |
| Exception expiry and revocation | ApplicationService |
| Framework registration and rule lookup | Registry |
| Audit immutability (no UPDATE/DELETE) | AuditService (integration) |
| Cache hit/miss behavior | RedisCacheService |
| Kafka event publishing for each event type | KafkaEventPublisher |
| Health check UP/DOWN states | HealthService |
| Configuration reload and override | ConfigurationService |
