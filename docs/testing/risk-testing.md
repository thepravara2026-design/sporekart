# Risk Framework Testing

## Test Files (27 Files)

### Domain Layer (2 files)

| File | Tests | Description |
|------|-------|-------------|
| `risk/domain/RiskRecordTest.java` | 14 | Domain record creation, equality, validation |
| `risk/domain/RiskEnumTest.java` | 6 | Enum values, parsing, serialization |

### Application Layer (11 files)

| File | Tests | Description |
|------|-------|-------------|
| `risk/application/RiskEngineImplTest.java` | 8 | Pipeline orchestration, status transitions |
| `risk/application/RiskAssessmentServiceImplTest.java` | 7 | Assessment lifecycle, CRUD operations |
| `risk/application/RiskScoringServiceImplTest.java` | 10 | Score calculation, weighted aggregation |
| `risk/application/RiskClassificationServiceImplTest.java` | 6 | Risk level classification, threshold comparison |
| `risk/application/TrustEngineImplTest.java` | 8 | Trust evaluation orchestration |
| `risk/application/TrustScoreServiceImplTest.java` | 10 | Trust factor scoring, weighted average |
| `risk/application/ConfidenceCalculatorImplTest.java` | 8 | Confidence factor scoring, explanation |
| `risk/application/RiskRecommendationServiceImplTest.java` | 6 | Recommendation generation logic |
| `risk/application/RiskAuditServiceImplTest.java` | 5 | Audit recording and query |
| `risk/application/RiskMetricsServiceImplTest.java` | 4 | Metric recording and statistics |
| `risk/application/RiskConfigurationServiceImplTest.java` | 4 | Configuration management |

### Engine Layer (3 files)

| File | Tests | Description |
|------|-------|-------------|
| `risk/engine/TrustScoreCalculatorTest.java` | 15 | 9 trust factor evaluations, weighted average calculation, edge cases (all zero, all max, missing factors) |
| `risk/engine/ConfidenceCalculatorEngineTest.java` | 12 | 6 confidence factor evaluations, weighted average, explanation generation, edge cases |
| `risk/engine/RiskResultTest.java` | 6 | Result model aggregation, serialization |

### Infrastructure Layer (8 files)

| File | Tests | Description |
|------|-------|-------------|
| `risk/infrastructure/persistence/RiskAssessmentRepositoryTest.java` | 6 | Assessment CRUD, soft delete, findByStatus |
| `risk/infrastructure/persistence/RiskFactorRepositoryTest.java` | 5 | Factor CRUD, findByAssessmentId |
| `risk/infrastructure/persistence/RiskDecisionRepositoryTest.java` | 4 | Decision CRUD, findByAssessmentId |
| `risk/infrastructure/persistence/RiskTrustScoreRepositoryTest.java` | 4 | Trust score CRUD, findByAssessmentId |
| `risk/infrastructure/kafka/RiskKafkaEventPublisherTest.java` | 7 | Event publishing and serialization |
| `risk/infrastructure/redis/RiskRedisCacheServiceTest.java` | 5 | Cache operations, namespace isolation, TTL |
| `risk/infrastructure/monitoring/RiskMonitoringServiceTest.java` | 5 | Metric recording, counters, timers |
| `risk/infrastructure/security/RiskExceptionTest.java` | 4 | Exception codes, error response mapping |

### Config Layer (1 file)

| File | Tests | Description |
|------|-------|-------------|
| `risk/config/RiskConfigTest.java` | 3 | Configuration properties loading |

### Interface Layer (2 files)

| File | Tests | Description |
|------|-------|-------------|
| `risk/interfaces/rest/RiskControllerTest.java` | 14 | REST endpoint behavior for all 10 endpoints |
| `risk/interfaces/rest/RiskDtoTest.java` | 5 | DTO serialization, validation, error mapping |

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
- Engine classes tested against known factor inputs and expected outputs
- TrustScoreCalculator tested with various weight configurations and factor combinations
- ConfidenceCalculatorEngine tested for all 6 factor types and edge cases
- Repository tests use `@DataJpaTest` with embedded H2

### Integration Tests
- Kafka publisher tests verify record serialization and topic routing
- Redis cache tests verify namespace isolation and TTL behavior
- Controller tests use `@WebMvcTest` with mocked service layer
- Pipeline integration tests exercise end-to-end risk evaluation flow

### Test Fixtures

Common test utilities:

- `RiskTestDataFactory` — Creates domain records with defaults
- `RiskMockProviders` — Mock factor and evidence providers
- `TrustFactorSeeder` — Seeds test trust factors with known weights
- `ConfidenceFactorSeeder` — Seeds test confidence factors with known weights

### Test Configuration

```yaml
# application-test.yml
risk:
  pipeline:
    fail-closed: true
    timeout: 5s
  thresholds:
    low-max: 20
    medium-max: 40
    high-max: 70
  cache:
    assessment-ttl: 60s
    factors-ttl: 30s
    trust-ttl: 60s
    confidence-ttl: 60s
  monitoring:
    enabled: false
```

### Running Tests

```bash
# All risk tests
mvn test -pl ai-service -Dtest="com.sporekart.ai.risk.*"

# Specific layer
mvn test -pl ai-service -Dtest="com.sporekart.ai.risk.application.*"

# Engine tests
mvn test -pl ai-service -Dtest="com.sporekart.ai.risk.engine.*"

# Single test class
mvn test -pl ai-service -Dtest="com.sporekart.ai.risk.engine.TrustScoreCalculatorTest"
```

## Key Test Scenarios

| Scenario | Test Coverage |
|----------|--------------|
| LOW risk — all factors score 0-20 | Pipeline, Scoring, Classification |
| CRITICAL risk — factor scores exceed high threshold | Pipeline, Scoring, Classification |
| Trust evaluation with all 9 factors scored | TrustScoreCalculator, TrustEngine |
| Confidence calculation with all 6 factors | ConfidenceCalculatorEngine |
| Recommendation generation for risk/trust/confidence combinations | RecommendationService |
| Edge case — all factors at minimum (score 0) | TrustScoreCalculator, ConfidenceCalculatorEngine |
| Edge case — all factors at maximum (score 100) | TrustScoreCalculator, ConfidenceCalculatorEngine |
| Edge case — missing trust factors (handle gracefully) | TrustScoreCalculator |
| Edge case — empty factor list | RiskEngine, ScoringService |
| Factor weight boundary validation | RiskScoringService |
| Threshold reconfiguration and effect on classification | RiskClassificationService |
| Audit immutability (no UPDATE/DELETE) | AuditService (integration) |
| Cache hit/miss behavior | RedisCacheService |
| Kafka event publishing for each event type | KafkaEventPublisher |
| Health check UP/DOWN states | Health endpoint |
| DTO serialization for all request/response types | DtoTest |
| Error response mapping for all RSK_4xx codes | ExceptionTest, ControllerTest |
