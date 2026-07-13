# Governance Testing Strategy

## Overview

The governance module test suite consists of 15 test files covering unit, integration, architecture, and performance tests. The strategy follows the same pattern established by all other AI platform modules.

## Test Categories

### 1. Unit Tests (Domain Layer)

**File:** `GovernanceDomainTest.java` — 12 tests

Tests domain records and enums for structural integrity:
- Record creation and field access
- Enum value completeness
- Record equality and hashcode
- Immutability verification
- Factory methods and builders
- Validation annotations on domain objects

### 2. Repository Tests (Persistence Layer)

**File:** `GovernancePolicyRepositoryTest.java` — 8 tests

Tests `GovernancePolicyRepository` Spring Data JPA interface:
- Save and find by ID
- Find by type, status, severity, scope
- Find by name (unique within scope)
- Pagination and sorting
- Optimistic locking version increments
- Custom query methods

**File:** `ConfigEntryRepositoryTest.java` — 7 tests

Tests `ConfigEntryRepository`:
- CRUD operations
- Find by scope + module + key combination
- Find by tag
- Key uniqueness constraint
- Encrypted field persistence

**File:** `AuditRecordRepositoryTest.java` — 6 tests

Tests `AuditRecordRepository`:
- Append-only insert
- Query by event type, actor, time range
- Paginated retrieval
- Export-friendly queries
- Verify no update/delete methods available

**File:** `PermissionAssignmentRepositoryTest.java` — 5 tests

Tests `PermissionAssignmentRepository`:
- Role-based lookup
- Permission matching
- Unique constraint on role + permission + resource + scope

**File:** `UsageQuotaRepositoryTest.java` — 5 tests

Tests `UsageQuotaRepository`:
- Find by module + user + operation + period
- Upsert increment
- Period-based queries
- Count and limit comparison

**File:** `ComplianceReportRepositoryTest.java` — 4 tests

Tests `ComplianceReportRepository`:
- Create with FK to policy
- Find by policy and status
- Cascade delete on policy removal

### 3. Application Service Tests

**File:** `PolicyManagerImplTest.java` — 10 tests

Tests `PolicyManagerImpl` application service:
- Create policy with valid data
- Create policy with duplicate name (throws exception)
- Update policy with optimistic locking
- Activate/deactivate policy lifecycle
- Archive policy
- Status transition validation (no backwards transitions)
- Policy rule schema validation
- Auditing on policy operations
- Cache invalidation on update
- Kafka event publishing on lifecycle changes

**File:** `ConfigurationServiceImplTest.java` — 8 tests

Tests `ConfigurationServiceImpl`:
- Create config entry
- Update existing entry
- Scope-based access enforcement
- Encrypted value masking
- Data type validation
- Change log recording
- Cache invalidation
- Concurrent modification handling

**File:** `AuditServiceImplTest.java` — 7 tests

Tests `AuditServiceImpl`:
- Record audit event with all fields
- Record event with minimal fields
- Query with time range filters
- Query with event type filter
- Pagination
- Export to CSV format
- Export to JSON format

**File:** `AccessControlServiceImplTest.java` — 8 tests

Tests `AccessControlServiceImpl`:
- Role permission resolution
- Hierarchical inheritance
- DENY override precedence
- Permission cache hit/miss
- Permission check for authorized user
- Permission check for unauthorized user
- Role assignment
- Role revocation

**File:** `QuotaManagerImplTest.java` — 6 tests

Tests `QuotaManagerImpl`:
- Increment usage counter
- Check quota within limit
- Check quota exceeded (returns false)
- Quota reset
- Period boundary alignment
- Concurrent increment handling

**File:** `ComplianceCheckerImplTest.java` — 5 tests

Tests `ComplianceCheckerImpl`:
- Run compliance check (compliant result)
- Run compliance check (non-compliant result)
- Report violation details
- Check specific policy
- Check all active policies for a module

### 4. Controller Tests

**File:** `GovernanceControllerTest.java` — 12 tests

Tests `GovernanceController` endpoints with `@WebMvcTest`:
- GET /policies — returns policy list
- POST /policies — creates policy
- PUT /policies/{id} — updates policy
- DELETE /policies/{id} — archives policy
- GET /audit — returns audit records
- POST /audit/export — exports audit records (file download)
- GET /compliance — returns compliance report
- GET /config — returns config entries
- POST /config — creates config entry
- POST /config/import — imports config bundle
- GET /quotas — returns quota list
- POST /roles — creates role
- POST /roles/{id}/assign — assigns role
- Error responses follow RFC 9457
- Authentication/authorization enforcement

