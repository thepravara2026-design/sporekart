# Grower Copilot — Developer Guide

## Prerequisites

- Java 17+ (JDK 17.0.2 or later)
- Maven 3.8+ (or Gradle 7.4+)
- Docker Desktop 20.10+
- Access to SporeKart Enterprise AI Platform (dev/staging endpoints)
- IDE: IntelliJ IDEA 2023+ (recommended) or VS Code with Java extension pack

## Project Structure

```
grower-copilot-service/
├── src/
│   ├── main/
│   │   ├── java/com/sporekart/grower/copilot/
│   │   │   ├── config/           # Spring configuration, beans
│   │   │   ├── controller/       # REST controllers
│   │   │   ├── engine/           # Business engines (9 engines)
│   │   │   ├── model/            # Domain models, DTOs
│   │   │   ├── provider/         # Weather provider abstraction
│   │   │   ├── repository/       # Data access
│   │   │   ├── service/          # Service layer, orchestration
│   │   │   ├── client/           # HTTP/gRPC clients (AI platform, knowledge)
│   │   │   └── GrowerCopilotApplication.java
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       ├── application-staging.yml
│   │       └── application-production.yml
│   └── test/
│       ├── java/com/sporekart/grower/copilot/
│       │   ├── engine/           # Engine unit tests
│       │   ├── controller/       # Controller integration tests
│       │   └── provider/         # Provider tests
│       └── resources/
│           └── test-data/        # Fixtures for tests
├── Dockerfile
├── docker-compose.yml
└── pom.xml (or build.gradle)
```

## Setup

```bash
# Clone the repository
git clone <repo-url>
cd sporekart/grower-copilot-service

# Build the project
mvn clean install -DskipTests

# Run tests
mvn test

# Run locally with dev profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Build Docker image
docker build -t sporekart/grower-copilot-service .

# Run with Docker Compose (includes dependencies)
docker-compose up -d
```

## Configuration

Key configuration properties in `application.yml`:

```yaml
server:
  port: 8102

grower-copilot:
  weather:
    provider: simulated   # simulated | open-meteo
  ai-platform:
    endpoint: http://localhost:9000
    grpc-port: 9001
    api-key: ${AI_PLATFORM_API_KEY}
  knowledge:
    endpoint: http://localhost:8200
  engines:
    disease:
      min-confidence: 0.5
      escalation-enabled: true
    yield:
      default-flushes: 6
    cultivation:
      strict-mode: false   # true = fail on missing params
```

## Building and Testing

```bash
# Unit tests only (fast)
mvn test -Dtest="*EngineTest"

# Integration tests (requires Docker dependencies)
mvn verify -Pintegration

# Full build with checks
mvn clean verify

# Checkstyle and linting
mvn checkstyle:check

# Dependency vulnerability scan
mvn dependency-check:check
```

## Extending Engines

### Adding a new engine

1. Create a new class in `engine/` implementing the `Engine` interface:

```java
@Component
public class MyNewEngine implements Engine {
    @Override
    public String getEngineId() { return "my_new_engine"; }

    @Override
    public EngineResult execute(EngineRequest request) {
        // Business logic here
        return EngineResult.builder()
            .engineId(getEngineId())
            .success(true)
            .data(...)
            .build();
    }
}
```

2. Register the engine in `EngineRegistry` (auto-detected via `@Component` scanning).
3. Add the engine to the health check in `HealthController`.
4. Write unit tests in `src/test/java/.../engine/MyNewEngineTest.java`.
5. Add any new configuration properties to `application.yml`.

### Engine interface contract

```java
public interface Engine {
    String getEngineId();
    EngineResult execute(EngineRequest request);
    default boolean isAvailable() { return true; }
    default int getPriority() { return 0; }
}
```

## Adding New Weather Providers

1. Implement the `WeatherProvider` interface:

```java
@Component
@ConditionalOnProperty(name = "grower-copilot.weather.provider", havingValue = "my-provider")
public class MyWeatherProvider implements WeatherProvider {
    // Implement all interface methods
}
```

2. Add provider configuration section to `application.yml`:

```yaml
grower-copilot:
  weather:
    provider: my-provider
    my-provider:
      api-key: ${MY_WEATHER_API_KEY}
      endpoint: https://api.myweather.com/v1
```

3. The `WeatherEngine` auto-discovers the active provider via Spring's `@ConditionalOnProperty`.
4. Fallback to `SimulatedWeatherProvider` when the primary provider is unavailable (handled by `WeatherEngine`).

## API Contract Changes

1. Update the OpenAPI spec at `contracts/openapi/grower-copilot-service.yaml`.
2. Generate Java DTOs from the spec (Maven plugin: `openapi-generator-maven-plugin`).
3. Update or add controller methods.
4. Write integration tests using `@WebMvcTest` or `TestRestTemplate`.

## Coding Conventions

- Follow [SporeKart Java Style Guide](https://wiki.sporekart.example/java-style-guide)
- Use Lombok (`@Data`, `@Builder`, `@Slf4j`) for DTOs and logging
- Use MapStruct for DTO mapping
- Use JUnit 5 + Mockito for unit tests
- Use Testcontainers for integration tests
- Prefer constructor injection over field injection
- Keep controller methods thin; delegate to services/engines
