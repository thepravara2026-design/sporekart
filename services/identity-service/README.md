# Identity Service

This module provides the Sprint 0 engineering foundation for the SporeKart identity service.

## Included foundation

- Spring Boot 3.3.x application bootstrap
- Java 21 Maven build configuration
- Environment-specific configuration for development, test, and production
- Security, validation, cache, Redis, Kafka, OpenAPI, and observability scaffolding
- Flyway migration foundation and logging configuration
- Docker and CI foundation for local infrastructure and build verification

## Local development

1. Start infrastructure with Docker Compose from the repository root:
   - docker compose -f docker/docker-compose.yml up -d
2. Run the service:
   - mvn -f services/identity-service/pom.xml spring-boot:run
3. Verify health endpoint:
   - curl http://localhost:8080/actuator/health
