# Deployment Configuration Guide

## Local Development

```bash
# Start infrastructure
docker compose up -d postgres redis kafka

# Run service
cd services/ai-service
mvn spring-boot:run -Dspring-boot.run.profiles=local

# Or with Maven
mvn clean install
java -jar target/ai-service-0.1.0-SNAPSHOT.jar --spring.profiles.active=local
```

## Docker Compose

```bash
# Build and start
docker compose up --build

# With custom profile
SPRING_PROFILES_ACTIVE=docker docker compose up
```

## Production (Cloud)

```bash
# Build
mvn clean package -Pprod

# Environment variables must be set
export AI_DATABASE_URL=jdbc:postgresql://...
export AI_DB_USERNAME=...
export AI_DB_PASSWORD=...
export AI_REDIS_HOST=...
export AI_REDIS_PORT=6379
export AI_REDIS_PASSWORD=...
export AI_KAFKA_SERVERS=...

# Run
java -jar target/ai-service-0.1.0-SNAPSHOT.jar --spring.profiles.active=prod,cloud
```

## Configuration Profiles Summary

| File | Profile | When to Use |
|------|---------|-------------|
| application.yml | (default) | Base configuration for all environments |
| application-local.yml | local | Developer workstation |
| application-dev.yml | dev | Shared development server |
| application-test.yml | test | Automated test execution |
| application-stage.yml | stage | Pre-production validation |
| application-prod.yml | prod | Live production |
| application-docker.yml | docker | Docker Compose deployment |
| application-cloud.yml | cloud | Cloud/container orchestration |

## Configuration Validation

Configuration is validated at startup:
- Missing required fields → startup failure
- Invalid URLs → startup failure
- Invalid timeout values → startup failure
- Unknown feature flags → warning
- Duplicate provider names → warning
