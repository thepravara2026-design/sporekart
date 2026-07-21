# AI Gateway — Extension Guide

## How to Extend the AI Gateway

### 1. Add a New Provider Type

**Step 1:** Add to `AiProviderType` (if not already present) in `core/domain/AiProviderType.java`

**Step 2:** Implement the `AIProvider` interface:
```java
@Component
public class NewProvider implements AIProvider {
    @Override
    public String providerId() { return "new-provider"; }
    @Override
    public ProviderType providerType() { return ProviderType.NEW; }
    @Override
    public ProviderResponse execute(ProviderRequest request) { ... }
}
```

**Step 3:** Implement `ProviderAdapter`:
```java
@Component
public class NewProviderAdapter implements ProviderAdapter {
    @Override
    public ProviderRequest adaptRequest(AiRequest request) { ... }
    @Override
    public ProviderResponse adaptResponse(AiResponse response) { ... }
    @Override
    public boolean supports(ProviderType type) {
        return type == ProviderType.NEW;
    }
}
```

**Step 4:** Register in `providerRegistry` or via `@ComponentScan`

**Step 5:** Configure in `application.yml`:
```yaml
sporekart:
  ai:
    providers:
      new-provider:
        enabled: true
        api-key: ${NEW_PROVIDER_KEY}
        models: [new-model-v1]
        timeout: 30s
```

**Step 6:** Update `ProviderResolver.resolve()` to map provider name → `AiProviderType`

### 2. Add a New Pipeline Stage

**Step 1:** Add enum constant:
```java
// In PipelineStage.java
CACHING(12, "Response caching");
```

**Step 2:** Create stage handler:
```java
@Component
public class CachingStage implements PipelineExecutor.StageHandler {
    @Override
    public PipelineResult handle(PipelineContext context) {
        // Implement stage logic
        return PipelineResult.success(..., context);
    }
}
```

**Step 3:** Register in `PipelineExecutor`:
```java
pipelineExecutor.registerStageHandler(PipelineStage.CACHING, cachingStage);
```

**Step 4:** Update `GatewayPipeline.execute()` to include the new stage

### 3. Add a New Routing Strategy

**Step 1:** Implement `ProviderRouterStrategy`:
```java
@Component
public class GeoLocationStrategy implements ProviderRouterStrategy {
    @Override
    public RoutingDecision resolve(GatewayExecutionContext context) { ... }
    @Override
    public String name() { return "geo-location"; }
    @Override
    public int order() { return 3; }
    @Override
    public boolean supports(GatewayExecutionContext context) { return true; }
}
```

**Step 2:** Register in `ProviderRouter`:
```java
providerRouter.registerStrategy(geoLocationStrategy);
```

### 4. Add a New Security Hook

**Step 1:** Implement `SecurityHook`:
```java
@Component
public class GeoIpSecurityHook implements SecurityHook {
    @Override
    public AuthenticationResult onAuthenticate(GatewayExecutionContext ctx) { ... }
    @Override
    public String name() { return "geo-ip"; }
    @Override
    public int order() { return 5; }
}
```

**Step 2:** Register in `SecurityManager`:
```java
securityManager.registerHook(geoIpSecurityHook);
```

### 5. Add a New Fallback Strategy

**Step 1:** Implement fallback logic in `FallbackSelectionStrategy` or create a new selector strategy:
```java
@Component
public class CustomFallbackStrategy implements SelectionStrategy {
    @Override
    public AIProvider select(List<AIProvider> providers, SelectionContext context) { ... }
}
```

### 6. Add Custom Metrics

**Step 1:** Add fields to `GatewayMetrics` record:
```java
public record GatewayMetrics(
    // ... existing fields
    double costPerRequest,
    int cacheHitRate
) { ... }
```

**Step 2:** Record in `GatewayMetricsCollector`:
```java
meterRegistry.counter("ai.gateway.cache.hits").increment();
```

### 7. Add a New Error Type

**Step 1:** Add factory method to `StandardGatewayError`:
```java
public static StandardGatewayError providerDegraded(String providerId) {
    return new StandardGatewayError("PROVIDER_DEGRADED",
        "Provider " + providerId + " is degraded", 503,
        providerId, "DEGRADED", true, null);
}
```

## Testing Patterns

### Unit Test Pattern
```java
@ExtendWith(MockitoExtension.class)
class MyServiceTest {
    @Mock private Dependency dependency;
    private MyService service;

    @BeforeEach
    void setUp() {
        service = new MyService(dependency);
    }

    @Test
    void shouldDoSomething() {
        when(dependency.call()).thenReturn(expected);
        var result = service.doSomething();
        assertThat(result).isEqualTo(expected);
    }
}
```

