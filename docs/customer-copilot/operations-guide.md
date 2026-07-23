# Customer Copilot Operations Guide

**Version:** 0.2.0
**Last Updated:** 2026-07-23

---

## 1. Service Startup & Shutdown

### Startup

```bash
# Production (JAR)
java -jar customer-copilot-service-0.2.0.jar \
  --spring.profiles.active=prod

# Docker
docker run -d \
  --name customer-copilot \
  -p 8099:8099 \
  -e SPRING_PROFILES_ACTIVE=prod \
  sporekart/customer-copilot-service:0.2.0

# Docker Compose
docker-compose up -d customer-copilot
```

### Shutdown

```bash
# Graceful shutdown (Spring Boot Actuator)
curl -X POST http://localhost:8099/actuator/shutdown

# Docker
docker stop customer-copilot

# Docker Compose
docker-compose stop customer-copilot
```

The service supports graceful shutdown with:
- In-flight requests completed within 30-second timeout
- Active sessions are terminated
- Unprocessed requests return 503

### Startup Checks

```bash
# Check service is running
curl -s http://localhost:8099/actuator/health | jq .status
# "UP"

# Check registered capabilities (logs)
docker logs customer-copilot | grep "capability registered"
# 15 capabilities registered

# Check downstream connectivity
curl -s http://localhost:8099/api/v1/copilot/customer/health | jq
```

---

## 2. Health Check Endpoints

### Actuator Health

**`GET /actuator/health`**

```json
{
  "status": "UP",
  "components": {
    "diskSpace": { "status": "UP" },
    "ping": { "status": "UP" }
  }
}
```

### Custom Health

**`GET /api/v1/copilot/customer/health`**

```json
{
  "status": "UP",
  "service": "customer-copilot-service",
  "version": "0.2.0",
  "uptime": 3600,
  "dependencies": {
    "catalog-service":      { "status": "UP",  "latencyMs": 45 },
    "order-service":        { "status": "UP",  "latencyMs": 32 },
    "knowledge-platform":   { "status": "UP",  "latencyMs": 120 },
    "training-service":     { "status": "UP",  "latencyMs": 28 },
    "identity-service":     { "status": "UP",  "latencyMs": 15 }
  }
}
```

### Health Status Values

| Status | Meaning |
|---|---|
| `UP` | Service and all dependencies healthy |
| `DEGRADED` | Service healthy, one or more non-critical dependencies unavailable |
| `DOWN` | Service or critical dependency unhealthy |

### Actuator Endpoints

| Endpoint | Description |
|---|---|
| `/actuator/health` | Health check |
| `/actuator/info` | Service info (version, build) |
| `/actuator/metrics` | Application metrics |
| `/actuator/metrics/http.server.requests` | HTTP request metrics |
| `/actuator/metrics/jvm.memory.used` | JVM memory usage |
| `/actuator/loggers` | Log level configuration |
| `/actuator/env` | Environment properties |
| `/actuator/threaddump` | Thread dump |

---

## 3. Monitoring Metrics

### Key Metrics

| Metric | Type | Description | Alert Threshold |
|---|---|---|---|
| `http.server.requests` | Counter | HTTP request count | - |
| `http.server.requests.p99` | Timer | P99 response latency | > 2000ms |
| `copilot.chat.requests` | Counter | Total chat requests | - |
| `copilot.chat.errors` | Counter | Chat error count | > 5% error rate |
| `copilot.stream.sessions` | Gauge | Active streaming sessions | > 100 |
| `copilot.recommendation.latency` | Timer | Recommendation latency | > 500ms |
| `copilot.downstream.catalog.latency` | Timer | Catalog service latency | > 1000ms |
| `copilot.downstream.knowledge.latency` | Timer | Knowledge platform latency | > 2000ms |
| `jvm.memory.used` | Gauge | JVM heap usage | > 80% |
| `jvm.threads.live` | Gauge | Live threads | > 200 |
| `process.cpu.usage` | Gauge | CPU usage | > 80% |

### Prometheus Integration

If Micrometer and Prometheus are configured:

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  metrics:
    export:
      prometheus:
        enabled: true
```

Metrics available at `/actuator/prometheus`.

### Sample Prometheus Alert Rules

```yaml
groups:
  - name: customer-copilot
    rules:
      - alert: HighErrorRate
        expr: rate(copilot_chat_errors_total[5m]) / rate(copilot_chat_requests_total[5m]) > 0.05
        for: 5m
        labels:
          severity: critical

      - alert: HighLatency
        expr: histogram_quantile(0.99, rate(http_server_requests_seconds_bucket[5m])) > 2
        for: 5m
        labels:
          severity: warning

      - alert: DownstreamDegraded
        expr: copilot_downstream_knowledge_latency_seconds > 2
        for: 2m
        labels:
          severity: warning
