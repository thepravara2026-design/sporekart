# tests

## Testing Guide

### Unit Tests

Located in `src/test/java/com/sporekart/admin/`. Run with Maven:

```bash
mvn test
```

### Integration Tests

Located in `src/test/java/com/sporekart/admin/interfaces/rest/`. Uses `@SpringBootTest` with H2 in-memory database.

### Test Configuration

Tests use `application-test.yml` (if present) or the default `application.yml` with H2 database. No external services required.

### Code Coverage

Generate coverage report:

```bash
mvn verify -Pcoverage
```

Coverage reports are written to `target/site/jacoco/`.
