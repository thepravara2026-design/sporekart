# Copilot Service Operations Runbook

> **Version:** 0.2.0-SNAPSHOT | **Service:** `copilot-service` | **Port:** 8098

---

## 1. Service Startup and Shutdown

### 1.1 Prerequisites

Before starting the copilot service, ensure the following dependencies are healthy:

| Dependency | Port | Health Check |
|------------|------|--------------|
| Redis | 6379 | `redis-cli ping` → `PONG` |
| Identity Service | 8081 | `GET /actuator/health` |
| AI Service | 8088 | `GET /actuator/health` |
| Memory Service | 8092 | `GET /actuator/health` |
| Gateway Service | 8080 | `GET /actuator/health` |

### 1.2 Starting the Service

```bash
# Development mode
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Production mode
java -jar copilot-service.jar --spring.profiles.active=prod

# Docker
docker run -d \
  --name copilot-service \
  --network sporekart \
  -p 8098:8098 \
  -e SPRING_PROFILES_ACTIVE=prod \
  sporekart/copilot-service:0.2.0-SNAPSHOT
```

### 1.3 Stopping the Service

```bash
# Graceful shutdown (Spring Boot Actuator)
curl -X POST http://localhost:8098/actuator/shutdown

# Docker
docker stop copilot-service
docker rm copilot-service

# Kubernetes
kubectl delete pod -l app=copilot-service
```

### 1.4 Startup Verification Checklist

- [ ] Service starts without errors
- [ ] `GET /api/copilot/health` returns `{"status": "UP"}`
- [ ] Gateway can route to copilot: `GET /api/copilot/health` via gateway
- [ ] Redis session store is connected
- [ ] AI service is reachable
- [ ] Default personas are loaded
- [ ] Default capabilities are registered

---

## 2. Health Check Endpoints

### 2.1 Service Health

```
GET /api/copilot/health
```

**Response:**
```json
{
  "status": "UP",
  "timestamp": "2026-07-23T10:30:00Z",
  "version": "0.2.0-SNAPSHOT",
  "components": {
    "ai-service": { "status": "UP", "details": { "latency": "45ms" } },
    "memory-service": { "status": "UP" },
    "redis": { "status": "UP", "details": { "connectedClients": 12 } }
  }
}
```

### 2.2 Copilot Instance Health

```
GET /api/copilot/{id}/health
```

**Response:**
```json
{
  "copilotId": "660e8400-e29b-41d4-a716-446655440001",
  "status": "UP",
  "uptime": "7d 3h 12m",
  "latency": "245ms",
  "memoryUsage": "256 MB",
  "activeSessions": 5,
  "lastError": null,
  "timestamp": "2026-07-23T10:30:00Z"
}
```

### 2.3 Gateway Health

```
GET /actuator/health (gateway-service)
```

The gateway health reflects the copilot service health as a downstream dependency.

---

## 3. Monitoring and Alerting

### 3.1 Key Metrics

| Metric | Type | Description | Warning Threshold | Critical Threshold |
|--------|------|-------------|-------------------|--------------------|
| `copilot.requests.total` | Counter | Total chat requests | — | — |
| `copilot.requests.active` | Gauge | Currently active requests | > 50 | > 100 |
| `copilot.requests.latency` | Histogram | Request latency (ms) | > 2000 (p99) | > 5000 (p99) |
| `copilot.requests.errors` | Counter | Error rate | > 1% | > 5% |
| `copilot.requests.rate` | Gauge | Requests per second | > 50 | > 100 |
| `copilot.streaming.active` | Gauge | Active streaming connections | > 20 | > 50 |
| `copilot.sessions.active` | Gauge | Active sessions | > 500 | > 1000 |
| `copilot.sessions.created` | Counter | Sessions created | — | — |
| `copilot.tokens.total` | Counter | Total tokens processed | — | — |
| `copilot.tokens.prompt` | Counter | Prompt tokens | — | — |
| `copilot.tokens.completion` | Counter | Completion tokens | — | — |
| `copilot.tools.invoked` | Counter | Tool invocations | — | — |
| `copilot.capabilities.invoked` | Counter | Capability invocations | — | — |
| `copilot.cache.hit.ratio` | Gauge | Context cache hit ratio | < 0.5 | < 0.3 |
| `copilot.upstream.ai.latency` | Histogram | AI service latency | > 3000 (p99) | > 8000 (p99) |
| `copilot.upstream.ai.errors` | Counter | AI service errors | > 1% | > 5% |
| `copilot.jvm.memory.used` | Gauge | JVM heap usage | > 70% | > 85% |
| `copilot.jvm.threads` | Gauge | Active threads | > 100 | > 200 |