```

---

## 4. Logging Configuration

### Default Logging (JSON format)

```yaml
logging:
  level:
    com.sporekart.customer.copilot: INFO
    com.sporekart.copilot: INFO
    org.springframework: WARN
  pattern:
    console: '{"timestamp":"%d{ISO8601}","level":"%p","thread":"%t","logger":"%c","message":"%m","correlationId":"%X{correlationId}"}%n'
  file:
    name: /var/log/customer-copilot/application.json
    max-size: 100MB
    max-history: 30
```

### Log Levels

| Package | Default | Production |
|---|---|---|
| `com.sporekart.customer.copilot` | DEBUG | INFO |
| `com.sporekart.copilot` | DEBUG | INFO |
| `org.springframework.security` | DEBUG | WARN |
| `org.springframework.web` | DEBUG | WARN |
| `org.hibernate.SQL` | DEBUG | WARN |

Change live via Actuator:

```bash
curl -X POST http://localhost:8099/actuator/loggers/com.sporekart.customer.copilot \
  -H "Content-Type: application/json" \
  -d '{"configuredLevel": "DEBUG"}'
```

### Log Fields

| Field | Description |
|---|---|
| `timestamp` | ISO 8601 timestamp |
| `level` | Log level (INFO, WARN, ERROR) |
| `logger` | Logger class name |
| `message` | Log message |
| `correlationId` | Correlation ID for request tracing |
| `sessionId` | Customer copilot session ID |
| `userId` | Authenticated user ID |
| `intent` | Detected user intent |
| `latencyMs` | Operation duration |

### Sensitive Data Masking

The following fields are masked in logs:
- Email addresses (masked: `r***@example.com`)
- Phone numbers (masked: `+91-XXXXX-XX123`)
- JWT tokens (omitted entirely)
- Credit card info (omitted entirely)
- Passwords (omitted entirely)

---

## 5. Performance Tuning

### JVM Tuning

```bash
# Production JVM options
java -Xms1g -Xmx2g \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=100 \
  -XX:+ParallelRefProcEnabled \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=/var/log/customer-copilot/heapdump.hprof \
  -jar customer-copilot-service-0.2.0.jar
```

### Connection Pool Tuning

```yaml
spring:
  threads:
    virtual:
      enabled: true  # Java 21 virtual threads
  servlet:
    multipart:
      max-request-size: 10MB
```

### HTTP Client Tuning (for downstream calls)

```yaml
customer:
  copilot:
    client:
      connect-timeout: 2000
      read-timeout: 5000
      max-connections: 200
      max-connections-per-route: 50
      connection-ttl: 30000
```

### Thread Pool

| Pool | Default | Production |
|---|---|---|
| CopilotEngine workers | 10 | 50 |
| Recommendation workers | 4 | 20 |
| Streaming emitter threads | 4 | 30 |
| Downstream HTTP connections | 20 | 200 |

---

## 6. Cache Management

### Cache Stores

| Cache | TTL | Max Size | Backend |
|---|---|---|---|
| Session store | Session lifetime | 10,000 | In-memory (ConcurrentHashMap) |
| Profile cache | 1 hour | 50,000 | In-memory |
| Recommendation cache | 30 minutes | 10,000 | In-memory |
| Product cache | 5 minutes | 5,000 | In-memory |

### Clearing Caches

```bash
# If cached API is exposed:
curl -X POST http://localhost:8099/actuator/caches/customer-profiles/clear
curl -X POST http://localhost:8099/actuator/caches/recommendations/clear
```

### Cache Eviction Policies

- **Session cache**: LRU (least recently used)
- **Profile cache**: TTL-based (1 hour)
- **Recommendation cache**: TTL-based (30 minutes), invalidated on profile change
- **Product cache**: TTL-based (5 minutes)

---

## 7. Troubleshooting Guide

### Issue: "Copilot not responding"

**Symptoms:**
- Chat endpoint returns 502 or timeout
- CopilotPanel shows "Service unavailable"
- Health check shows dependencies as DOWN

**Checks:**
```bash
# 1. Is the service running?
curl http://localhost:8099/actuator/health

# 2. Is the Copilot Service reachable?
curl -s http://copilot-service:8090/actuator/health

# 3. Check logs for connectivity errors
docker logs customer-copilot --tail=100 | grep -i "connection refused\|timeout\|copilot-service"

# 4. Check network connectivity
ping copilot-service
```

**Resolution:**
| Cause | Fix |
|---|---|
| Copilot Service down | Restart `copilot-service` |
| Network partition | Check DNS resolution and network policies |
| Connection pool exhausted | Increase `max-connections` |
| OOM/killed | Check JVM heap usage, increase memory |

### Issue: "Recommendations empty"

**Symptoms:**
- Recommend endpoint returns empty array
- Chat recommendations show "No recommendations available"
- Logged warning: "No products found for recommendation"

**Checks:**
```bash
# 1. Check catalog service availability
curl http://catalog-service:8083/actuator/health

