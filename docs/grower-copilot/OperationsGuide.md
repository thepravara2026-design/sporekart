# Grower Copilot — Operations Guide

## Deployment

### Infrastructure Requirements

| Resource | Minimum | Recommended |
|----------|---------|-------------|
| CPU | 2 cores | 4+ cores |
| RAM | 4 GB | 8 GB |
| Disk | 20 GB SSD | 50 GB SSD |
| Network | 100 Mbps | 1 Gbps |

### Kubernetes Deployment

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: grower-copilot-service
  namespace: sporekart
spec:
  replicas: 2
  selector:
    matchLabels:
      app: grower-copilot
  template:
    metadata:
      labels:
        app: grower-copilot
    spec:
      containers:
        - name: grower-copilot
          image: sporekart/grower-copilot-service:1.2.0
          ports:
            - containerPort: 8102
            - containerPort: 8103  # health
            - containerPort: 8104  # metrics
          env:
            - name: SPRING_PROFILES_ACTIVE
              value: "production"
            - name: AI_PLATFORM_API_KEY
              valueFrom:
                secretKeyRef:
                  name: ai-platform-credentials
                  key: api-key
          livenessProbe:
            httpGet:
              path: /healthz
              port: 8103
            initialDelaySeconds: 30
            periodSeconds: 15
          readinessProbe:
            httpGet:
              path: /readyz
              port: 8103
            initialDelaySeconds: 20
            periodSeconds: 10
          resources:
            requests:
              memory: "2Gi"
              cpu: "500m"
            limits:
              memory: "4Gi"
              cpu: "2"
---
apiVersion: v1
kind: Service
metadata:
  name: grower-copilot-service
  namespace: sporekart
spec:
  selector:
    app: grower-copilot
  ports:
    - name: grpc
      port: 8102
      targetPort: 8102
    - name: health
      port: 8103
      targetPort: 8103
    - name: metrics
      port: 8104
      targetPort: 8104
```

### Environment Variables

| Variable | Required | Description |
|----------|----------|-------------|
| `SPRING_PROFILES_ACTIVE` | No | Active Spring profile |
| `AI_PLATFORM_API_KEY` | Yes | API key for Enterprise AI Platform |
| `WEATHER_PROVIDER` | No | `simulated` or `open-meteo` |
| `KNOWLEDGE_ENDPOINT` | No | Knowledge platform base URL |
| `LOG_LEVEL` | No | `INFO` (default), `DEBUG`, `TRACE` |
| `METRICS_ENABLED` | No | `true` (default) |
| `MAX_CONCURRENT_SESSIONS` | No | `100` (default) |

## Monitoring

### Health Endpoints

| Endpoint | Port | Description |
|----------|------|-------------|
| `/healthz` | 8103 | Liveness probe |
| `/readyz` | 8103 | Readiness probe (checks all dependencies) |
| `/health` | 8102 | Detailed health (API endpoint) |

### Prometheus Metrics

Available at `:8104/metrics`:

| Metric | Type | Labels | Description |
|--------|------|--------|-------------|
| `grower_copilot_requests_total` | Counter | engine, status | Total request count by engine |
| `grower_copilot_request_duration_ms` | Histogram | engine | Request latency distribution |
| `grower_copilot_engine_errors_total` | Counter | engine | Engine execution errors |
| `grower_copilot_llm_invocations_total` | Counter | model | LLM call count |
| `grower_copilot_llm_tokens_total` | Counter | model | Token usage |
| `grower_copilot_active_sessions` | Gauge | — | Currently active sessions |
| `grower_copilot_weather_provider_status` | Gauge | provider | 1 = available, 0 = unavailable |
| `grower_copilot_knowledge_cache_hits` | Counter | — | Knowledge cache hit count |
| `grower_copilot_knowledge_cache_misses` | Counter | — | Knowledge cache miss count |

### Grafana Dashboard Recommendations

Recommended panels:
- **Request rate** (requests/sec) by engine
- **P95/P99 latency** by endpoint
- **Error rate** by engine and error type
- **LLM cost** (estimated tokens × model rate)
- **Active sessions** over time
- **Dependency health** (weather, knowledge, AI platform)

## Logging

- **Format:** JSON structured logging (Logback + Logstash encoder)
- **Destination:** stdout (container), forwarded to ELK/Loki
- **Levels:** INFO (default), DEBUG (troubleshooting), TRACE (engine internals)

### Log Fields

```json
{
  "@timestamp": "2026-07-23T10:00:00.000+00:00",
  "level": "INFO",
  "logger": "com.sporekart.grower.copilot.engine.DiseaseEngine",
  "message": "Disease analysis completed",
  "requestId": "req_abc123",
  "sessionId": "sess_xyz789",
  "engine": "disease",
  "durationMs": 342,
  "disease": "dry_bubble",
  "confidence": 0.91,
  "userId": "usr_456"
}
```

### Log Filtering (Production)

| Event | Level | Sample |
|-------|-------|--------|
| Normal request flow | INFO | "Disease analysis completed" |
| Slow request (>2s) | WARN | "Disease analysis exceeded threshold" |
| Dependency failure | ERROR | "Weather provider unavailable" |
| Escalation triggered | WARN | "Escalation triggered for user usr_456" |
| Authentication failure | WARN | "Invalid token received" |

## Troubleshooting

### Common Issues

| Symptom | Likely Cause | Resolution |
|---------|-------------|------------|
| High latency on `/chat` | LLM timeout / model overload | Check AI Platform status; reduce max_tokens; verify model capacity |
| Engine returns degraded | Dependency unavailable | Check `/health` for which dependency is down; verify network policies |
| Weather data stale | Provider rate limit exceeded | Switch to simulated provider; check provider usage dashboard |
| Knowledge search returning empty | Index not populated | Run `POST /admin/knowledge/reindex`; check knowledge platform logs |
| Memory pressure | High concurrent sessions | Scale horizontally; reduce session TTL; check memory limits |
| gRPC connection refused | AI Platform not running | Verify pod status; check network policy; verify port configuration |

### Debugging

```bash
# Check pod status
kubectl get pods -n sporekart -l app=grower-copilot

