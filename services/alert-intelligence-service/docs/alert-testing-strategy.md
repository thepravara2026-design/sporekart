# Alert Testing Strategy

## Test Coverage

### Unit Tests (domain models)
| Class | Tests | Coverage |
|---|---|---|
| Alert | 8 | Immutability, factory, getters, equals, toString |
| BusinessRisk | 8 | Immutability, factory, getters, score calc |
| Anomaly | 8 | Immutability, factory, getters, confidence |
| TimelineEvent | 8 | Immutability, factory, getters, type |
| AlertCache | 5 | Entry creation, expiry, get/put |

### Engine Tests
| Class | Tests | Coverage |
|---|---|---|
| AlertEngine | 8 | Generate all/category, category validation |
| RiskEngine | 8 | Generate all, by category, summary |
| AnomalyEngine | 8 | Detect all, by domain, confidence |
| TimelineEngine | 8 | Generate all, by type, type validation |

### Service Tests
| Class | Tests | Coverage |
|---|---|---|
| AlertIntelligenceService | 8 | Orchestration, delegation |
| AlertRegistryService | 8 | Query delegation, filtering |
| AlertTelemetryService | 8 | Metric recording, history, reset |

### Infrastructure Tests
| Class | Tests | Coverage |
|---|---|---|
| AlertCacheService | 8 | Cache put/get, TTL expiry, clear, stats |
| InMemoryAlertRepository | 8 | Save, find, delete, query by fields |

### SDK Tests
| Class | Tests | Coverage |
|---|---|---|
| AlertBuilder | 8 | Fluent builder, defaults, validation |
| AlertRuntime | 8 | Query delegation, cache access |

### Controller Tests
| Class | Tests | Coverage |
|---|---|---|
| AlertControllerTest | 17 | CRUD, query, actions, risks, anomalies, timeline, telemetry, cache |

### Context Test
| Class | Tests | Coverage |
|---|---|---|
| AlertServiceApplicationTests | 1 | Spring context loads |

## Running Tests
```bash
mvn test                          # All tests
mvn test -Dtest=AlertEngineTest   # Single test class
mvn test -pl alert-intelligence-service  # Module only
```

## Mocking Strategy
- All engine tests use real engine instances
- Service tests use Mockito mocks for repository/engine
- Controller tests use `@WebMvcTest` with `@WithMockUser`
- Context test uses `@SpringBootTest`
