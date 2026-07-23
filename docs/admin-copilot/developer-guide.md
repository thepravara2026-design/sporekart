# Developer Guide

## Quick Start

### Prerequisites

- Java 17+
- Maven 3.9+
- Docker Desktop (for local service dependencies)
- Access to SporeKart artifact registry

### Clone and Build

```bash
git clone https://github.com/sporekart/platform.git
cd platform/admin-copilot-service

# Build the service
mvn clean install -DskipTests

# Run tests
mvn test

# Run locally with in-memory dependencies
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Verify the Service

```bash
# Health check
curl http://localhost:8100/actuator/health

# Dashboard endpoint
curl http://localhost:8100/api/v1/copilot/admin/dashboard \
  -H "Authorization: Bearer <dev-token>"

# Chat endpoint
curl -X POST http://localhost:8100/api/v1/copilot/admin/chat \
  -H "Authorization: Bearer <dev-token>" \
  -H "Content-Type: application/json" \
  -d '{"message": "show me the dashboard"}'
```

### Project Structure

```
admin-copilot-service/
├── src/
│   ├── main/
│   │   ├── java/com/sporekart/copilot/admin/
│   │   │   ├── AdminCopilotApplication.java
│   │   │   ├── config/
│   │   │   │   ├── CopilotConfig.java
│   │   │   │   ├── WebConfig.java
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── interfaces/
│   │   │   │   └── rest/
│   │   │   │       ├── AdminCopilotController.java
│   │   │   │       └── dto/
│   │   │   │           ├── ChatRequest.java
│   │   │   │           ├── ChatResponse.java
│   │   │   │           ├── ReportRequest.java
│   │   │   │           └── ForecastRequest.java
│   │   │   ├── domain/
│   │   │   │   ├── model/
│   │   │   │   │   ├── AdminDashboard.java
│   │   │   │   │   ├── BusinessInsight.java
│   │   │   │   │   ├── ForecastResult.java
│   │   │   │   │   ├── OperationalAlert.java
│   │   │   │   │   └── PerformanceReport.java
│   │   │   │   └── service/
│   │   │   │       ├── CopilotEngine.java
│   │   │   │       ├── AnalyticsEngine.java
│   │   │   │       ├── BusinessInsightsEngine.java
│   │   │   │       └── ForecastingEngine.java
│   │   │   ├── application/
│   │   │   │   └── service/
│   │   │   │       ├── AdminCopilotService.java
│   │   │   │       ├── ReportService.java
│   │   │   │       └── AlertService.java
│   │   │   └── infrastructure/
│   │   │       ├── client/
│   │   │       │   ├── AnalyticsServiceClient.java
│   │   │       │   ├── OrderServiceClient.java
│   │   │       │   └── InventoryServiceClient.java
│   │   │       ├── cache/
│   │   │       │   └── InsightCacheManager.java
│   │   │       ├── messaging/
│   │   │       │   └── AuditEventPublisher.java
│   │   │       └── persistence/
│   │   │           ├── ReportJobRepository.java
│   │   │           └── AlertRepository.java
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       └── db/migration/
│   └── test/
│       └── java/com/sporekart/copilot/admin/
│           ├── interfaces/rest/
│           │   └── AdminCopilotControllerTest.java
│           ├── domain/service/
│           │   ├── CopilotEngineTest.java
│           │   ├── AnalyticsEngineTest.java
│           │   └── BusinessInsightsEngineTest.java
│           └── application/service/
│               └── AdminCopilotServiceTest.java
└── pom.xml
```

## How Admin Copilot Uses the Enterprise Copilot Framework

The Admin Copilot is a specialized copilot that integrates with the shared Enterprise Copilot Framework (`shared-copilot` module). The framework provides:

### Intent Resolution

The `CopilotOrchestrator` (in `ai-service`) resolves user intent and routes to the appropriate copilot:

```java
public class CopilotOrchestratorImpl implements CopilotOrchestrator {
    private final Map<String, CopilotType> intentCopilotMap;

    public CopilotOrchestratorImpl() {
        intentCopilotMap = new ConcurrentHashMap<>();
        intentCopilotMap.put("analytics_report", CopilotType.ANALYTICS);
        intentCopilotMap.put("account_admin", CopilotType.ADMINISTRATION);
        // ...
    }

