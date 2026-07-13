# Assistant Platform Testing

**Version:** 1.0.0
**Last Updated:** 2026-07-12
**Module:** ai-service

---

## Test Strategy

The Assistant Platform follows a layered testing strategy covering unit tests, integration tests, and architecture validation tests. The approach prioritizes testing the orchestration pipeline, intent resolution, task planning, copilot execution, security controls, and REST API contracts.

### Testing Principles

1. **Pipeline Coverage** — Test each stage of the pipeline (parse → intent → task → execute → respond) in isolation and as a full flow
2. **Copilot Contract** — Every copilot stub must pass the same contract tests
3. **Security First** — Prompt injection protection, rate limiting, and RBAC must have dedicated test suites
4. **Mock Business Modules** — Business module clients are mocked; no real service calls in tests
5. **Persistence Verified** — JPA entity mappings and repository queries are tested with @DataJpaTest
6. **API Contract Tests** — REST endpoints validated with @WebMvcTest against documented schemas

---

## Test Coverage Targets

| Layer | Target Coverage | Measurement |
|-------|-----------------|-------------|
| Domain (enums + records) | 100% | Line coverage |
| Application services | 95% | Branch coverage |
| Infrastructure (JPA) | 90% | Line coverage |
| Infrastructure (Redis) | 90% | Line coverage |
| Infrastructure (Kafka) | 90% | Line coverage |
| REST Controller | 95% | Line coverage |
| Security (sanitizer, rate limiter) | 100% | Branch coverage |
| **Overall** | **85%+** | Line coverage |

---

## Test Categories

### Unit Tests (~40 tests)

| Test Class | Tests | Focus |
|------------|-------|-------|
| `IntentResolverImplTest` | 6 | Intent classification, entity extraction, confidence scoring, ambiguous resolution |
| `TaskPlannerImplTest` | 5 | Plan creation, step ordering, dependency resolution, priority assignment |
| `CopilotOrchestratorImplTest` | 5 | Copilot routing, step execution, error handling, aggregation |
| `AssistantOrchestratorImplTest` | 4 | Full pipeline execution, error propagation, response building |
| `InputParserTest` | 3 | Message validation, sanitization, context extraction |
| `PromptInjectionSanitizerTest` | 6 | Pattern detection, action enforcement, edge cases |
| `RateLimiterTest` | 4 | Window tracking, burst handling, ban duration escalation |
| `ResponseBuilderTest` | 3 | Response formatting, metadata inclusion, suggestion generation |
| `AssistantTypeTest` | 2 | Enum values, display names |
| `IntentCategoryTest` | 2 | Category coverage, mapping |

### Repository Tests (~16 tests)

| Test Class | Tests | Focus |
|------------|-------|-------|
| `CopilotProfileRepositoryTest` | 2 | CRUD, find by type, find enabled |
| `IntentRepositoryTest` | 2 | CRUD, find by session, find by user |
| `TaskPlanRepositoryTest` | 2 | CRUD, find by intent, find by session |
| `TaskStepRepositoryTest` | 2 | CRUD, find by plan, find by copilot |
| `SessionRepositoryTest` | 2 | CRUD, find by user, find active |
| `FeedbackRepositoryTest` | 2 | CRUD, find by session, find by rating |
| `AuditLogRepositoryTest` | 2 | CRUD, find by user, find by action |
| `ContextRepositoryTest` | 2 | CRUD, find by session, upsert by key |

### Controller Tests (~18 tests)

| Test Class | Tests | Focus |
|------------|-------|-------|
| `AssistantControllerChatTest` | 3 | Chat success, validation error, rate limit |
| `AssistantControllerIntentTest` | 2 | Intent resolution, validation |
| `AssistantControllerTaskTest` | 2 | Task plan create, task plan get |
| `AssistantControllerCopilotTest` | 3 | List copilots, get copilot, copilot not found |
| `AssistantControllerSessionTest` | 4 | Session create, session get, session not found, session ownership |
| `AssistantControllerFeedbackTest` | 2 | Feedback submit, validation error |
| `AssistantControllerSecurityTest` | 2 | Unauthenticated access, insufficient role |

### Integration Tests (~6 tests)

| Test Class | Tests | Focus |
|------------|-------|-------|
| `AssistantFullPipelineIntegrationTest` | 2 | End-to-end chat flow, multi-step plan |
| `AssistantRedisCacheIntegrationTest` | 2 | Cache hit/miss, TTL expiry |
| `AssistantKafkaEventIntegrationTest` | 2 | Event publishing, event payload structure |

---

## Mocking Approach

### Mocked Components