# 2. Check catalog has products in the requested category
curl http://catalog-service:8083/products?category=mushroom-spawn

# 3. Check recommendation strategy logs
docker logs customer-copilot --tail=100 | grep -i "recommendation\|strategy.*score"

# 4. Verify customer has profile data
# (Check CustomerContextService profile store)
```

**Resolution:**
| Cause | Fix |
|---|---|
| Catalog service down | Restart `catalog-service` |
| No products match criteria | Broaden search filters or add products |
| Customer cold start | Use context-aware/seasonal fallback strategies |
| Strategy weights sum != 1.0 | Verify ensemble weights total 1.0 |

### Issue: "Knowledge not grounded"

**Symptoms:**
- Responses contain speculation or disclaimers
- Missing citations in knowledge responses
- "I don't have information about that" responses

**Checks:**
```bash
# 1. Check Knowledge Platform health
curl http://ai-service:8095/actuator/health

# 2. Test knowledge retrieval directly
curl -X POST http://ai-service:8095/api/v1/knowledge/retrieve \
  -H "Content-Type: application/json" \
  -d '{"query": "how to grow shiitake", "language": "en"}'

# 3. Check knowledge documents exist
curl http://ai-service:8095/api/v1/knowledge/categories

# 4. Check grounding logs
docker logs customer-copilot --tail=100 | grep -i "grounding\|citation\|knowledge"
```

**Resolution:**
| Cause | Fix |
|---|---|
| Knowledge Platform unavailable | Restart `ai-service` |
| No relevant documents ingested | Add documents via Knowledge Platform API |
| Query mismatch | Reformulate query or expand document coverage |
| RAG chunking issues | Re-chunk documents with appropriate size |
| Language mismatch | Ensure documents exist in requested language |

### Issue: "SSE streaming not working"

**Symptoms:**
- SSE connection drops
- Partial responses received
- Browser console shows SSE errors

**Checks:**
```bash
# 1. Test with curl
curl -N -X POST http://localhost:8099/api/v1/copilot/customer/stream \
  -H "Content-Type: application/json" \
  -d '{"message":"test","sessionId":"sess-test"}'

# 2. Check streaming is enabled in config
grep "streaming-enabled" application.yml

# 3. Check gateway proxy timeouts
# (Gateway may have lower timeouts than SSE requires)
```

**Resolution:**
| Cause | Fix |
|---|---|
| `streaming-enabled: false` | Set to `true` in config |
| Gateway timeout too low | Increase gateway `proxy-read-timeout` to 60s |
| Proxy buffering enabled | Disable proxy buffering for SSE endpoints |
| Client disconnected | Add SSE reconnect logic on client |

### Issue: "Authentication failures"

**Symptoms:**
- 401 responses on all requests
- "Invalid token" errors
- "Token expired" errors

**Checks:**
```bash
# 1. Test with a known-valid token
curl -I http://localhost:8099/api/v1/copilot/customer/health
# (health should return 200 without auth)

# 2. Verify token with Identity Service
curl http://identity-service:8081/v1/auth/session/validate \
  -H "Authorization: Bearer <token>"

# 3. Check token expiry
# (Decode JWT and check exp claim)
```

**Resolution:**
| Cause | Fix |
|---|---|
| Token expired | Refresh token via Identity Service |
| Invalid audience | Ensure token includes `customer-copilot` audience |
| Missing Authorization header | Add `Authorization: Bearer <token>` |
| Wrong signing key | Verify Identity Service public key matches |

---

## 8. Backup & Recovery

### Configuration Backup

```bash
# Backup application config
cp /app/config/application.yml /backup/customer-copilot-config-$(date +%Y%m%d).yml
```

### Recovery Procedure

1. **Service crash**: Auto-restart via Docker `restart: unless-stopped` or Kubernetes liveness probe
2. **Data loss**: In-memory session data is ephemeral — no persistent data to recover
3. **Configuration corruption**: Restore from backup, restart service
4. **Dependency failure**: Circuit breakers will degrade gracefully; restore dependency first

---

## 9. Capacity Planning

| Metric | Per 1K Requests | Peak (100 RPM) |
|---|---|---|
| CPU | 2 CPU-seconds | ~0.3 cores |
| Memory | 50 MB | ~500 MB |
| Network (in) | 500 KB | ~50 KB/s |
| Network (out) | 10 MB | ~1 MB/s |
| Downstream calls | 3,000 (3 per request) | ~5/sec |

**Scaling formula:** `replicas = ceil(peakRPM / (60 * maxRPS))` where `maxRPS ≈ 25` per instance.
