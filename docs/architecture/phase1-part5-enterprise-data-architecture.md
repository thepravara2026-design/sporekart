# SporeKart Phase 1 Part 5 — Enterprise Data Architecture and Persistence Design

- Version: 1.0
- Status: Review complete
- Owner: Enterprise Architecture Review Board
- Review Date: 2026-07-10
- Purpose: Freeze the service-owned persistence and data architecture baseline.
- Scope: Database ownership, transaction boundaries, Flyway strategy, caching, retention, and recovery expectations.
- References: [phase1-part2-service-catalog-domain-design.md](phase1-part2-service-catalog-domain-design.md), [phase1-part6-enterprise-openapi-contract-architecture.md](phase1-part6-enterprise-openapi-contract-architecture.md)
- Approval Status: Reviewed; migration and retention artifacts remain pending

## 1. Purpose and Scope

This document defines the enterprise data architecture and persistence blueprint for the SporeKart platform. It is architecture-only and does not generate implementation code, SQL migrations, Flyway scripts, JPA entities, repositories, or database tables.

This document is the mandatory persistence baseline for all later implementation phases.

## 2. Data Architecture Principles

The persistence architecture is governed by the following principles:

- Every microservice owns its own database schema and persistence model.
- No service directly accesses another service’s database.
- Supabase PostgreSQL is used only for managed PostgreSQL, storage, backups, and point-in-time recovery.
- Spring Data JPA, Spring Transaction Management, and Flyway are the mandatory persistence technologies.
- Read models and write models are explicitly separated where required.
- Data ownership, retention, and audit obligations are clear and service-scoped.
- The architecture remains portable across managed PostgreSQL providers and infrastructure environments.

## 3. Enterprise Data Architecture

### 3.1 Logical data architecture

The platform uses a polyglot but service-owned persistence model:

- Transactional operational data remains in each service-owned PostgreSQL schema.
- Read-optimized projections are maintained for search, analytics, reporting, and customer-facing views.
- Shared reference and metadata data is owned by the service that logically governs it.
- Event-driven integration enables data propagation without shared-database access.

### 3.2 Physical data architecture

The physical data architecture is composed of:

- Service-owned PostgreSQL schemas in managed PostgreSQL.
- Redis for ephemeral cache and coordination state.
- OpenSearch for search-oriented read models and indexing.
- Supabase Storage for non-transactional blobs and media assets.
- Kafka for change propagation and event-driven synchronization.

### 3.3 Data lifecycle

Data follows a lifecycle of:

1. Creation
2. Validation
3. Transactional persistence
4. Projection or publication
5. Optional archival
6. Soft delete or retention expiration
7. Hard delete or purge

### 3.4 Data governance

- Each service owns its schema, retention policy, and migration history.
- Data contracts and ownership boundaries are documented and reviewed.
- Schema changes must be versioned, reviewable, and observable.
- Sensitive data is classified and protected according to policy.

## 4. Database Ownership Matrix

| Service | Database ownership | Schema ownership | Table ownership | View ownership | Read model ownership | Write model ownership | Migration ownership | Backup ownership |
|---|---|---|---|---|---|---|---|---|
| Identity Service | Yes | Yes | Yes | Limited | Yes | Yes | Yes | Yes |
| Catalog Service | Yes | Yes | Yes | Yes | Yes | Yes | Yes | Yes |
| Inventory Service | Yes | Yes | Yes | Yes | Yes | Yes | Yes | Yes |
| Cart Service | Yes | Yes | Yes | Limited | Yes | Yes | Yes | Yes |
| Order Service | Yes | Yes | Yes | Yes | Yes | Yes | Yes | Yes |
| Payment Service | Yes | Yes | Yes | Yes | Yes | Yes | Yes | Yes |
| Fulfillment Service | Yes | Yes | Yes | Yes | Yes | Yes | Yes | Yes |
| Training Service | Yes | Yes | Yes | Yes | Yes | Yes | Yes | Yes |
| Content Service | Yes | Yes | Yes | Yes | Yes | Yes | Yes | Yes |
| Notification Service | Yes | Yes | Yes | Limited | Yes | Yes | Yes | Yes |
| Search Service | Yes | Yes | Yes | Yes | Yes | Yes | Yes | Yes |
| Analytics Service | Yes | Yes | Yes | Yes | Yes | Yes | Yes | Yes |
| Risk Service | Yes | Yes | Yes | Yes | Yes | Yes | Yes | Yes |
| Support Service | Yes | Yes | Yes | Yes | Yes | Yes | Yes | Yes |