| Component | Mocking Library | Reason |
|-----------|-----------------|--------|
| Business module clients (12 clients) | Mockito | External service boundaries |
| RedisTemplate | MockedRepository | Cache layer isolation |
| KafkaTemplate | Mockito | Event publishing isolation |
| SecurityContextHolder | Mockito | Authentication context |
| MeterRegistry | Mockito | Monitoring isolation |

### Mock Configuration

```java
@ExtendWith(MockitoExtension.class)
class IntentResolverImplTest {

    @Mock
    private EntityExtractor entityExtractor;

    @Mock
    private KeywordClassifier keywordClassifier;

    @InjectMocks
    private IntentResolverImpl intentResolver;

    @Test
    void shouldResolveInquiryIntent() {
        // Given
        var input = new ParsedInput("Show me customer CUST-456", ...);
        when(keywordClassifier.classify(input.text()))
            .thenReturn(new Classification("INQUIRY", 0.92));
        when(entityExtractor.extract(input.text()))
            .thenReturn(Map.of("customerId", "CUST-456"));

        // When
        var result = intentResolver.resolve(input);

        // Then
        assertThat(result.category()).isEqualTo(IntentCategory.INQUIRY);
        assertThat(result.confidence()).isGreaterThanOrEqualTo(0.6);
        assertThat(result.entities()).containsKey("customerId");
    }
}
```

### Test Fixtures

```java
public class TestDataFactory {
    public static ParsedInput createParsedInput(String message) {
        return new ParsedInput(
            UUID.randomUUID(),
            "user-123",
            message,
            Map.of("sessionId", UUID.randomUUID().toString())
        );
    }

    public static IntentRecord createResolvedIntent(String copilotType) {
        return new IntentRecord(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "user-123",
            "test utterance",
            IntentCategory.INQUIRY,
            0.92,
            Map.of("customerId", "CUST-456"),
            copilotType,
            IntentStatus.RESOLVED,
            null,
            Instant.now()
        );
    }

    public static TaskPlan createSingleStepPlan() {
        return new TaskPlan(
            UUID.randomUUID(),
            UUID.randomUUID(),
            UUID.randomUUID(),
            List.of(createTaskStep(1, "CUSTOMER", "LOOKUP_CUSTOMER")),
            TaskStatus.PENDING,
            TaskPriority.MEDIUM,
            "CUSTOMER",
            Instant.now(),
            null
        );
    }

    public static TaskStep createTaskStep(int seq, String copilotType, String action) {
        return new TaskStep(
            UUID.randomUUID(),
            UUID.randomUUID(),
            seq,
            copilotType,
            action,
            Map.of("customerId", "CUST-456"),
            null,
            TaskStepStatus.PENDING,
            null,
            null,
            null
        );
    }
}
```

---

## Test Execution

```bash
# Run all assistant tests
./mvnw test -pl ai-service -Dtest="*Assistant*"

# Run specific test class
./mvnw test -pl ai-service -Dtest="IntentResolverImplTest"

# Run with coverage
./mvnw verify -pl ai-service -Pcoverage

# Run architecture tests only
./mvnw test -pl ai-service -Dtest="*ArchitectureTest"
```

---

## Test File Organization

```
ai-service/src/test/java/com/sporekart/ai/assistant/
├── domain/
│   ├── AssistantTypeTest.java
│   └── IntentCategoryTest.java
├── application/
│   ├── IntentResolverImplTest.java
│   ├── TaskPlannerImplTest.java
│   ├── CopilotOrchestratorImplTest.java
│   ├── AssistantOrchestratorImplTest.java
│   ├── InputParserTest.java
│   ├── ResponseBuilderTest.java
│   └── PromptInjectionSanitizerTest.java
├── infrastructure/
│   ├── persistence/
│   │   ├── CopilotProfileRepositoryTest.java
│   │   ├── IntentRepositoryTest.java
│   │   ├── TaskPlanRepositoryTest.java
│   │   ├── TaskStepRepositoryTest.java
│   │   ├── SessionRepositoryTest.java
│   │   ├── FeedbackRepositoryTest.java
│   │   ├── AuditLogRepositoryTest.java
│   │   └── ContextRepositoryTest.java
│   ├── cache/
│   │   └── AssistantRedisCacheIntegrationTest.java
│   └── events/
│       └── AssistantKafkaEventIntegrationTest.java
├── interfaces/
│   └── rest/
│       ├── AssistantControllerChatTest.java
│       ├── AssistantControllerIntentTest.java
│       ├── AssistantControllerTaskTest.java
│       ├── AssistantControllerCopilotTest.java
│       ├── AssistantControllerSessionTest.java
│       ├── AssistantControllerFeedbackTest.java
│       └── AssistantControllerSecurityTest.java
└── integration/
    └── AssistantFullPipelineIntegrationTest.java
```