### 3.2 Prometheus Queries

```promql
# Request rate (last 5 minutes)
rate(copilot_requests_total[5m])

# P99 latency
histogram_quantile(0.99, rate(copilot_requests_latency_seconds_bucket[5m]))

# Error rate
rate(copilot_requests_errors_total[5m]) / rate(copilot_requests_total[5m]) * 100

# Active sessions
copilot_sessions_active

# Upstream AI service latency (p99)
histogram_quantile(0.99, rate(copilot_upstream_ai_latency_seconds_bucket[5m]))
```

### 3.3 Alerting Rules

```yaml
groups:
  - name: copilot-service
    rules:
      - alert: CopilotHighErrorRate
        expr: rate(copilot_requests_errors_total[5m]) / rate(copilot_requests_total[5m]) > 0.05
        for: 5m
        labels: { severity: critical }
        annotations:
          summary: "Copilot service error rate > 5%"

      - alert: CopilotHighLatency
        expr: histogram_quantile(0.99, rate(copilot_requests_latency_seconds_bucket[5m])) > 5
        for: 5m
        labels: { severity: warning }
        annotations:
          summary: "Copilot service p99 latency > 5s"

      - alert: CopilotAIDown
        expr: copilot_upstream_ai_latency_seconds_count == 0
        for: 1m
        labels: { severity: critical }
        annotations:
          summary: "AI service is not responding"

      - alert: CopilotHighMemoryUsage
        expr: jvm_memory_used_bytes{area="heap"} / jvm_memory_max_bytes{area="heap"} > 0.85
        for: 5m
        labels: { severity: warning }
        annotations:
          summary: "JVM heap usage > 85%"

      - alert: CopilotSessionsExhausted
        expr: copilot_sessions_active > 1000
        for: 2m
        labels: { severity: warning }
        annotations:
          summary: "Active sessions exceeded 1000"
```

---

## 4. Logging and Debugging

### 4.1 Log Levels

| Package | Default | Debug | Trace |
|---------|---------|-------|-------|
| `com.sporekart.copilot` | INFO | DEBUG | TRACE |
| `com.sporekart.copilot.engine` | INFO | DEBUG | TRACE |
| `com.sporekart.copilot.streaming` | INFO | DEBUG | TRACE |
| `com.sporekart.copilot.context` | INFO | DEBUG | TRACE |
| `org.springframework.cloud.gateway` | WARN | DEBUG | TRACE |

### 4.2 Changing Log Levels at Runtime

```bash
# Set DEBUG for the copilot engine
curl -X POST http://localhost:8098/actuator/loggers/com.sporekart.copilot.engine \
  -H "Content-Type: application/json" \
  -d '{"configuredLevel": "DEBUG"}'

# Reset to default
curl -X POST http://localhost:8098/actuator/loggers/com.sporekart.copilot.engine \
  -H "Content-Type: application/json" \
  -d '{"configuredLevel": null}'
```

### 4.3 Log Format

```
2026-07-23 10:30:00.123 [http-nio-8098-exec-1] INFO  c.s.c.e.ChatEngine - Processing chat request
  traceId=abc123def456 spanId=xyz789 sessionId=550e8400-... userId=user-123 copilotId=660e8400-...
  message="What is my order status?"
```

### 4.4 Debugging Common Issues

#### 4.4.1 Failed Context Assembly

```bash
# Enable debug logging for context assembly
curl -X POST http://localhost:8098/actuator/loggers/com.sporekart.copilot.context \
  -H "Content-Type: application/json" \
  -d '{"configuredLevel": "DEBUG"}'

# Reproduce the issue
curl -X POST http://localhost:8080/api/copilot/context \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"sessionId": "<id>", "includeSources": ["orders"]}'

# Check logs for source-by-source assembly results
```

#### 4.4.2 Streaming Issues

```bash
# Check active streaming connections
curl -X GET http://localhost:8098/actuator/metrics/copilot.streaming.active

# Test streaming directly (bypass gateway)
curl -X POST http://localhost:8098/api/copilot/stream \
  -H "Content-Type: application/json" \
  -H "Accept: text/event-stream" \
  -d '{"message": "Hello", "copilotId": "<id>"}'
```

#### 4.4.3 AI Service Errors

```bash
# Check AI service health
curl -X GET http://localhost:8088/actuator/health

# Check AI service logs
kubectl logs -l app=ai-service --tail=100

# Increase copilot timeout for AI calls
# application.yml:
# sporekart.copilot.upstream.ai.timeout: 60s
```

---

## 5. Performance Tuning

### 5.1 JVM Configuration

