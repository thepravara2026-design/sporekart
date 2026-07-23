# SporeKart Enterprise AI Platform — Testing Guide

## Prerequisites

- Java 21
- Apache Maven 3.9+
- Docker Desktop (for integration tests requiring containers)

## Running All Tests

```bash
# Run all service tests
mvn clean test -f services/gateway-service/pom.xml
mvn clean test -f services/ai-service/pom.xml
mvn clean test -f copilot-marketplace-service/pom.xml

# Run all copilot service tests
mvn clean test -f customer-copilot-service/pom.xml
mvn clean test -f admin-copilot-service/pom.xml
mvn clean test -f trainer-copilot-service/pom.xml
mvn clean test -f grower-copilot-service/pom.xml
mvn clean test -f bi-copilot-service/pom.xml
mvn clean test -f marketing-copilot-service/pom.xml
mvn clean test -f operations-copilot-service/pom.xml
mvn clean test -f executive-copilot-service/pom.xml
```

## Gateway Service Tests

```bash
cd services/gateway-service

# Run all gateway tests
mvn test

# Run specific certification suites
mvn test -Dtest=SecurityCertificationTest
mvn test -Dtest=PerformanceCertificationTest
mvn test -Dtest=ConfigurationCertificationTest
mvn test -Dtest=ObservabilityCertificationTest

# Run QA certification suite
mvn test -Dtest="com.sporekart.gateway.qa.*"

# Run individual QA tests
mvn test -Dtest=QaMethodValidationTest
mvn test -Dtest=QaJwtSecurityAuditTest
mvn test -Dtest=QaInjectionSecurityTest
mvn test -Dtest=QaCorsSecurityTest
mvn test -Dtest=QaConfigurationAuditTest
mvn test -Dtest=QaConcurrentAccessTest

# Run route certification tests
mvn test -Dtest=MarketplaceRouteCertificationTest
mvn test -Dtest=CopilotRouteCertificationTest

# Run with verbose output
mvn test -X

# Run with surefire report
mvn test surefire-report:report
```

## AI Service Tests

```bash
cd services/ai-service

# Run all AI service tests
mvn test

# Run by module
mvn test -Dtest="com.sporekart.ai.workflow.*"
mvn test -Dtest="com.sporekart.ai.semantic.*"
mvn test -Dtest="com.sporekart.ai.risk.*"
mvn test -Dtest="com.sporekart.ai.prompt.*"
mvn test -Dtest="com.sporekart.ai.provider.*"
mvn test -Dtest="com.sporekart.ai.providerregistry.*"

# Run security architecture tests
mvn test -Dtest="com.sporekart.ai.security.*"

# Run specific test classes
mvn test -Dtest=WorkflowControllerTest
mvn test -Dtest=SemanticSearchServiceImplTest
```

## Copilot Marketplace Service Tests

```bash
cd copilot-marketplace-service

# Run all marketplace tests
mvn test

# Run certification suites
mvn test -Dtest="com.sporekart.marketplace.certification.*"

# Run lifecycle certification
mvn test -Dtest=PluginLifecycleCertificationTest

# Run sandbox isolation certification
mvn test -Dtest=SandboxIsolationCertificationTest

# Run domain model tests
mvn test -Dtest="com.sporekart.marketplace.domain.*"

# Run SDK tests
mvn test -Dtest="com.sporekart.marketplace.sdk.*"

# Run sandbox tests
mvn test -Dtest="com.sporekart.marketplace.sandbox.*"

# Run lifecycle tests
mvn test -Dtest="com.sporekart.marketplace.lifecycle.*"

# Run integration tests
mvn test -Dtest="com.sporekart.marketplace.integration.*"

# Run controller tests
mvn test -Dtest=PluginMarketplaceControllerTest
```

## Running Tests with Coverage

```bash
# Gateway service with JaCoCo
mvn clean test jacoco:report -f services/gateway-service/pom.xml

# Marketplace service with JaCoCo
mvn clean test jacoco:report -f copilot-marketplace-service/pom.xml

# AI service with JaCoCo
mvn clean test jacoco:report -f services/ai-service/pom.xml
```

## Test Profiles

| Profile | Use Case |
|---------|----------|
| `test` | Default test profile (gateway) |
| `integration` | Full integration tests with container dependencies |

## Test Categories

- **Unit Tests** — Isolated class-level tests with mocked dependencies
- **Certification Tests** — End-to-end certification suites validating platform behavior
- **Integration Tests** — Full Spring context tests with @SpringBootTest
- **Security Tests** — Authentication, authorization, JWT validation
- **Performance Tests** — Latency and throughput benchmarks
- **Observability Tests** — Metrics, health, tracing validation

## Test Output

Reports are generated in:
- `services/gateway-service/target/surefire-reports/`
- `copilot-marketplace-service/target/surefire-reports/`
- `services/ai-service/target/surefire-reports/`
