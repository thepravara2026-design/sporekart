# Governance Admin Testing

## Test Files (23)

### Domain Tests (2 files)
| File | Description |
|------|-------------|
| `governance/admin/domain/AdminEnumTest.java` | Validates all 5 enums (AdminOperationType, ConfigurationStatus, EnvironmentType, GovernanceModuleType, MaintenanceStatus) |
| `governance/admin/domain/AdminRecordTest.java` | Validates all 13 records (AdminConfiguration, SystemConfiguration, EnvironmentProfile, FeatureFlag, GovernanceModule, ConfigurationVersion, ConfigurationSnapshot, AdminOperation, AdminSession, ConfigurationAudit, MaintenanceWindow, OperationalSetting, ConfigurationMetadata) |

### Application Service Tests (8 files)
| File | Description |
|------|-------------|
| `governance/admin/application/ConfigurationManagerTest.java` | CRUD operations, caching, validation |
| `governance/admin/application/FeatureFlagServiceTest.java` | Global/env/module flag resolution, toggling, caching |
| `governance/admin/application/EnvironmentManagerTest.java` | Environment profile CRUD, activation |
| `governance/admin/application/ConfigurationVersionManagerTest.java` | Version creation, rollback, diff |
| `governance/admin/application/ConfigurationSnapshotServiceTest.java` | Snapshot create/restore, integrity |
| `governance/admin/application/ConfigurationValidationServiceTest.java` | Validation rules, type checking, dry-run |
| `governance/admin/application/MaintenanceModeServiceTest.java` | Enable/disable, status, scheduling |
| `governance/admin/application/AdministrationAuditServiceTest.java` | Audit recording, query, immutability |

### Infrastructure Tests (6 files)
| File | Description |
|------|-------------|
| `governance/admin/infrastructure/persistence/AdminConfigurationRepositoryTest.java` | CRUD, unique constraints, pagination |
| `governance/admin/infrastructure/persistence/FeatureFlagRepositoryTest.java` | CRUD, scope queries |
| `governance/admin/infrastructure/persistence/EnvironmentProfileRepositoryTest.java` | CRUD, active queries |
| `governance/admin/infrastructure/redis/AdminRedisCacheServiceTest.java` | 5 namespace cache operations, TTL, eviction |
| `governance/admin/infrastructure/kafka/AdminKafkaEventPublisherTest.java` | 6 event types publishing |
| `governance/admin/infrastructure/monitoring/AdminMonitoringServiceTest.java` | Metrics recording, health check |

### Config Tests (1 file)
| File | Description |
|------|-------------|
| `governance/admin/config/AdminConfigTest.java` | Configuration properties binding |

### Interface Tests (4 files)
| File | Description |
|------|-------------|
| `governance/admin/interfaces/rest/AdminControllerTest.java` | 11 endpoint integration tests |
| `governance/admin/interfaces/rest/AdminControllerSecurityTest.java` | Role-based access tests |
| `governance/admin/interfaces/rest/AdminControllerValidationTest.java` | Request validation tests |
| `governance/admin/interfaces/rest/AdminControllerAuditTest.java` | Audit recording verification on all endpoints |

### Architecture Tests (1 file)
| File | Description |
|------|-------------|
| `governance/admin/architecture/AdminArchitectureTest.java` | Package dependency rules, layer isolation, naming conventions |

### Integration Tests (1 file)
| File | Description |
|------|-------------|
| `governance/admin/integration/AdminIntegrationTest.java` | End-to-end configuration lifecycle, feature flag toggle flow, import/export flow, maintenance mode lifecycle |

## Coverage Strategy

### Target Coverage
| Layer | Target | Key Scenarios |
|-------|--------|---------------|
| Domain | 95% | Enum values, record construction, validation |
| Application | 90% | CRUD, validation, caching, rollback, import/export |
| Infrastructure | 85% | Persistence, cache, events, metrics |
| Interface | 85% | Endpoint behavior, security, validation, audit |
| Config | 90% | Property binding, defaults |

### Test Categories

**Configuration Management Tests:**
- Create/update/delete configurations
- Module+environment scoping
- Value type validation (string, number, boolean, JSON)
- Version creation on update
- Rollback to previous version
- Snapshot create and restore
- Import with dry-run validation
- Export filtered by module/environment

**Feature Flag Tests:**
- Global flag resolution
- Environment override resolution
- Module override resolution
- Toggle and persistence
- Cache invalidation on toggle
- Audit recording on toggle

**Module Management Tests:**
- Enable/disable module
- Module health status
- Module isolation (disabled module returns error)

**Maintenance Mode Tests:**
- Enable with authorized user
- Disable with authorized user
- Unauthorized user cannot toggle
- Status reflects in health check
- Scheduled maintenance windows

**Audit Tests:**
- Every operation records audit entry
- Audit records are immutable (cannot be updated/deleted)
- Query by operation type, user, date range, target
- Export audit records

### Performance Tests
- Configuration CRUD: <100ms per operation (p99)
- Feature flag resolution: <10ms (cached)
- Import with 1000 entries: <5s
- Snapshot restore with 1000 entries: <10s

### Security Tests
- Unauthenticated access returns 401
- Unauthorized role returns 403
- Config access respects module boundaries
- Audit immutability enforced at DB trigger level
- Role hierarchy resolution (inherited permissions)
