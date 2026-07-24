# Engineering Standards

## Architecture Principles

- **Clean Architecture** / **Hexagonal Architecture** — Domain innermost, interfaces outermost
- **SOLID** — Single responsibility, open-closed, Liskov substitution, interface segregation, dependency inversion
- **DRY** — Extract once, share everywhere via shared-platform modules
- **KISS** — Simple over clever, explicit over implicit
- **YAGNI** — Build only what's needed now; extensible via interfaces

## Package Convention

```
com.sporekart.{service}
├── application/
│   ├── dto/          # Request/Response records
│   └── service/      # Application services (@Service)
├── common/           # (deprecated — use shared-platform)
├── config/           # Spring configuration
├── domain/
│   ├── model/        # Domain entities, value objects, enums
│   └── repository/   # Repository port interfaces
├── infrastructure/
│   └── persistence/  # Repository implementations
└── interfaces/
    └── rest/         # REST controllers
```

## Naming Conventions

| Artifact | Convention | Example |
|----------|-----------|---------|
| Application class | `{Domain}ServiceApplication` | `CartServiceApplication` |
| Controller | `{Entity}Controller` | `OrderController` |
| Service | `{Entity}Service` | `OrderService` |
| Repository Port | `{Entity}RepositoryPort` | `OrderRepositoryPort` |
| Repository Impl | `Jpa{Entity}RepositoryAdapter` | `JpaOrderRepositoryAdapter` |
| DTO Request | `{Action}{Entity}Request` | `CreateOrderRequest` |
| DTO Response | `{Entity}Response` | `OrderResponse` |
| Exception | `{Name}Exception` | `NotFoundException` |
| Domain Event | Past tense verb | `OrderCreated` |

## Error Handling

All services must use `shared-platform` exception hierarchy:
- `BusinessException` (422) — Business rule violations
- `NotFoundException` (404) — Resource not found
- `ValidationException` (400) — Input validation failures
- `DuplicateResourceException` (409) — Resource conflicts
- `SecurityException` (401/403) — Auth/authz failures
- `InfrastructureException` (500) — Technical failures

No ad-hoc `IllegalArgumentException` in controllers. Use `GlobalExceptionHandler` from `shared-platform`.

## API Responses

All REST APIs must return standardized responses:
- `ApiResponse<T>` for single resources
- `ApiPageResponse<T>` for paginated collections
- `ProblemDetails` for errors (RFC 9457)

## Validation

Use `BusinessValidator` for programmatic validation:
```java
BusinessValidator.create()
    .requireNonBlank(name, "name", "Name is required")
    .requirePositive(price, "price", "Price must be positive")
    .validate();
```

## Security

- Use `@PreAuthorize` for role-based access control
- Use `SharedSecurityConfig` with `buildWithPublicEndpoints()` instead of copying SecurityConfig
- Use `PermissionValidator` for programmatic permission checks
- Never copy-paste `SecurityConfig` — use the shared base

## Logging

- `CorrelationIdFilter` handles MDC correlation IDs automatically
- Use `PerformanceLogger` for timing operations
- Use `AuditLogger` for audit events
- Use `SecurityLogger` for security events
