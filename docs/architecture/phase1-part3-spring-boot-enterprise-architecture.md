# SporeKart Phase 1 Part 3 — Spring Boot Enterprise Architecture and Module Design

- Version: 1.0
- Status: Review complete
- Owner: Enterprise Architecture Review Board
- Review Date: 2026-07-10
- Purpose: Freeze the Java 21 and Spring Boot enterprise architecture baseline.
- Scope: Module structure, layer architecture, package conventions, shared libraries, and dependency rules.
- References: [phase1-part2-service-catalog-domain-design.md](phase1-part2-service-catalog-domain-design.md), [technology-baseline.md](technology-baseline.md)
- Approval Status: Reviewed; implementation guidance only

## 1. Purpose and Scope

This document defines the enterprise Java 21 and Spring Boot 3.x engineering architecture for the SporeKart platform. It is intentionally architecture-only and does not generate production code, Maven project files, Java source files, or runtime implementations.

This blueprint is the mandatory backend engineering standard for all later implementation phases.

## 2. Architecture Principles

The backend architecture is governed by the following principles:

- Java 21 LTS and Spring Boot 3.x are the runtime baseline.
- Spring Security is the sole application authorization framework.
- Supabase is used only for managed PostgreSQL, storage, backups, and point-in-time recovery.
- Every service uses a clear layered architecture with explicit dependency direction.
- Domain logic remains isolated from infrastructure, persistence, and web concerns.
- All cross-service communication is contract-driven and observable.
- Every service must be independently deployable, independently scalable, and independently testable.

## 3. Enterprise Repository Architecture

The repository is organized to support multiple independently deployable services, shared libraries, platform infrastructure, and contract governance.

```text
sporekart/
├── pom.xml
├── services/
│   ├── pom.xml
│   ├── identity-service/
│   ├── catalog-service/
│   ├── inventory-service/
│   ├── cart-service/
│   ├── order-service/
│   ├── payment-service/
│   ├── fulfillment-service/
│   ├── training-service/
│   ├── content-service/
│   ├── notification-service/
│   ├── search-service/
│   ├── analytics-service/
│   ├── risk-service/
│   ├── support-service/
├── shared-libs/
│   ├── shared-core/
│   ├── shared-security/
│   ├── shared-events/
│   ├── shared-validation/
│   ├── shared-logging/
│   ├── shared-observability/
│   ├── shared-testing/
│   ├── shared-exceptions/
│   ├── shared-config/
│   └── shared-common/
├── frontend/
│   ├── buyer-app/
│   └── admin-dashboard/
├── platform/
│   ├── docker/
│   ├── kubernetes/
│   ├── monitoring/
│   ├── observability/
│   └── gateway/
├── contracts/
├── infrastructure/
├── docs/
├── testing/
├── ci/
├── docker/
├── scripts/
└── .github/
```

### Repository ownership

- Services directory: service-specific backend modules.
- Shared-libs: platform-owned reusable infrastructure and cross-cutting libraries.
- Platform: environment, deployment, observability, and gateway concerns.
- Contracts: OpenAPI 3.1, AsyncAPI, event, and schema governance artifacts.
- Docs: architecture and engineering standards.

## 4. Maven Multi-Module Architecture

### 4.1 Root parent structure

The build will be organized around a single Maven parent build that manages shared versions, plugin configuration, dependency management, and release conventions.

### 4.2 Proposed module structure

| Module type | Module name | Purpose |
|---|---|---|
| Parent build | sporekart-parent | Shared dependency management, plugin management, release conventions |
| Service module | sporekart-identity-service | Identity and access concerns |
| Service module | sporekart-catalog-service | Catalog ownership |
| Service module | sporekart-inventory-service | Inventory and reservation ownership |
| Service module | sporekart-cart-service | Ephemeral cart ownership |
| Service module | sporekart-order-service | Order lifecycle and saga orchestration |
| Service module | sporekart-payment-service | Payment and refund ownership |
| Service module | sporekart-fulfillment-service | Shipment and fulfillment ownership |
| Service module | sporekart-training-service | Enrollment and learning lifecycle |
| Service module | sporekart-content-service | Content and publishing ownership |
| Service module | sporekart-notification-service | Notification ownership |
| Service module | sporekart-search-service | Search indexing and retrieval |
| Service module | sporekart-analytics-service | Analytics ingestion and reporting |
| Service module | sporekart-risk-service | Risk scoring and fraud detection |
| Service module | sporekart-support-service | Support and ticket management |
| Shared library | shared-core | Common abstractions and utility types |
| Shared library | shared-security | Security primitives and token utilities |
| Shared library | shared-events | Event contracts and metadata |
| Shared library | shared-validation | Reusable validation rules |
| Shared library | shared-logging | Structured logging helpers |
| Shared library | shared-observability | Tracing and metrics conventions |
| Shared library | shared-testing | Shared test fixtures and base test utilities |
| Shared library | shared-exceptions | Shared exception and problem-details models |
| Shared library | shared-config | Shared configuration contracts and profiles |
| Shared library | shared-common | General-purpose cross-cutting utilities |

