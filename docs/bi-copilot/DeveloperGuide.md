# Developer Guide

## Setup

### Prerequisites
- Java 21+
- Maven 3.9+
- Access to SporeKart internal Maven registry

### Build & Run

```bash
# Clone and navigate
cd bi-copilot-service

# Build
mvn clean install

# Run locally
mvn spring-boot:run

# Run with profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Package
mvn package -DskipTests

# Run JAR
java -jar target/bi-copilot-service.jar
```

### Configuration

Create `application.yml` or use environment variables:

```yaml
server:
  port: 8104

sporekart:
  bi:
    data-retention-days: 365
    forecast-default-horizon: 90
    dashboard-refresh-seconds: 300
    executive-report-format: pdf,html,json
    health-score-weights:
      revenue: 0.25
      customer: 0.20
      training: 0.15
      inventory: 0.15
      operations: 0.15
      growth: 0.10

spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://identity.sporekart.example
```

### Verification

```bash
# Health check
curl http://localhost:8104/actuator/health

# API docs
open http://localhost:8104/swagger-ui/index.html
```

---

## Project Structure

```
bi-copilot-service/
  src/
    main/
      java/com/sporekart/bi/copilot/
        BiCopilotApplication.java
        config/
          BiCopilotConfig.java        # Configuration properties
          OpenApiConfig.java           # Swagger/OpenAPI config
          SecurityConfig.java          # JWT auth config
        domain/
          CompanyHealthScore.java      # Health score record
          RevenueAnalytics.java        # Revenue analytics record
          CustomerAnalytics.java       # Customer analytics record
          ProductAnalytics.java        # Product analytics record
          InventoryAnalytics.java      # Inventory analytics record
          TrainingAnalytics.java       # Training analytics record
          BusinessForecast.java        # Forecast result record
          DecisionRecommendation.java  # Recommendation record
          BusinessInsight.java         # Insight record
          RiskAlert.java               # Risk alert record
          ExecutiveSummary.java        # Executive summary record
          NaturalLanguageQuery.java    # NLQ record
          VisualizationConfig.java     # Visualization config record
          TrendDataPoint.java          # Trend data point record
        dto/
          ForecastRequest.java
          ForecastResponse.java
          HealthScoreResponse.java
          InsightsRequest.java
          InsightsResponse.java
          DecisionSupportResponse.java
          KpiResponse.java
          DashboardResponse.java
          ReportRequest.java
          ReportResponse.java
          RiskResponse.java
          NaturalLanguageQueryRequest.java
          NaturalLanguageQueryResponse.java
          ChatRequest.java
          ChatResponse.java
        engine/
          RevenueAnalyticsEngine.java
          CustomerAnalyticsEngine.java
          ProductAnalyticsEngine.java
          InventoryAnalyticsEngine.java
          TrainingAnalyticsEngine.java
    resources/
      application.yml
  test/
    java/com/sporekart/bi/copilot/
      engine/
        RevenueAnalyticsEngineTest.java
        CustomerAnalyticsEngineTest.java
        ProductAnalyticsEngineTest.java
        InventoryAnalyticsEngineTest.java
        TrainingAnalyticsEngineTest.java
```

---

## Extending with New Data Sources

### Step 1: Add a Domain Record

Create a new record in the `domain` package:

```java
public record SupplierAnalytics(
    String period,
    int totalSuppliers,
    double onTimeDeliveryRate,
    double qualityScore,
    List<SupplierPerformance> topSuppliers
) {
    public record SupplierPerformance(String supplierId, String name, double score) {}
}
```

### Step 2: Create an Engine

Create a new `@Component` engine in the `engine` package with seed data and query methods:

```java
@Component
public class SupplierAnalyticsEngine {
    // Generate seed data in constructor
    // Implement public query methods
    // Return domain records
}
```

### Step 3: Add DTOs

```java
public record SupplierResponse(
    SupplierAnalytics analytics,
    List<VisualizationConfig> visualizations
) {}
```

### Step 4: Create Controller Endpoint

Add a REST controller with the new endpoint. Follow existing patterns:

```java
@RestController
@RequestMapping("/v1/bi")
public class BiAnalyticsController {
    private final SupplierAnalyticsEngine supplierEngine;

    @GetMapping("/suppliers")
    public ResponseEntity<SupplierResponse> getSupplierAnalytics(
        @RequestParam(defaultValue = "current") String period) {
        // Delegate to engine, wrap in response
    }
}
```

### Step 5: Register in OpenAPI

Update the OpenAPI spec (`contracts/openapi/bi-copilot-service.yaml`) with the new endpoint and schemas.

---

## Adding Custom Metrics

### Option A: Add to Existing Engine

Add a new method to an existing engine (e.g., `RevenueAnalyticsEngine`):

```java
public double getAverageRevenuePerCustomer(String period) {
    return getRevenueSummary(period).grossRevenue() / customerEngine.getCustomerSummary(period).totalCustomers();
}
```

### Option B: Add to Domain Record

Add a new field to an existing domain record (Java record — you'll need to add the field and update seed data / aggregations accordingly).

---

## Adding New Insight Rules

Insight rules are evaluated in the recommendation/insight generation layer. Add rules by creating a `RuleSet` in the respective engine or a dedicated rule engine:

```java
// Example rule added to an engine
public Optional<BusinessInsight> detectRevenueDrop(RevenueAnalytics current, RevenueAnalytics previous) {
    double drop = ((current.grossRevenue() - previous.grossRevenue()) / previous.grossRevenue()) * 100;
    if (drop < -10) {
        return Optional.of(new BusinessInsight(
            UUID.randomUUID().toString(),
            "Revenue Drop Alert",
            String.format("Revenue declined %.1f%% MoM", drop),
            "revenue",
            drop < -20 ? "critical" : "important",
            0.85,
            String.format("Potential revenue loss of INR %.0f", previous.grossRevenue() - current.grossRevenue()),
            List.of("Investigate root cause", "Review pricing strategy"),
            Map.of("dropPct", drop, "period", current.period()),
            OffsetDateTime.now()
        ));
    }
    return Optional.empty();
}
```

## Testing

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=RevenueAnalyticsEngineTest

# Run with coverage
mvn verify
```

Each engine test class should:
- Verify seed data generation (correct number of records)
- Verify period resolution (`current`, specific `yyyy-MM`, fallback)
- Verify metric calculations (growth rates, averages, ratios)
- Verify edge cases (empty data, invalid periods)
- Verify insight/recommendation generation rules
