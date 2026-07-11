# health

## Health Endpoints

Spring Boot Actuator exposes the following health-related endpoints:

| Endpoint | Description |
|----------|-------------|
| `GET /actuator/health` | Liveness and readiness probes |
| `GET /actuator/info` | Application metadata |
| `GET /actuator/prometheus` | Prometheus metrics (if enabled) |

### Docker Healthcheck

The Dockerfile does not include a built-in healthcheck. For production deployments, configure a healthcheck at the orchestrator level:

```yaml
healthcheck:
  test: ["CMD", "curl", "-f", "http://localhost:8089/actuator/health"]
  interval: 30s
  timeout: 10s
  retries: 3
```

### Readiness and Liveness

- **Readiness**: The `/actuator/health/readiness` endpoint reports when the service is ready to accept traffic (DB, Redis, Kafka connections established).
- **Liveness**: The `/actuator/health/liveness` endpoint reports whether the service is alive and functioning internally.