### 4.3 Version and dependency management

- Dependency versions are centrally managed by the parent build.
- All service modules inherit the same Spring Boot BOM and Java toolchain version.
- Shared libraries are versioned independently but follow the same release cadence.
- Build order follows service dependency and shared library dependency direction.

### 4.4 Artifact naming

- Service modules use the pattern sporekart-<service-name>-service.
- Shared libraries use the pattern shared-<capability>.
- Maven coordinates must remain stable and versioned explicitly.

## 5. Spring Boot Layer Architecture

Every service will use the same layer structure and dependency rules.

| Layer | Responsibility | Allowed dependencies |
|---|---|---|
| Presentation | Controllers, request mapping, API adapters | Application layer, shared-validation, shared-exceptions |
| Application | Use cases, orchestration, transaction orchestration, ports | Domain layer, infrastructure abstractions |
| Domain | Business rules, aggregates, entities, policies, specifications | No framework or infrastructure dependencies |
| Infrastructure | Adapters for persistence, messaging, security, external clients | Domain contracts, shared libraries |
| Persistence | Repository implementations and persistence mapping | Domain, JPA, Flyway, shared-config |
| Security | Authentication, authorization, filters, method security | Application, domain, shared-security |
| Messaging | Producers, consumers, topic handlers, outbox integration | Domain, shared-events, Kafka abstractions |
| Configuration | Environment configuration, profile binding, bean wiring | Shared-config, Spring Boot |
| Validation | Bean validation, DTO validation, custom validators | Domain, shared-validation |
| Mapper | Domain-to-DTO and DTO-to-domain mapping | Domain, shared-common |
| DTO | Request/response contracts | Shared-validation, shared-exceptions |
| Exception | Error handling, problem details mapping | Shared-exceptions |
| Health | Readiness and liveness reporting | Spring Actuator, shared-observability |
| Monitoring | Metrics, tracing, logging hooks | shared-observability, shared-logging |

### Layer dependency rule

The dependency direction is:

Presentation -> Application -> Domain

Infrastructure -> Domain contracts

Security and Monitoring are cross-cutting and should depend on application and domain abstractions, not the reverse.

## 6. Package Structure Specification

### 6.1 Service package root

Each service will use the root package pattern:

- com.sporekart.identity
- com.sporekart.catalog
- com.sporekart.inventory
- com.sporekart.cart
- com.sporekart.order
- com.sporekart.payment
- com.sporekart.fulfillment
- com.sporekart.training
- com.sporekart.content
- com.sporekart.notification
- com.sporekart.search
- com.sporekart.analytics
- com.sporekart.risk
- com.sporekart.support

### 6.2 Subpackage conventions

| Subpackage | Use |
|---|---|
| api | Controllers, request/response abstractions |
| application | Use cases and orchestration |
| application.command | Command-oriented workflows |
| application.query | Query-oriented workflows |
| domain | Aggregates, entities, value objects, policies |
| domain.model | Core domain model |
| domain.policy | Policy objects |
| domain.service | Domain services |
| domain.specification | Specifications |
| infrastructure | External adapters and technical implementations |
| infrastructure.persistence | JPA entities and repositories |
| infrastructure.messaging | Kafka producers/consumers |
| infrastructure.cache | Redis adapters |
| infrastructure.security | Security adapter implementations |
| infrastructure.external | Third-party integration adapters |
| config | Spring configuration and beans |
| validation | Validation logic |
| mapper | Object mapping |
| dto | DTOs and payload contracts |
| exception | Exception types and handlers |
| health | Health and readiness indicators |
| constants | Shared constants |
| util | Pure utilities |
| event | Event payloads and metadata |

