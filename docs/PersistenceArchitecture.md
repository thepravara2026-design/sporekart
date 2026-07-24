# Persistence Architecture

## Architecture Pattern

Hexagonal Architecture (Ports & Adapters) applied to persistence:

```
┌─────────────────────────────────────────────┐
│               Domain Layer                   │
│  ┌─────────────────────────────────────┐    │
│  │  domain/repository/RepositoryPort   │    │
│  │  domain/model/ (Entities, VOs)      │    │
│  └─────────────────────────────────────┘    │
├─────────────────────────────────────────────┤
│          Application Layer                   │
│  ┌─────────────────────────────────────┐    │
│  │  application/service/Service        │    │
│  │  Uses port interface only           │    │
│  └─────────────────────────────────────┘    │
├─────────────────────────────────────────────┤
│        Infrastructure Layer                  │
│  ┌─────────────────────────────────────┐    │
│  │  @Primary JPA Adapter (Production)  │    │
│  │  InMemory Adapter (Test/Dev)        │    │
│  │  Spring Data JPA Repository         │    │
│  │  JPA Entity (mapping)              │    │
│  └─────────────────────────────────────┘    │
└─────────────────────────────────────────────┘
```

## Key Design Decisions

1. **@Primary on JPA adapters** — Production always uses JPA; InMemory repos can be activated via `@Profile("test")` for isolated testing
2. **fromDomain()/toDomain()** — Entity classes provide bidirectional mapping between domain models and JPA entities
3. **@Transactional** on every adapter — All repository operations are transactional
4. **UUID primary keys** — All entities use UUID strings (VARCHAR(36)), matching existing Flyway schema
5. **Immutable domain models** — Domain models remain POJO; JPA entities own mutable state for Hibernate

## Database

- **Production**: PostgreSQL (via SPRING_DATASOURCE_URL env vars)
- **Test**: H2 in-memory (MODE=PostgreSQL) via `src/test/resources/application.properties`
- **Migration**: Flyway with `ddl-auto: validate` in production, `create-drop` for tests
- **Connection pool**: HikariCP (Spring Boot default)

## Transaction Boundaries

All repository operations are transactional at the adapter level. Service-layer `@Transactional` annotations provide coarse-grained transaction boundaries for multi-repository operations. Default propagation: REQUIRED.

## Audit Columns

All entities include:
- `created_at` (TIMESTAMP WITH TIME ZONE, NOT NULL, updatable=false)
- `updated_at` (TIMESTAMP WITH TIME ZONE, NOT NULL)