```bash
java -jar copilot-service.jar \
  -Xms2g -Xmx4g \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=100 \
  -XX:ParallelGCThreads=4 \
  -XX:ConcGCThreads=2 \
  -Djava.security.egd=file:/dev/./urandom
```

### 5.2 Thread Pool Configuration

**application.yml:**
```yaml
sporekart:
  copilot:
    thread-pool:
      chat:
        core-size: 10
        max-size: 50
        queue-capacity: 200
      streaming:
        core-size: 20
        max-size: 100
        queue-capacity: 500
      context-assembly:
        core-size: 10
        max-size: 30
        queue-capacity: 100
```

### 5.3 Connection Pooling

```yaml
spring:
  redis:
    lettuce:
      pool:
        min-idle: 5
        max-idle: 20
        max-active: 50
        max-wait: 500ms
```

### 5.4 Caching

```yaml
sporekart:
  copilot:
    cache:
      context:
        enabled: true
        ttl: 30s
        max-size: 1000
      persona:
        enabled: true
        ttl: 5m
        max-size: 100
      capability:
        enabled: true
        ttl: 5m
        max-size: 200
```

### 5.5 Performance Benchmarks

| Operation | Target | Acceptable | Unacceptable |
|-----------|--------|------------|--------------|
| Chat (simple) | < 500ms | < 1s | > 3s |
| Chat (with context) | < 2s | < 5s | > 10s |
| Streaming (first token) | < 200ms | < 500ms | > 1s |
| Session creation | < 50ms | < 100ms | > 200ms |
| Context assembly | < 1s | < 3s | > 5s |
| Health check | < 50ms | < 100ms | > 200ms |

---

## 6. Capacity Planning

### 6.1 Current Capacity

| Metric | Per Instance | 3 Instances |
|--------|-------------|-------------|
| Max chat requests/min | 600 | 1,800 |
| Max streaming connections | 100 | 300 |
| Max active sessions | 1,000 | 3,000 |
| Max tokens/min | 500,000 | 1,500,000 |

### 6.2 Scaling Triggers

| Metric | Scale Up | Scale Down |
|--------|----------|------------|
| CPU utilization | > 70% for 5 min | < 30% for 10 min |
| Memory utilization | > 80% for 5 min | < 50% for 10 min |
| Request latency (p99) | > 3s for 5 min | < 1s for 10 min |
| Active sessions | > 800 | < 200 |

### 6.3 Resource Estimation per 1M Requests

| Resource | Estimate |
|----------|----------|
| CPU | 8 vCPU-hours |
| Memory | 16 GB-hours |
| Network | 2 GB |
| Redis memory | 500 MB (session data) |

---

## 7. Disaster Recovery

### 7.1 Backup Strategy

| Data | Backup Frequency | Retention | Method |
|------|-----------------|-----------|--------|
| Session data | Real-time | 24 hours | Redis persistence (RDB + AOF) |
| Copilot registrations | Real-time | 7 days | Database replication |
| Audit logs | 1 hour | 90 days | Log shipping to S3/Blob |
| Configuration | Per deployment | Permanent | Git (infrastructure repo) |

### 7.2 Recovery Procedures

#### 7.2.1 Service Crash

```bash
# 1. Check logs for crash reason
kubectl logs -l app=copilot-service --previous --tail=50

# 2. Restart the service
kubectl rollout restart deployment/copilot-service

# 3. Verify health
kubectl wait --for=condition=ready pod -l app=copilot-service --timeout=60s

# 4. Verify gateway routing
curl -X GET http://localhost:8080/api/copilot/health
```

#### 7.2.2 Redis Failure

```bash
# 1. Check Redis health
redis-cli ping

# 2. If Redis down, sessions become non-persistent (in-memory only)
#    New sessions will work but existing sessions are lost

# 3. Restart Redis
kubectl rollout restart deployment/redis

# 4. Verify session recovery
curl -X GET http://localhost:8098/api/copilot/session/<id>
```

#### 7.2.3 AI Service Failure

```yaml
# Fallback strategy: copilot operates in degraded mode
sporekart.copilot.upstream.ai.fallback:
  enabled: true
  mode: cached-response  # or: error-message, static-fallback
  cache-ttl: 1h
  static-response: "I'm sorry, the AI service is temporarily unavailable."
```

#### 7.2.4 Full Region Failure

```yaml
# Multi-region deployment strategy
sporekart.copilot.multi-region:
  enabled: true
  primary: us-east
  secondary: us-west
  failover:
    auto: true
    health-threshold: 3
    cooldown: 5m
```

### 7.3 Runbook: Service Degraded