### 6.3 Naming conventions

- Controllers: `<AggregateName>Controller`
- Use cases: `<Action>UseCase`
- Services: `<Boundary>Service`
- Repositories: `<AggregateName>Repository`
- Entities: `<AggregateName>`
- DTOs: `<Action>Request`, `<Action>Response`, `<AggregateName>Dto`
- Exceptions: `<Domain>Exception`, `<Domain>ValidationException`
- Mappers: `<Source>Mapper`
- Events: `<Domain>Event`
- Constants: `<DOMAIN>_CONSTANTS`

## 7. Dependency Matrix

| Component | May depend on | Must not depend on |
|---|---|---|
| Controller | Application, DTO, Validation, shared-exceptions | Domain implementation details, persistence, messaging directly |
| Application | Domain, infrastructure abstractions, shared-common | Web framework, JPA entities, Kafka clients directly |
| Domain | None of the technical frameworks | Spring, JPA, Kafka, Redis, web stack, databases |
| Infrastructure | Domain contracts, external libraries, shared libraries | Business logic in the opposite direction |
| Persistence | Domain, JPA, Flyway, shared-config | Controllers or use-case orchestration |
| Security | Application, domain, shared-security | Direct persistence or business logic shortcuts |
| Messaging | Domain, shared-events, infrastructure abstractions | Controller or UI concerns |
| Validation | Domain, shared-validation | Infrastructure implementations |
| Mapper | Domain, DTO | Business rule orchestration |
| Exception | shared-exceptions | Infrastructure or persistence implementation details |

## 8. Shared Library Architecture

| Shared library | Responsibility | Ownership | Allowed usage |
|---|---|---|---|
| shared-core | Common abstractions, identifiers, value objects, base utilities | Platform team | All services |
| shared-security | JWT parsing, token validation contracts, security utility primitives | Platform team | All services |
| shared-events | Event metadata, envelope model, event versioning helpers | Platform team | All services |
| shared-validation | Validation helpers and shared constraints | Platform team | All services |
| shared-logging | Structured logging helpers and correlation metadata | Platform team | All services |
| shared-observability | Tracing, meter registration, health signal contracts | Platform team | All services |
| shared-testing | Test fixtures, builders, testcontainers base, mock helpers | Platform team | All services and shared libs |
| shared-exceptions | Domain error models and Problem Details payloads | Platform team | All services |
| shared-config | Environment and configuration abstractions | Platform team | All services |
| shared-common | Reusable general-purpose utilities | Platform team | All services |

### Ownership model

- Shared libraries are platform-owned.
- Service teams may consume them but must not modify them without platform review.
- New shared abstractions require justification through architecture review before adoption.

## 9. Configuration Architecture

### 9.1 Configuration model

Configuration will be environment-specific and profile-driven.

| Profile | Purpose |
|---|---|
| dev | Local development and docker-compose based workflow |
| test | Automated test execution |
| staging | Pre-production validation |
| prod | Production deployment |

### 9.2 Configuration strategy

- Application configuration is externalized and environment-driven.
- Secrets are injected through the platform secret management mechanism and never hardcoded.
- Shared defaults live in the shared-config library.
- Feature flags are centrally managed and exposed through configuration.

### 9.3 Configuration responsibilities

- Service-specific config: database, messaging, cache, feature toggles.
- Platform config: tracing, metrics, observability endpoint exposure.
- Security config: JWT issuer, audience, token lifetime, refresh behavior.

## 10. Spring Security Architecture

### 10.1 Security architecture goals

- Authentication and authorization are implemented entirely in Spring Security.
- Supabase is not used as an authentication layer.
- Identity Service is responsible for credential and session lifecycle.

### 10.2 Authentication flow

1. Client authenticates against Identity Service.
2. Identity Service issues short-lived JWT access tokens and refresh tokens.
3. Other services validate JWTs through shared security primitives.
4. Security context is populated for downstream authorization decisions.
5. Sensitive actions are logged through audit hooks.

### 10.3 RBAC and permission model

- Roles: customer, admin, support, operations, finance, internal-service.
- Permissions are evaluated using method-level security and resource ownership checks.
- Sensitive actions require step-up or admin-scoped access.

### 10.4 Security components