### Ownership constraints

- No service may directly read from another service database.
- Cross-service joins are prohibited.
- Integration uses events, APIs, and contracts rather than shared tables.
- Shared reference data must be copied or exposed via owned integration contracts.

## 5. Supabase PostgreSQL Architecture

### 5.1 Platform role of Supabase

Supabase PostgreSQL is used exclusively as the managed relational data platform for SporeKart.

Approved usage:

- Managed PostgreSQL
- Storage for files and media objects
- Automated backups
- Point-in-time recovery

Not approved:

- Supabase Auth
- Supabase Edge Functions
- Supabase Realtime
- Supabase Row Level Security for application authorization

### 5.2 Connection strategy

- Services connect through managed database connection pools.
- Connection pooling is configured per workload and service.
- Database credentials are injected through runtime secret management.
- Each service uses its own logical connection context and schema ownership.

### 5.3 High availability and resilience

- Managed PostgreSQL must be provisioned with high availability where supported.
- Failover behavior is operationally tested through recovery drills.
- Readiness and health endpoints are used to detect database connectivity issues.

### 5.4 Backup and PITR

- Automated backups are enabled for all production environments.
- PITR is enabled for point-in-time restore and incident response.
- Restore procedures are documented and tested periodically.

### 5.5 Portability strategy

- Database design remains portable across managed PostgreSQL providers.
- Service logic does not depend on Supabase-specific business logic features.
- Data and schema conventions remain provider-agnostic.

## 6. Persistence Strategy

### 6.1 Spring Data JPA usage

Spring Data JPA is the standard persistence abstraction for service-owned relational data.

Use cases:

- Aggregates and transactional persistence
- Repository-based domain access
- Transactional write flows
- Query and sorting operations for service-owned data

### 6.2 Repository pattern

- Repositories are scoped to aggregates and service-owned data.
- Repository interfaces are narrow and domain-oriented.
- Persistence concerns remain behind repository abstractions.

### 6.3 Aggregate persistence

- Each aggregate root is persisted as a transactionally consistent unit.
- Aggregate boundaries align to domain ownership.
- Large aggregate graphs are avoided to preserve performance and consistency.

### 6.4 Transaction scope

- Local transactions are used for single-service write flows.
- Distributed coordination uses saga patterns rather than distributed database transactions.
- Transactional boundaries are explicit and documented per workflow.

### 6.5 Loading guidelines

- Lazy loading is used for non-critical associations and large object graphs.
- Eager loading is reserved for small, commonly required association sets.
- N+1 query patterns are avoided through query design and projection strategies.

### 6.6 Pagination and batch processing

- Pagination is required for list and search endpoints.
- Batch processing is used for large backfills and reconciliation operations.
- Streaming is used for high-volume event or file processing when appropriate.

### 6.7 Read optimization

- Read models are optimized for access patterns and UI requirements.
- Heavy read workloads use denormalized projections or asynchronous materialization where needed.

### 6.8 Write optimization

- Writes should be minimized where possible.
- Batching and transactional grouping are used for high-volume operations.
- Idempotent write patterns are required for event-driven flows.

## 7. Flyway Strategy

### 7.1 Migration ownership

Each service owns its own Flyway migration history and schema change set.

### 7.2 Migration naming convention

Migration names follow a clear convention:

- V<version>__<description>.sql

Example structure:

- V001__create_identity_tables
- V002__add_order_status_index

### 7.3 Migration versioning

- Version numbers are monotonic and service-scoped.
- Each migration is applied once per environment.
- Versioning remains independent from shared infrastructure changes.

### 7.4 Rollback policy

- Rollbacks are not automatic.
- Rollback planning is required for destructive migrations.
- Reversible changes are preferred where possible.