1. **Detect**: Alert triggers for high error rate or latency
2. **Assess**: Check `GET /api/copilot/health` for component status
3. **Isolate**: Identify the failing component (AI service, Redis, etc.)
4. **Mitigate**: Apply fallback strategy or scale up instances
5. **Resolve**: Fix the failing component
6. **Verify**: Confirm health returns to `UP`
7. **Post-mortem**: Document root cause and preventive measures

---

## 8. Common Troubleshooting Scenarios

### 8.1 "401 Unauthorized" on Copilot Requests

**Symptoms:** All copilot API requests return 401.

**Causes:**
- Expired or invalid JWT token
- Identity service is down
- JWK set URI is misconfigured
- Clock skew between services

**Resolution:**
```bash
# 1. Verify token validity
curl -X POST http://localhost:8081/api/auth/validate \
  -H "Authorization: Bearer <token>"

# 2. Check identity service health
curl -X GET http://localhost:8081/actuator/health

# 3. Check JWK set endpoint
curl -X GET http://localhost:8081/.well-known/jwks.json

# 4. Verify clock sync
w32tm /query /status
```

### 8.2 "Copilot Not Found" 404

**Symptoms:** Requests to a specific copilot ID return 404.

**Causes:**
- Copilot was never registered
- Copilot was deregistered or disabled
- Wrong copilot ID in the request
- Gateway routing to wrong service

**Resolution:**
```bash
# 1. List all registered copilots
curl -X GET http://localhost:8080/api/copilot/list \
  -H "Authorization: Bearer <token>"

# 2. Check if copilot exists directly on copilot service
curl -X GET http://localhost:8098/api/copilot/list

# 3. Verify gateway is routing correctly
curl -X GET http://localhost:8080/api/copilot/health

# 4. Re-register if needed
```

### 8.3 Streaming Connection Drops

**Symptoms:** SSE stream disconnects mid-response.

**Causes:**
- Gateway timeout
- AI service timeout
- Network proxy timeout
- Client disconnect

**Resolution:**
```yaml
# Increase gateway timeout for streaming
sporekart:
  gateway:
    routing:
      service-timeout: 120s

# Increase AI service timeout
sporekart:
  copilot:
    upstream:
      ai:
        timeout: 60s
```

### 8.4 High Memory Usage

**Symptoms:** JVM heap consistently above 80%, GC pauses increasing.

**Causes:**
- Large context sizes
- Session memory leak
- Too many concurrent streaming connections
- Insufficient heap

**Resolution:**
```bash
# 1. Check memory usage
curl -X GET http://localhost:8098/actuator/metrics/jvm.memory.used

# 2. Check active sessions
curl -X GET http://localhost:8098/actuator/metrics/copilot.sessions.active

# 3. Reduce context max tokens
# application.yml: sporekart.copilot.context.max-tokens: 2048

# 4. Force GC
curl -X POST http://localhost:8098/actuator/gc

# 5. Scale up or add instances
```

### 8.5 Slow Chat Responses

**Symptoms:** Chat responses take > 5 seconds.

**Causes:**
- Context assembly is slow (one or more sources timing out)
- AI service is overloaded
- Model response generation is slow
- Rate limiting is throttling

**Resolution:**
```bash
# 1. Check request latency breakdown in logs
# Look for: contextAssemblyTime, llmTime, totalTime

# 2. Test AI service directly
curl -X POST http://localhost:8088/api/ai/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Hello"}'

# 3. Check rate limiter metrics
curl -X GET http://localhost:8098/actuator/metrics/copilot.requests.rate

# 4. Reduce max context tokens
# 5. Switch to a faster model (gpt-4o-mini vs gpt-4o)
```

### 8.6 Session State Loss

**Symptoms:** Session history is lost across requests.

**Causes:**
- Redis connection issue
- Session TTL expired
- Session was deleted
- Service restart without persistence

**Resolution:**
```bash
# 1. Check Redis connectivity
redis-cli ping

# 2. Verify session exists
redis-cli keys "session:*"

# 3. Check session TTL configuration
# application.yml: sporekart.copilot.session.ttl: 60m

# 4. Verify Redis persistence config
redis-cli config get save
redis-cli config get appendonly
```

### 8.7 Rate Limiting Issues

**Symptoms:** `429 Rate Limited` errors.

**Causes:**
- Client exceeding rate limits
- Misconfigured rate limiter
- Rate limiter key collision

**Resolution:**
```bash
# 1. Check current rate limit state
curl -X GET http://localhost:8098/actuator/metrics/copilot.requests.rate

# 2. Increase per-copilot rate limit
curl -X PUT http://localhost:8080/api/copilot/<id>/configure \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{"rateLimitPerMinute": 200}'

# 3. Check rate limiter configuration
# application.yml: sporekart.gateway.rate-limiter
```