    public CopilotType resolveCopilot(String intent) {
        return intentCopilotMap.getOrDefault(intent, CopilotType.SUPPORT);
    }
}
```

The Admin Copilot handles intents routed to `CopilotType.ADMINISTRATION` and `CopilotType.ANALYTICS`. When the framework identifies an admin or analytics intent, it forwards the request to the Admin Copilot service endpoint.

### CopilotEngine Integration

The `CopilotEngine` is the central orchestrator within the Admin Copilot service:

```java
@Service
public class CopilotEngine {
    private final IntentParser intentParser;
    private final AnalyticsEngine analyticsEngine;
    private final BusinessInsightsEngine insightsEngine;
    private final ForecastingEngine forecastingEngine;
    private final AuditEventPublisher auditPublisher;

    public CopilotResponse handleMessage(ChatRequest request) {
        Intent intent = intentParser.parse(request.message());

        return switch (intent.getType()) {
            case DASHBOARD_QUERY -> handleDashboardQuery(intent);
            case SALES_QUERY -> handleSalesQuery(intent);
            case INSIGHT_REQUEST -> handleInsightRequest(intent);
            case FORECAST_REQUEST -> handleForecastRequest(intent);
            case REPORT_REQUEST -> handleReportRequest(intent);
            case ALERT_QUERY -> handleAlertQuery(intent);
            default -> handleGeneralQuery(intent);
        };
    }
}
```

### Shared Components

The Admin Copilot reuses these shared framework components:

| Component | Location | Purpose |
|---|---|---|
| `CopilotType` | `shared-copilot` | Enum of copilot types for intent routing |
| `CorrelationIdFilter` | `shared-copilot` | Injects correlation IDs into request context |
| `AuditEventPublisher` | `shared-copilot` | Publishes audit events to Kafka |
| `ProblemDetails` | `shared-errors` | Standardized error response format |
| `ValidationUtils` | `shared-utils` | Input validation utilities |

## Adding New Analytics Metrics

### 1. Define the Metric

Add the metric ID to the metrics registry:

```java
public enum AnalyticsMetric {
    REVENUE("revenue", "Total Revenue", MetricCategory.SALES),
    ORDERS("orders", "Order Count", MetricCategory.SALES),
    AOV("aov", "Average Order Value", MetricCategory.SALES),
    // Add new metric:
    GROSS_PROFIT("gross_profit", "Gross Profit", MetricCategory.SALES);

    private final String id;
    private final String displayName;
    private final MetricCategory category;
}
```

### 2. Implement the Data Fetcher

Create a fetcher for the new metric:

```java
@Component
public class GrossProfitFetcher implements MetricDataFetcher {
    private final OrderServiceClient orderClient;
    private final AnalyticsServiceClient analyticsClient;

    @Override
    public String getMetricId() {
        return "gross_profit";
    }

    @Override
    public MetricResult fetch(Period period, Map<String, String> filters) {
        BigDecimal revenue = orderClient.getRevenue(period);
        BigDecimal cogs = analyticsClient.getCOGS(period, filters);
        BigDecimal grossProfit = revenue.subtract(cogs);
        BigDecimal margin = grossProfit.divide(revenue, 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100));

        return new MetricResult(grossProfit, Map.of("margin", margin));
    }
}
```

### 3. Register in AnalyticsEngine

```java
@Component
public class AnalyticsEngine {
    private final Map<String, MetricDataFetcher> fetchers;

    public AnalyticsEngine(List<MetricDataFetcher> fetcherList) {
        this.fetchers = fetcherList.stream()
            .collect(Collectors.toMap(MetricDataFetcher::getMetricId, f -> f));
    }