### 7.5 Repeatable migrations

- Repeatable migrations are used for view definitions, metadata, and reference data where appropriate.
- Repeatable migrations are re-applied consistently on each change.

### 7.6 Baseline strategy

- Existing databases are baselined before introducing service-specific migration pipelines.
- New services start with a clean baseline and explicit migration history.

### 7.7 Environment promotion

- Development, test, staging, and production each use their own schema evolution path.
- Promotion uses a controlled sequence of approved migrations.
- Migration validation occurs before promotion.

## 8. Transaction Architecture

### 8.1 Local transactions

- Local transactions are used for single-service writes that do not span multiple services.
- Transaction boundaries align with aggregate state changes and business workflows.

### 8.2 Distributed transactions

- Distributed transactions are avoided.
- Cross-service transactional consistency is achieved through saga orchestration and compensation.

### 8.3 Saga coordination

- Order and payment workflows orchestrate state transitions through explicit saga steps.
- Each step applies local transaction semantics and emits domain events.

### 8.4 Saga compensation

- Compensation actions are defined for each step that can fail.
- The process remains observable and auditable.
- Failed workflows use explicit state and retry logic.

### 8.5 Optimistic locking

- Optimistic locking is the default for aggregate updates.
- Version fields are used to prevent lost updates where concurrency is expected.

### 8.6 Pessimistic locking

- Pessimistic locking is reserved for hotspot contention cases such as inventory allocation.
- Lock scope remains limited and carefully documented.

### 8.7 Isolation levels

- Default isolation is the lowest safe isolation level for the use case.
- Stronger isolation is used only where required for correctness.

### 8.8 Retry and idempotency

- Retries are bounded and safe for idempotent operations.
- Producers and consumers must support event deduplication and replay tolerance.

## 9. Database Performance Guidelines

### 9.1 Primary keys

- Surrogate identifiers are used consistently.
- UUIDs are used where distribution and external reference requirements justify them.
- Numeric identifiers remain acceptable for high-volume internal entities where appropriate.

### 9.2 UUID strategy

- UUIDs are used for cross-system identity propagation and distributed systems compatibility.
- UUID generation strategy remains stable and consistent across services.

### 9.3 Index strategy

- Indexes are created for frequent filters, sort keys, joins within service-owned data, and lookup paths.
- Excessive indexing is avoided to preserve write performance.
- Indexes are reviewed as part of schema change governance.

### 9.4 Composite indexes

- Composite indexes are used where multi-column query patterns are common.
- They are designed around business access paths rather than arbitrary combinations.

### 9.5 Unique constraints

- Unique constraints protect canonical business identities and invariants.
- Unique constraints are used instead of application-side duplicates where enforcement is required.

### 9.6 Partitioning strategy

- Partitioning is reserved for large historical or high-volume tables.
- Partition criteria must be based on clear and stable business patterns.
- Partitioning is introduced only when measured performance or retention requirements justify it.

### 9.7 Query optimization

- Query plans are reviewed for high-volume and critical flows.
- Service-owned data access patterns are kept simple and predictable.
- Query complexity is reduced via projection and read model design.

### 9.8 Connection pooling and vacuum strategy

- Connection pool sizing is service-specific and environment-aware.
- Autovacuum and maintenance settings are tuned according to workload.
- Statistics updates are automated and continuously monitored.

## 10. Caching Architecture

### 10.1 Redis cache strategy

Redis is used for temporary, high-speed, or shared access data.

Primary cache use cases:

- Session and auth context caching
- Catalog reference data caching
- Cart and temporary shopping state
- Rate limiting and short-lived coordination
- Hot read data for frequently requested objects

### 10.2 Cache ownership

- Each service owns its cache keys and invalidation policy.
- Shared caches are avoided unless the owning service is clearly defined.

### 10.3 Cache keys and TTL

- Cache keys are namespaced by service, domain, and entity identity.
- TTL values are explicit and workload-based.
- Keys are versioned when a data shape changes.

### 10.4 Invalidation rules

- Invalidation occurs on write, event receipt, or explicit expiration.
- Cache invalidation is deterministic and documented.

### 10.5 Distributed locking

