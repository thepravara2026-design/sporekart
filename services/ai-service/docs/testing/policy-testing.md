# Policy Engine Testing

## Test Files (18 files)

### Domain Tests (2 files)

| File | Coverage |
|------|----------|
| `domain/PolicyRecordTest.java` | Record construction, field access, equality, toString |
| `domain/PolicyEnumTest.java` | Enum values, ordinal, valueOf, meta-attributes |

### Application Service Tests (10 files)

| File | Coverage |
|------|----------|
| `application/PolicyEngineImplTest.java` | Full evaluation pipeline, context building, decision determination, isAllowed, evaluateWithContext |
| `application/PolicyEvaluatorImplTest.java` | Policy evaluation, rule evaluation, expression matching, decision resolution, matches |
| `application/PolicyDecisionServiceImplTest.java` | Decision strategies, conflict resolution (DENY_OVERRIDES, ALLOW_OVERRIDES), isAllowed, requiresReview |
| `application/PolicyLifecycleManagerImplTest.java` | Lifecycle transitions (activate/deactivate/archive/draft), version creation, transition validation |
| `application/PolicyValidatorImplTest.java` | Policy validation, rule validation, request validation, isValid |
| `application/PolicyCompilerImplTest.java` | Compile, validate, parse, validatePolicy, isCompiled |
| `application/PolicyConfigurationServiceImplTest.java` | Config CRUD, getConfig, setConfig, getAllConfigs, reloadConfig, isFeatureEnabled |
| `application/PolicyAuditServiceImplTest.java` | Audit recording, findByPolicyId, findByRequestId, findByUserId, findByDateRange, findByDecision |
| `application/PolicyMetricsServiceImplTest.java` | Metrics recording, evaluation count, violation count, average time, cache hit/miss, activations |
| `application/PolicyRegistryImplTest.java` | Registry CRUD, register, unregister, findById, findByModule, findByScope, findAll, isRegistered |

### Engine Tests (2 files)

| File | Coverage |
|------|----------|
| `engine/RuleEngineTest.java` | Rule matching (score-based: module, action, role, param), conflict resolution (7 strategies), filterApplicable, RuleMatchResult |
| `engine/ConditionEvaluatorTest.java` | 14 operators (EQUALS, NOT_EQUALS, CONTAINS, NOT_CONTAINS, GREATER_THAN, LESS_THAN, GREATER_EQUALS, LESS_EQUALS, IN, NOT_IN, EXISTS, NOT_EXISTS, MATCHES, STARTS_WITH, ENDS_WITH), evaluateAll, evaluateAny, negate, field resolution |

### Infrastructure Tests (3 files)

| File | Coverage |
|------|----------|
| `infrastructure/kafka/PolicyKafkaEventPublisherTest.java` | Event publishing for all 8 types (created, updated, deleted, activated, deactivated, evaluated, violation, failed), serialization |
| `infrastructure/redis/PolicyRedisCacheServiceTest.java` | Cache CRUD for 5 namespaces (registry, compiled, metadata, evaluation, health), eviction, invalidateAll |
| `infrastructure/monitoring/PolicyMonitoringServiceTest.java` | Micrometer metrics, recording evaluations, violations, decisions, activations, deactivations, cache hits/misses |

### Config Tests (1 file)

| File | Coverage |
|------|----------|
| `config/PolicyConfigTest.java` | Configuration properties binding, default values, CacheConfig, KafkaConfig |

## Coverage Targets

| Layer | Target |
|-------|--------|
| Application services | >90% |
| Engine classes | >90% |
| Infrastructure adapters | >80% |
| Domain records/enums | >95% |

## Test Dependencies

- JUnit 5 (Jupiter)
- Mockito for service mocking
- Spring Boot Test for integration tests
- H2 in-memory database for repository tests
- Embedded Kafka for event publisher tests
- Mock Redis for cache service tests

## Test Structure Pattern

All tests follow Arrange-Act-Assert pattern:

```java
@Test
void shouldReturnAllowWhenNoViolations() {
    // Arrange
    EvaluationRequest request = new EvaluationRequest(/*...*/);
    // Act
    EvaluationResult result = engine.evaluate(request);
    // Assert
    assertEquals(PolicyDecision.ALLOW, result.finalDecision());
    assertTrue(result.passed());
}
```

## Running Tests

```bash
# All policy engine tests
mvn test -pl services/ai-service -Dtest="com.sporekart.ai.policy.**"

# Specific layer
mvn test -pl services/ai-service -Dtest="com.sporekart.ai.policy.application.*"
mvn test -pl services/ai-service -Dtest="com.sporekart.ai.policy.engine.*"
mvn test -pl services/ai-service -Dtest="com.sporekart.ai.policy.infrastructure.*"
```