    public MetricResult getMetric(String metricId, Period period, Map<String, String> filters) {
        MetricDataFetcher fetcher = fetchers.get(metricId);
        if (fetcher == null) {
            throw new UnknownMetricException(metricId);
        }
        return fetcher.fetch(period, filters);
    }
}
```

### 4. Add Dashboard Display

Update the `AdminDashboard` model to include the new metric:

```java
public record AdminDashboard(
    // existing fields...
    @JsonProperty("grossProfit") BigDecimal grossProfit,
    @JsonProperty("grossMargin") BigDecimal grossMargin
) {}
```

### 5. Add Tests

```java
@Test
void grossProfitIsRevenueMinusCogs() {
    when(orderClient.getRevenue(any())).thenReturn(BigDecimal.valueOf(100000));
    when(analyticsClient.getCOGS(any(), any())).thenReturn(BigDecimal.valueOf(60000));

    MetricResult result = fetcher.fetch(testPeriod, Map.of());

    assertEquals(BigDecimal.valueOf(40000), result.value());
    assertEquals(40.0, result.metadata().get("margin"));
}
```

## Creating Custom Reports

### 1. Define Report Type

```java
public enum ReportType {
    DASHBOARD_SNAPSHOT("dashboard_snapshot"),
    SALES("sales"),
    INVENTORY("inventory"),
    CUSTOMER("customer"),
    TRAINING("training"),
    // Add custom:
    GROSS_PROFIT_ANALYSIS("gross_profit_analysis");

    private final String id;
}
```

### 2. Implement Report Generator

```java
@Component
public class GrossProfitReportGenerator implements ReportGenerator {
    @Override
    public String getReportType() {
        return "gross_profit_analysis";
    }

    @Override
    public ReportData generate(ReportRequest request) {
        Period period = new Period(request.periodStart(), request.periodEnd());
        List<GrossProfitRow> rows = fetchGrossProfitData(period, request.filters());

        List<String> headers = List.of("Date", "Product", "Revenue", "COGS", "Gross Profit", "Margin");
        List<List<Object>> data = rows.stream()
            .map(r -> List.of(r.date(), r.product(), r.revenue(), r.cogs(), r.grossProfit(), r.margin()))
            .toList();

        return new ReportData(headers, data, buildSummary(rows));
    }
}
```

### 3. Register Generator

```java
@Component
public class ReportService {
    private final Map<String, ReportGenerator> generators;

    public ReportService(List<ReportGenerator> generatorList) {
        this.generators = generatorList.stream()
            .collect(Collectors.toMap(ReportGenerator::getReportType, g -> g));
    }
}
```

## Extending Forecasting Models

### 1. Implement Algorithm

```java
@Component
public class ThetaForecastMethod implements ForecastAlgorithm {
    @Override
    public String getAlgorithmName() {
        return "theta";
    }

    @Override
    public ForecastResult forecast(List<DataPoint> history, int horizon,
                                    List<Integer> confidenceLevels) {
        // Theta method implementation
        double theta = calculateTheta(history);
        List<ForecastPoint> points = new ArrayList<>();

        for (int i = 1; i <= horizon; i++) {
            double forecast = history.get(history.size() - 1).value() + theta * i;
            double[] intervals = calculateIntervals(history, forecast, confidenceLevels);
            points.add(new ForecastPoint(i, forecast, intervals));
        }

        return new ForecastResult(points, calculateMetrics(history, points));
    }
}
```

### 2. Register in ForecastingEngine

```java
@Component
public class ForecastingEngine {
    private final Map<String, ForecastAlgorithm> algorithms;
    private final AlgorithmSelector selector;

    public ForecastingEngine(List<ForecastAlgorithm> algorithmList) {
        this.algorithms = algorithmList.stream()
            .collect(Collectors.toMap(ForecastAlgorithm::getAlgorithmName, a -> a));
        this.selector = new AlgorithmSelector(algorithms);
    }

    public ForecastResult forecast(ForecastRequest request) {
        List<DataPoint> history = loadHistory(request);
        String algorithm = resolveAlgorithm(request, history);
        return algorithms.get(algorithm).forecast(history, request.horizon(),
            request.confidenceLevels());
    }
}
```

## Adding Alert Rules

### 1. Define Alert Rule

```java
public class AlertRule {
    private final String id;
    private final String metric;
    private final ThresholdType type;  // ABSOLUTE, PERCENTAGE, Z_SCORE
    private final double warningThreshold;
    private final double criticalThreshold;
    private final int minDurationMinutes;
    private final List<String> notificationChannels;
}
```

### 2. Configure via YAML or API

```yaml
sporekart:
  copilot:
    admin:
      alerts:
        rules:
          - id: "payment-success-rate"
            metric: "payment_success_rate"
            type: ABSOLUTE
            warningThreshold: 95.0
            criticalThreshold: 90.0
            minDurationMinutes: 5
            channels: ["email", "pagerduty"]
```

### 3. Implement Rule Evaluator

```java
@Component
public class AlertRuleEvaluator {
    private final List<AlertRule> rules;
    private final AlertRepository alertRepository;