- Redis-based distributed locks are permitted for short-lived coordination scenarios.
- Locks are time-bounded and safe-fail mechanisms are used.

## 11. Search Architecture

### 11.1 OpenSearch integration

OpenSearch is used for search and discovery-oriented data access.

### 11.2 Search index ownership

- Search indexes are owned by the service that governs the data domain.
- Search indexing is event-driven and asynchronous where possible.

### 11.3 Synchronization strategy

- Search documents are updated through domain events or explicit integration steps.
- Index updates are idempotent and versioned.

### 11.4 Rebuild and versioning strategy

- Rebuilds are supported through replayable event streams or full reindex workflows.
- Index versions are explicitly managed to avoid stale or incompatible search documents.

### 11.5 Consistency model

- Search is treated as eventually consistent.
- Search results must clearly indicate the expected freshness level where necessary.

## 12. Data Integrity and Lifecycle Standards

### 12.1 Referential integrity

- Within a service, foreign keys are used where they add correctness and clarity.
- Cross-service references are represented as identifiers and contracts, not direct foreign keys.

### 12.2 Soft deletes and hard deletes

- Soft delete is preferred for business records that may require restoration.
- Hard delete is reserved for data that is no longer legally or operationally needed.

### 12.3 Archival and retention

- Archival is defined per domain and regulatory requirement.
- Retention schedules are documented and reviewable.
- Legal hold scenarios are supported through explicit policy hooks.

### 12.4 Duplicate prevention and validation

- Unique constraints and domain rules prevent duplicate records.
- Validation is enforced at the service boundary and persisted data layer.

## 13. Backup, Recovery, and Disaster Recovery

### 13.1 Backup schedule

- Daily automated backups for operational data.
- Additional backup cadence for high-change domains such as orders and payments.

### 13.2 PITR

- Point-in-time recovery is enabled for all production databases.
- Recovery windows are defined per environment and SLA.

### 13.3 Restore procedure

- Restore procedures are tested and documented.
- Restore scope and timing are predefined by the operational team.

### 13.4 RTO and RPO

- RTO target: under 30 minutes for critical services.
- RPO target: under 15 minutes for transactional services.

### 13.5 Geo-redundancy and disaster recovery

- Multi-region or region-redundant recovery is considered for critical data domains.
- Disaster recovery exercises are planned and logged.

## 14. Data Security Architecture

### 14.1 Encryption

- Encryption at rest is enabled for PostgreSQL storage and backup systems.
- Encryption in transit is required for all database traffic.

### 14.2 PII and sensitive fields

- PII is classified and protected through least-privilege access and masking.
- Sensitive columns are not logged in plaintext.
- Contact, payment, identity, and address data receive enhanced controls.

### 14.3 Secrets handling

- Database credentials and connection details are injected at runtime.
- Secrets are rotated periodically and stored outside source control.

### 14.4 Least privilege and audit trails

- Database users are scoped to the minimum required privilege set.
- Audit trails capture data changes and administrative actions.

## 15. Observability for Data Platforms

### 15.1 Database metrics

- Connection counts
- Query latency
- Transaction throughput
- Error rate
- Deadlocks
- Lock waits
- Replication lag where applicable

### 15.2 Slow query and migration monitoring

- Slow queries are monitored and reviewed.
- Migration execution and failure events are observable.
- Schema drift and deployment anomalies trigger alerts.

### 15.3 Capacity and index health

- Storage growth, index bloat, and vacuum health are monitored.
- Capacity planning is performed via trend analysis.

## 16. Spring Boot Persistence Module Architecture

### 16.1 Persistence module responsibilities

The persistence module for each service will contain:

- Persistence configuration
- Repository abstractions
- Mapping or adapter layer definitions
- Transaction configuration
- Flyway integration contracts
- Audit support structures
- Specification or query support types

### 16.2 Package structure expectations

- persistence
- repository
- mapper
- specification
- audit
- config
- migration

### 16.3 Dependency direction

The persistence module must depend on domain contracts and not leak persistence concerns into the domain layer.

## 17. Data Governance Standards

### 17.1 Naming standards