- Authentication filter
- JWT validation filter
- Refresh token handling
- OTP challenge flow support
- RBAC policy layer
- Method security enforcement
- Audit logging interceptor
- Security exception handling

### 10.5 Security exception flow

- Authentication failures return a standard problem-details response.
- Authorization failures are handled by a dedicated access-denied handler.
- Security events are emitted to audit logs and monitoring.

## 11. Persistence Architecture

### 11.1 Database ownership

Each service owns its own relational database or its own dedicated persistence store.

### 11.2 Spring Data JPA strategy

- JPA is used per service for domain-owned relational data.
- Repositories are service-owned and domain-specific.
- No service directly accesses another service’s schema.

### 11.3 Transaction boundaries

- Local transactions are used for single-aggregate changes.
- Saga boundaries are owned by the Order Service.
- Payment and inventory operations require explicit compensation semantics.

### 11.4 Concurrency controls

- Optimistic locking is the default for aggregate updates.
- Pessimistic locking is reserved for hot-path inventory reservation and other explicit contention scenarios.

### 11.5 Read and write model strategy

- Write models are authoritative transactional stores.
- Read models are generated or projected where query performance or denormalization is needed.
- Search and analytics are read-model-oriented and event-driven.

### 11.6 Flyway strategy

- Flyway manages schema evolution for every service-owned database.
- Each service owns its own migration set.
- Migration naming, rollback planning, and change review are standardized.

## 12. Kafka Architecture

### 12.1 Topic naming

Kafka topics follow the convention:

- sporekart.<domain>.<event>
- Example: sporekart.order.created

### 12.2 Producer strategy

- Producers publish domain events from the owning service.
- Event publishing occurs through an outbox pattern to guarantee atomicity.
- Event payloads include correlation ID and event metadata.

### 12.3 Consumer strategy

- Consumers are idempotent and deduplicate via processed-event records.
- Consumers must be resilient to re-delivery.
- Consumers should not mutate shared state without explicit ownership boundaries.

### 12.4 Reliability controls

- Bounded retries with exponential backoff.
- Dead-letter queue for poison messages.
- Ordering is only guaranteed where the business case requires it.
- Versioned event payloads and compatibility rules are mandatory.

## 13. Redis Architecture

### 13.1 Caching strategy

- Catalog, session, and cart data are the primary cache candidates.
- Redis is used for ephemeral state, cache-aside patterns, and fast read operations.

### 13.2 Key strategy

- Keys are namespaced by service and domain.
- TTLs are explicit and documented per use case.

### 13.3 Additional uses

- Distributed locks for short-lived coordination.
- Session cache for identity and authorization context.
- Rate limiting cache for abuse protection.
- Cache invalidation is service-owned and event-driven where appropriate.

## 14. Observability Architecture

### 14.1 Logging

- Structured logging is mandatory.
- Every log entry includes correlation ID, service name, and operation context.
- Logs are emitted in a consistent JSON-like format.

### 14.2 Metrics

- Prometheus metrics are exposed by every service.
- Business metrics and technical metrics are both collected.
- Dashboards are owned per service and platform-level dashboards are standardized.

### 14.3 Tracing

- OpenTelemetry is the tracing standard.
- Trace IDs and correlation IDs are propagated through synchronous requests and Kafka events.
- Checkout, refund, and payment flows require end-to-end trace visibility.

### 14.4 Health and readiness

- Every service exposes health and readiness endpoints via Spring Actuator.
- Readiness is used by Kubernetes deployment and service mesh routing.

## 15. Exception Handling Architecture

### 15.1 Exception strategy

- Domain errors remain in the domain layer.
- Application errors are converted into standard service-level responses.
- Infrastructure and security failures are mapped to explicit problem-details output.

### 15.2 Error response model

All API errors use RFC 9457-style problem details with:

- title
- status
- detail
- instance
- correlation ID
- error code

### 15.3 Exception categories

- Validation exceptions
- Business exceptions
- Infrastructure exceptions
- Security exceptions
- Integration exceptions

## 16. Validation Architecture

### 16.1 Validation layers

- DTO validation uses Bean Validation annotations.
- Cross-field validation is implemented through custom validators.
- Business rules that cannot be expressed in annotations are handled in the application or domain layer.

### 16.2 Validation standards

- Validation errors return a consistent error representation.
- Invalid input is rejected before reaching domain logic.
- Custom validators are centralized in the shared-validation library where possible.

