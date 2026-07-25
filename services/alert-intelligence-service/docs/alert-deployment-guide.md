# Alert Deployment Guide

## Prerequisites
- Java 21+
- Maven 3.9+
- Docker (optional)

## Build
```bash
cd services/alert-intelligence-service
mvn clean package -DskipTests
```

## Run (local)
```bash
mvn spring-boot:run
# Service starts on http://localhost:8095
```

## Run (JAR)
```bash
java -jar target/alert-intelligence-service-1.0.0.jar
```

## Run (Docker)
```dockerfile
FROM openjdk:21-jdk-slim
COPY target/alert-intelligence-service-1.0.0.jar app.jar
EXPOSE 8095
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

```bash
docker build -t alert-intelligence-service .
docker run -p 8095:8095 alert-intelligence-service
```

## Configuration
Key properties in `application.yml`:
| Property | Default | Description |
|---|---|---|
| server.port | 8095 | HTTP port |
| app.cache.ttl-minutes | 60 | Default cache TTL |
| app.data.seed-on-startup | true | Seed sample data |

## Health Check
```bash
curl http://localhost:8095/api/v1/alerts/health
# Expected: {"status":200,"data":{"status":"UP"},"message":"Service is healthy"}
```

## Verification
```bash
# Generate seed data
curl -X POST http://localhost:8095/api/v1/alerts/generate

# List alerts
curl http://localhost:8095/api/v1/alerts/

# Check telemetry
curl http://localhost:8095/api/v1/alerts/telemetry/metrics
```

## Monitoring
- Health endpoint: `/api/v1/alerts/health`
- Metrics: `/api/v1/alerts/telemetry/metrics`
- Cache stats: `/api/v1/alerts/cache/stats`