# View logs
kubectl logs -n sporekart -l app=grower-copilot --tail=100

# Port-forward for local debugging
kubectl port-forward -n sporekart pod/grower-copilot-xxx 8102:8102

# Enable debug logging (runtime)
kubectl exec -n sporekart deploy/grower-copilot -- \
  curl -X POST localhost:8102/admin/log-level?logger=com.sporekart.grower.copilot.engine&level=DEBUG

# Trigger a health check with full details
curl -X GET localhost:8102/health | jq .
```

### Common HTTP Error Codes and Fixes

| Status | Meaning | Likely Fix |
|--------|---------|------------|
| 401 | Unauthenticated | Verify JWT token is valid and not expired |
| 422 | Validation error | Check request payload against OpenAPI schema |
| 429 | Rate limited | Reduce request frequency or upgrade tier |
| 503 | Dependency down | Check `/health` and resolve dependency |

## Backup and Recovery

- **Knowledge base:** Daily automated snapshots (retained 30 days)
- **Configuration:** Stored in Git (infrastructure/config repo)
- **Session cache:** Ephemeral (Redis); no backup required
- **Recovery time objective (RTO):** < 5 minutes (with hot standby)
- **Recovery point objective (RPO):** Near-zero (stateless service)

## Scaling

- **Horizontal:** Increase replica count (stateless design)
- **Vertical:** Increase CPU/memory per pod
- **LLM capacity:** Provision additional model deployment throughput via Enterprise AI Platform
- **Knowledge cache:** Increase Redis cluster size for high query volume

## Runbook Summary

| Scenario | Action | SLA |
|----------|--------|-----|
| Pod crash | Kubernetes auto-restart (CrashLoopBackOff → manual intervention after 3 restarts) | < 1 min |
| High latency (>3s P99) | Scale replicas; check LLM capacity | < 5 min |
| Dependency failure | Failover to degraded mode; page on-call engineer | < 2 min |
| Data corruption | Restore from last knowledge base snapshot | < 30 min |
| Security incident | Isolate pod; revoke credentials; forensics | < 15 min |
