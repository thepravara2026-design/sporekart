# Developer Guide — Enterprise AI Platform

## Getting Started

### Prerequisites

- JDK 21
- Docker (for PostgreSQL, Redis, Kafka)
- Maven 3.9+

### Local Development

```bash
# Start infrastructure
docker compose up -d postgres redis kafka

# Run the service locally
cd services/ai-service
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### Build

```bash
mvn clean install
```

### Run Tests

```bash
mvn test
```

### Architecture Tests

```bash
mvn test -Dtest="com.sporekart.ai.architecture.*"
```

## Project Structure

```
services/ai-service/
├── src/main/java/com/sporekart/ai/
│   ├── {module}/                     # Bounded context module
│   │   ├── api/                      # Inbound port interfaces
│   │   ├── application/              # @Service implementations
│   │   ├── config/                   # @ConfigurationProperties
│   │   ├── domain/                   # Records/enums (pure Java)
│   │   ├── infrastructure/           # Outbound adapters
│   │   └── interfaces/rest/          # @RestController
│   ├── events/                       # Domain/Integration events
│   ├── shared/                       # Shared constants, enums, utilities
│   └── config/                       # Global config
```

## Module Creation Checklist

When adding a new module:

1. Create package under `com.sporekart.ai.{module}`
2. Add sub-packages: `api`, `application`, `config`, `domain`, `infrastructure`, `interfaces/rest/dto`
3. Define domain records/enums in `domain/`
4. Define API interfaces in `api/`
5. Implement services in `application/`
6. Add JPA entities/repositories in `infrastructure/persistence/`
7. Add Redis cache service in `infrastructure/redis/`
8. Add Kafka publisher in `infrastructure/kafka/`
9. Add monitoring service in `infrastructure/monitoring/`
10. Add REST controller in `interfaces/rest/`
11. Add DTO records in `interfaces/rest/dto/`
12. Add @ConfigurationProperties in `config/`
13. Add feature flag in `application.yml` under `sporekart.ai.features.*`
14. Add module config in `application.yml` under `sporekart.ai.modules.*`
15. Add unit tests in `src/test/java/com/sporekart/ai/{module}/`
16. Add architecture tests in `src/test/java/com/sporekart/ai/architecture/`

## Coding Standards

- Java 21 records for DTOs and domain models
- Constructor injection (no @Autowired on fields)
- No business logic in controllers
- Domain layer has zero framework dependencies
- All config values externalized with sensible defaults
- Kafka events follow `{module}-events` topic naming
- Redis cache keys follow `ai:{module}:{key}` pattern

## Architecture Rules

- Modules must NOT have circular dependencies
- Domain layer must NOT depend on Spring/Infrastructure
- API layer (interfaces) must NOT depend on Infrastructure
- Shared module must NOT depend on any application module
- New modules must NOT break existing module boundaries
