# BI Copilot Developer Guide

## Setup

### Prerequisites
- Java 21+
- Maven 3.9+
- Spring Boot 3.x

### Build and Run
```bash
cd bi-copilot-service
mvn clean install
mvn spring-boot:run
```

The service starts on port **8104**.

### Configuration

See `src/main/resources/application.yml` — all `sporekart.bi.copilot.*` properties are configurable via environment variables or Spring Cloud Config.

## Project Structure

```
bi-copilot-service/
├── src/main/java/com/sporekart/bi/copilot/
│   ├── BiCopilotApplication.java
│   ├── config/
│   │   ├── BiCopilotConfig.java
│   │   ├── OpenApiConfig.java
│   │   └── SecurityConfig.java
│   ├── controller/          # REST controllers (to be implemented)
│   ├── domain/              # Domain records (12)
│   ├── dto/                 # Request/Response DTOs (14)
│   ├── engine/              # Analytics engines (12)
│   ├── infrastructure/
│   │   ├── data/            # DataAggregationClient
│   │   └── monitoring/      # BiMetricsService
│   └── service/             # Service layer (to be implemented)
└── src/test/
```

## Extending with New Data Sources

1. **Create a domain record** in `domain/` — define the data shape
2. **Create an engine** in `engine/` — implement analytics logic
3. **Create DTOs** in `dto/` — request/response shapes
4. **Add controller endpoints** in `controller/` — map to service methods
5. **Register in service layer** — wire engine to service facade
6. **Add OpenAPI spec** — update `contracts/openapi/bi-copilot-service.yaml`

## Adding Custom Metrics

1. Extend the appropriate engine with a new public method
2. Add optional DTO fields if new query parameters are needed
3. Register the metric in the `BiMetricsService` for Prometheus monitoring
4. Cross-copilot metrics can be added to `CrossCopilotIntelligenceEngine`

## Engine Convention

Each engine:
- Is a Spring `@Component`
- Works with seeded data by default (replaceable with real data sources)
- Exposes domain-specific analytics via public methods
- Logs initialization with data point counts
- Uses deterministic random seeds for reproducible demo data

## Testing

Tests are in `src/test/`. Run with:
```bash
mvn test
```

## API Contract

The canonical OpenAPI 3.0 specification lives at:
```
contracts/openapi/bi-copilot-service.yaml
```

Always update the spec in parallel with code changes.

## Conventions

- Domain records use Java `record` with immutable fields
- DTOs use Java `record` with Jakarta validation annotations
- Engines log at DEBUG for method entry/exit and INFO for initialization
- APIs return `application/json` by default, `application/problem+json` for errors
- All endpoints are prefixed with `/v1/bi/`