### Integration Test Pattern
```java
@ExtendWith(MockitoExtension.class)
class MyIntegrationTest {
    @Mock private ExternalService external;
    // System under test with real implementations + mocked edges

    @Test
    void shouldHandleFullFlow() {
        // Arrange
        // Act
        // Assert
    }
}
```

### Concurrency Test Pattern
```java
@Test
void shouldHandleConcurrentLoad() throws Exception {
    int threads = 100;
    ExecutorService executor = Executors.newFixedThreadPool(20);
    CountDownLatch latch = new CountDownLatch(threads);
    AtomicInteger success = new AtomicInteger(0);

    for (int i = 0; i < threads; i++) {
        executor.submit(() -> {
            try { success.incrementAndGet(); }
            finally { latch.countDown(); }
        });
    }

    assertThat(latch.await(30, TimeUnit.SECONDS)).isTrue();
    executor.shutdown();
}
```

### Performance Benchmark Pattern
```java
@Test
@Tag("benchmark")
void benchmarkLatency() {
    warmup(); // JVM warmup
    List<Long> latencies = new ArrayList<>();
    for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
        long start = System.nanoTime();
        // operation
        latencies.add(System.nanoTime() - start);
    }
    // Report avg, P95, P99, throughput
}
```

## Contract Tests

All providers must pass identical contract tests defined in `GatewayIntegrationTest`:

- Provider resolution works
- Request/response adaptation works
- Timeout handling works
- Rate limit handling works
- Error mapping to `StandardGatewayError` works

## Architecture Diagrams

### Gateway Pipeline Flow

```
┌─────────────────────────────────────────────────────────────────────┐
│                        AI GATEWAY PIPELINE                           │
│                                                                      │
│  ┌──────────┐  ┌──────────────┐  ┌───────────┐  ┌───────────────┐  │
│  │VALIDATION│→ │AUTHENTICATION│→ │AUTHORIZATN│→ │  QUOTA_CHECK  │  │
│  └──────────┘  └──────────────┘  └───────────┘  └───────────────┘  │
│       ↓              ↓               ↓               ↓              │
│  ┌──────────┐  ┌──────────────┐  ┌───────────┐  ┌───────────────┐  │
│  │RATE_LIMIT│→ │PROVIDER_SELEC│→ │  ROUTING  │→ │  EXECUTION    │  │
│  └──────────┘  └──────────────┘  └───────────┘  └───────────────┘  │
│       ↓              ↓               ↓               ↓              │
│  ┌──────────┐  ┌──────────────┐  ┌───────────┐                      │
│  │POST_PROC │→ │    AUDIT     │→ │  METRICS  │→ Response            │
│  └──────────┘  └──────────────┘  └───────────┘                      │
└─────────────────────────────────────────────────────────────────────┘
```

### Provider Resolution Flow

```
Request → ProviderResolver
              │
              ├── MOCK (always available)
              ├── OPENAI (if configured)
              ├── GEMINI (if configured)
              ├── CLAUDE (if configured)
              ├── AZURE (if configured)
              └── CUSTOM (if configured)
                      │
                      ▼
              CircuitBreaker check
                      │
              ┌──── OPEN ────→ StandardGatewayError.circuitOpen()
              │
              ▼ CLOSED/HALF_OPEN
              Execution with retry
                      │
              ┌── SUCCESS ──→ response
              │
              └── FAILURE ──→ RetryStrategy
                                  │
                          ┌── retries exhausted ──→ FallbackStrategy
                          │                              │
                          └── retry available ──────→ retry
```

### Fallback Chain

```
Primary Provider (e.g., GEMINI)
       │
       ▼ (failure)
Fallback 1 (OPENAI)
       │
       ▼ (failure)
Fallback 2 (CLAUDE)
       │
       ▼ (failure)
Fallback n (MOCK — always succeeds)
```

## Performance Baselines

| Metric | Baseline | Measured |
|--------|----------|----------|
| Gateway overhead (avg) | < 50 ms | Benchmark |
| P95 latency | < 100 ms | Benchmark |
| P99 latency | < 200 ms | Benchmark |
| Throughput | > 1000 req/s | Benchmark |
| Memory per response | < 10 KB | Benchmark |
| Serialization | < 10 µs | Benchmark |
| Provider routing | < 10 µs | Benchmark |

## Security Baselines

| Requirement | Status |
|-------------|--------|
| API keys never in error responses | ✓ Enforced |
| Secrets never in logs | ✓ Enforced |
| No stack traces in responses | ✓ Enforced |
| XSS prevention | ✓ Hook available |
| Header injection prevention | ✓ Validated |
| Prompt injection detection | ✓ Framework ready |
| DoS resilience | ✓ Rate limiting + circuit breaker |
| Input sanitization | ✓ Hook available |