    public void evaluateAll(Map<String, Double> currentMetrics) {
        for (AlertRule rule : rules) {
            Double value = currentMetrics.get(rule.metric());
            if (value == null) continue;

            Alert.Severity severity = evaluateThreshold(rule, value);
            if (severity != null) {
                createOrUpdateAlert(rule, value, severity);
            }
        }
    }
}
```

## Testing Guide

### Unit Tests

```java
@ExtendWith(MockitoExtension.class)
class CopilotEngineTest {
    @Mock private IntentParser intentParser;
    @Mock private AnalyticsEngine analyticsEngine;
    @InjectMocks private CopilotEngine copilotEngine;

    @Test
    void dashboardQueryReturnsDashboardData() {
        Intent intent = new Intent(IntentType.DASHBOARD_QUERY, null);
        when(intentParser.parse("show dashboard")).thenReturn(intent);
        when(analyticsEngine.getDashboard(any())).thenReturn(mockDashboard());

        ChatResponse response = copilotEngine.handleMessage(
            new ChatRequest("show dashboard", null));

        assertThat(response.reply()).contains("revenue");
        assertThat(response.structuredData()).isNotNull();
    }

    @Test
    void unknownIntentReturnsGeneralResponse() {
        Intent intent = new Intent(IntentType.UNKNOWN, null);
        when(intentParser.parse("hello")).thenReturn(intent);

        ChatResponse response = copilotEngine.handleMessage(
            new ChatRequest("hello", null));

        assertThat(response.reply()).contains("How can I help");
    }
}
```

### Integration Tests

```java
@SpringBootTest
@AutoConfigureMockMvc
class AdminCopilotControllerTest {
    @Autowired private MockMvc mockMvc;

    @Test
    void dashboardEndpointReturns200() throws Exception {
        mockMvc.perform(get("/api/v1/copilot/admin/dashboard")
                .header("Authorization", "Bearer " + getAdminToken()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.sales.revenueToday").isNumber())
            .andExpect(jsonPath("$.sales.ordersToday").isNumber());
    }

    @Test
    void chatEndpointReturnsReply() throws Exception {
        mockMvc.perform(post("/api/v1/copilot/admin/chat")
                .header("Authorization", "Bearer " + getAdminToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"message": "sales yesterday", "context": {}}
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.reply").isString());
    }

    @Test
    void analystRoleCannotAccessAlerts() throws Exception {
        mockMvc.perform(get("/api/v1/copilot/admin/alerts")
                .header("Authorization", "Bearer " + getAnalystToken()))
            .andExpect(status().isForbidden());
    }
}
```

### Test Fixtures

```java
public class TestDataFactory {
    public static ChatRequest aChatRequest(String message) {
        return new ChatRequest(message, Map.of("timezone", "UTC"));
    }

    public static AdminDashboard aDashboard() {
        return new AdminDashboard(
            Instant.now(),
            new Period(Instant.now(), Instant.now()),
            new SalesSummary(42500.00, 142, 47.50, 3.8, List.of()),
            // ...
        );
    }

    public static ForecastResult aForecast() {
        return new ForecastResult("revenue", 30, List.of(
            new ForecastPoint("2026-07-23", 41200.00, 39800.00, 42600.00, 38500.00, 43900.00)
        ), new ForecastSummary(1245000.00, 41500.00, "Holt-Winters", 4.2));
    }
}
```

### Running Tests

```bash
# All tests
mvn test

# Specific test class
mvn test -Dtest=AnalyticsEngineTest

# Integration tests only
mvn test -Dtest=*ControllerTest -P integration

# With coverage report
mvn verify -P coverage
# Report: target/site/jacoco/index.html
```

### Testing Checklist

- [ ] Unit test each domain service (CopilotEngine, AnalyticsEngine, InsightsEngine, ForecastingEngine)
- [ ] Unit test each client class with mocked HTTP responses
- [ ] Controller integration tests for all 7 endpoints
- [ ] Security tests for each role (ADMIN, ANALYST, unauthenticated)
- [ ] Error path tests (404, 502, rate limiting)
- [ ] Request validation tests (null fields, out-of-range values)
- [ ] Report job lifecycle tests (pending -> completed -> download -> expiration)
- [ ] Alert evaluation tests (threshold crossing, severity escalation, deduplication)