## 17. Testing Architecture

### 17.1 Test pyramid

- Unit tests for domain logic and pure service behavior.
- Integration tests for repository and persistence behavior.
- Contract tests for cross-service interfaces and event compatibility.
- Security tests for authentication and authorization.
- End-to-end tests for critical workflows such as checkout and refund.

### 17.2 Tooling strategy

- JUnit 5 for test execution.
- Mockito for mocking collaborators.
- Testcontainers for integration testing against real dependencies.
- Shared test utilities in the shared-testing library.

## 18. Engineering Standards

### 18.1 Architectural standards

- Follow SOLID principles.
- Keep the domain model clean and framework-agnostic.
- Enforce dependency inversion through ports and adapters.
- Prefer explicit contracts and immutable value objects.

### 18.2 Code standards

- Consistent naming and package conventions.
- Small classes and focused methods.
- Avoid hidden state and cross-layer leakage.
- Keep infrastructure concerns behind interfaces.

### 18.3 Team standards

- Every service must have a documented ownership model.
- Every service must have a clear review checklist.
- All changes must be reviewed for correctness, security, and operational impact.
- Branches and merge standards are governed by the repository workflow.

## 19. Performance Standards

- Thread pool sizing is explicit and environment-aware.
- Connection pools are configured per service and workload.
- Pagination is required for large result sets.
- Streaming is used for large payload or event ingestion cases.
- Compression is used where payload size is meaningful.
- HTTP keep-alive and connection reuse are enabled by default.
- Database indexes are designed around query and write access patterns.

## 20. Maintainability Standards

- Package boundaries remain stable and explicit.
- Class size and method complexity are reviewed during PRs.
- Documentation accompanies significant domain behavior changes.
- Technical debt must be tracked and retired deliberately.
- Deprecation is handled through versioned contracts and migration windows.

## 21. Architecture Validation Report

| Validation area | Result | Notes |
|---|---|---|
| Java 21 compatibility | Pass | Architecture targets Java 21 LTS |
| Spring Boot 3.x compatibility | Pass | Architecture is aligned to Spring Boot 3.x |
| Spring Security architecture completeness | Pass | Security architecture is defined end to end |
| Domain-driven design compliance | Pass | Service boundaries and layers are explicit |
| Clean architecture and SOLID alignment | Pass | Dependency direction is defined and enforced |
| Cloud readiness | Pass | Docker, Kubernetes, observability, and service boundaries are defined |
| Container readiness | Pass | Platform and service packaging standards are included |
| AI agent readiness | Pass | Module boundaries, contracts, and standards are explicit |
| Scalability readiness | Pass | Scaling and infrastructure patterns are defined |
| Maintainability readiness | Pass | Standards and dependency boundaries are documented |

## 22. Spring Boot Readiness Score

Overall Spring Boot readiness: 84/100

### Readiness rationale

- Strong backend foundation and module structure: 88/100
- Security and observability architecture: 84/100
- Dependency and package discipline: 86/100
- Testing and maintainability standards: 80/100
- Remaining risk: contract completeness and final production policy approval

## 23. Phase 1 Part 3 Completion Checklist

- [x] Enterprise repository architecture defined.
- [x] Maven multi-module strategy defined.
- [x] Spring Boot layer architecture defined.
- [x] Package structure specification defined.
- [x] Dependency matrix defined.
- [x] Shared library architecture defined.
- [x] Configuration architecture defined.
- [x] Spring Security architecture defined.
- [x] Persistence architecture defined.
- [x] Kafka architecture defined.
- [x] Redis architecture defined.
- [x] Observability architecture defined.
- [x] Exception handling architecture defined.
- [x] Validation architecture defined.
- [x] Testing architecture defined.
- [x] Engineering standards defined.
- [x] Performance standards defined.
- [x] Maintainability standards defined.
- [x] Architecture validation report completed.

## 24. Phase 1 Part 4 Prerequisites

The following prerequisites must be completed before Phase 1 Part 4 can proceed:

1. Product requirements and acceptance criteria must be approved.
2. OpenAPI 3.1 and AsyncAPI contracts must be finalized.
3. Security policy and RBAC matrix must be approved.
4. Event versioning and compatibility rules must be approved.
5. Observability SLOs and alert ownership must be approved.
6. Service-owned database migration policy must be approved.
7. Shared library ownership and release rules must be approved.