- Schemas: service-scoped, lowercase, descriptive.
- Tables: singular or plural based on the domain standard and kept consistent per service.
- Columns: snake_case, descriptive, and explicit.
- Primary keys: explicit and stable.
- Foreign keys: clear and domain-meaningful.
- Audit columns: created_at, updated_at, created_by, updated_by, deleted_at where applicable.

### 17.2 Timestamp and UUID standards

- Timestamps use UTC.
- UUIDs are standardized for distributed identity use cases.
- Version columns are used for optimistic concurrency where applicable.

## 18. Scalability Assessment

### 18.1 Horizontal scaling

- The platform is designed for incremental horizontal scaling across services.
- Service-owned data stores allow independent scaling per workload.

### 18.2 Read replicas and partitioning

- Read replicas are considered for high-read domains.
- Partitioning is introduced for large historical or high-volume data sets when needed.

### 18.3 Growth readiness

The persistence architecture supports growth in:

- catalog volume
- order volume
- analytics workload
- training content volume
- international expansion and data residency needs

## 19. Data Platform Readiness Report

| Validation area | Result | Notes |
|---|---|---|
| Service-owned persistence | Pass | Each service owns its own schema and data boundaries |
| PostgreSQL best practices | Pass | PostgreSQL design follows enterprise conventions |
| Supabase managed PostgreSQL alignment | Pass | Supabase is used only for managed PostgreSQL and storage |
| DDD and microservice isolation | Pass | Data ownership and isolation are explicit |
| Transaction architecture | Pass | Local and saga-based boundaries are defined |
| Caching strategy | Pass | Redis strategy is documented |
| Search architecture | Pass | Search ownership and indexing strategy are defined |
| Backup and disaster recovery | Pass | Recovery and operational readiness are documented |
| Security and compliance | Pass | Data protection and audit controls are defined |
| Portability | Pass | Design remains provider-agnostic |

## 20. Architecture Risk Register

| Risk | Impact | Likelihood | Mitigation |
|---|---|---|---|
| Shared schema drift | High | Medium | Enforce service ownership and migration governance |
| Cross-service coupling through data access | High | Medium | Require events and contracts instead of shared joins |
| Hotspot contention | Medium | Medium | Use optimistic locking, targeted locking, and query optimization |
| Search inconsistency | Medium | Medium | Use event-driven indexing and versioned reindexing |
| Backup and restore delays | High | Low | Enable PITR and test restore procedures |
| Data sprawl and retention issues | Medium | Medium | Define retention and archival policies per domain |
| Over-indexing | Medium | Medium | Review indexes as part of schema governance |

## 21. Data Readiness Score

Overall data readiness: 88/100

### Readiness rationale

- Service ownership and isolation: 92/100
- Persistence and transaction design: 87/100
- Flyway and migration governance: 85/100
- Caching and search integration: 84/100
- Backup, recovery, and observability: 88/100

## 22. Phase 1 Part 5 Completion Checklist

- [x] Enterprise data architecture defined.
- [x] Database ownership matrix defined.
- [x] Persistence strategy defined.
- [x] Supabase PostgreSQL architecture defined.
- [x] Flyway strategy defined.
- [x] Transaction architecture defined.
- [x] Database performance guidelines defined.
- [x] Redis caching architecture defined.
- [x] Search architecture defined.
- [x] Data integrity standards defined.
- [x] Backup and recovery plan defined.
- [x] Data security architecture defined.
- [x] Data lifecycle strategy defined.
- [x] Observability standards defined.
- [x] Spring Boot persistence architecture defined.
- [x] Data governance standards defined.
- [x] Scalability assessment completed.
- [x] Data platform readiness report completed.
- [x] Architecture risk register completed.
- [x] Data readiness score assigned.

## 23. Phase 1 Part 6 Prerequisites

The following prerequisites must be completed before Phase 1 Part 6 can proceed:

1. Final service-owned data model review by each domain team.
2. Flyway migration governance approval.
3. Backup, PITR, and restore procedure approval.
4. Search synchronization and index ownership approval.
5. Redis cache naming and invalidation policy approval.
6. Data retention and archival policy approval.
7. Security and compliance review for sensitive data controls.