### 5. Kafka Integration Tests

**File:** `GovernanceKafkaEventPublisherTest.java` — 8 tests

Tests `GovernanceKafkaEventPublisher` using embedded Kafka:
- Publish PolicyCreated event
- Publish PolicyActivated event
- Publish ConfigUpdated event
- Publish AuditRecordCreated event
- Publish ComplianceViolationDetected event
- Event carries correlationId
- Event serialization/deserialization
- Multiple events published sequentially

### 6. Redis Cache Tests

**File:** `GovernanceRedisCacheServiceTest.java` — 6 tests

Tests `GovernanceRedisCacheService` using embedded Redis:
- Cache policy entry
- Cache config entry
- Cache invalidation by key
- Cache invalidation by namespace prefix
- Cache miss loads from database
- TTL expiry behavior

### 7. Architecture Tests

**File:** `GovernanceArchitectureTest.java` — 5 tests

ArchUnit and Spring Modulith tests:
- Governance module does not depend on any other AI module
- Domain layer has no external dependencies
- Application layer depends only on domain and API layers
- Infrastructure layer depends only on application and API layers
- Interfaces layer depends only on application layer

### 8. Integration Tests

**File:** `GovernanceIntegrationTest.java` — 8 tests

Full integration tests with `@SpringBootTest`:
- Full policy CRUD lifecycle
- Audit trail end-to-end
- RBAC enforcement across endpoints
- Config import/export round-trip
- Quota tracking and reset
- Compliance check workflow
- Error handling across all endpoints
- Transactional rollback behavior

## Performance Targets

| Test Type | Target | Threshold |
|-----------|--------|-----------|
| Policy evaluation latency | < 5ms | 10ms (p95) |
| Audit record append | < 10ms | 25ms (p95) |
| Config lookup (cache hit) | < 2ms | 5ms (p95) |
| Config lookup (cache miss) | < 15ms | 30ms (p95) |
| Permission check | < 3ms | 8ms (p95) |
| Quota increment | < 5ms | 15ms (p95) |
| Compliance check (10 policies) | < 50ms | 100ms (p95) |
| Audit query (30 days) | < 500ms | 1s (p95) |
| Config import (100 entries) | < 1s | 2s (p95) |

## Test Summary

| # | Test File | Type | Test Count |
|---|-----------|------|------------|
| 1 | `GovernanceDomainTest.java` | Unit | 12 |
| 2 | `GovernancePolicyRepositoryTest.java` | Repository | 8 |
| 3 | `ConfigEntryRepositoryTest.java` | Repository | 7 |
| 4 | `AuditRecordRepositoryTest.java` | Repository | 6 |
| 5 | `PermissionAssignmentRepositoryTest.java` | Repository | 5 |
| 6 | `UsageQuotaRepositoryTest.java` | Repository | 5 |
| 7 | `ComplianceReportRepositoryTest.java` | Repository | 4 |
| 8 | `PolicyManagerImplTest.java` | Service | 10 |
| 9 | `ConfigurationServiceImplTest.java` | Service | 8 |
| 10 | `AuditServiceImplTest.java` | Service | 7 |
| 11 | `AccessControlServiceImplTest.java` | Service | 8 |
| 12 | `QuotaManagerImplTest.java` | Service | 6 |
| 13 | `ComplianceCheckerImplTest.java` | Service | 5 |
| 14 | `GovernanceControllerTest.java` | Controller | 13 |
| 15 | `GovernanceKafkaEventPublisherTest.java` | Kafka | 8 |
| 16 | `GovernanceRedisCacheServiceTest.java` | Redis | 6 |
| 17 | `GovernanceArchitectureTest.java` | Architecture | 5 |
| 18 | `GovernanceIntegrationTest.java` | Integration | 8 |
| **Total** | | | **131** |

## Test Execution

```bash
# Run all governance tests
mvn test -pl ai-service -Dtest="*Governance*"

# Run specific test class
mvn test -pl ai-service -Dtest="GovernancePolicyRepositoryTest"

# Run with coverage
mvn verify -pl ai-service -Pcoverage

# Run architecture tests only
mvn test -pl ai-service -Dtest="GovernanceArchitectureTest"
```

## Coverage Targets

| Layer | Target |
|-------|--------|
| Domain (records, enums) | 100% |
| Application services | 90%+ |
| Repository (custom queries) | 95%+ |
| Controller | 90%+ |
| Infrastructure (Redis, Kafka) | 85%+ |
| Overall | 85%+ |
