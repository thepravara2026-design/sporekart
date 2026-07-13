# Policy Engine Runbook

## Health Checks

### Endpoint Health

```
GET /api/v1/policies/health
```

Expected response:
```json
{
  "status": "UP",
  "service": "policy-engine",
  "timestamp": 1712345678000,
  "details": {
    "metrics": {
      "evaluationCount": 42,
      "violationCount": 7,
      "cacheHits": 15,
      "cacheMisses": 3,
      "activations": 5,
      "deactivations": 2,
      "averageEvaluationTimeMs": 12.5
    }
  }
}
```

### Troubleshooting Health

If health endpoint returns 500:
1. Check `PolicyController.handleException` — all unhandled exceptions return RFC 9457 error
2. Check `PolicyMonitoringService` is properly initialized (requires `MeterRegistry` bean)
3. Verify database connectivity for repositories used by metrics

## Evaluation Pipeline

### Running an Evaluation

```bash
curl -X POST /api/v1/policies/evaluate \
  -H "Content-Type: application/json" \
  -d '{"module":"content","action":"generate","payload":{},"userId":"user-1","roles":["editor"]}'
```

### Expected Decisions

| Scenario | Decision |
|----------|----------|
| No policies match | ALLOW (default) |
| Policy matches, no violations | ALLOW |
| BLOCKING or CRITICAL violation | DENY |
| ERROR violation | REVIEW |
| WARNING violation | LOG |
| All violations INFO | ALLOW |

### Troubleshooting Evaluations

| Symptom | Likely Cause | Action |
|---------|-------------|--------|
| Unexpected ALLOW | Policy not active, not in ACTIVE status, or module mismatch | Check policy status and module filter |
| Unexpected DENY | BLOCKING or CRITICAL violation from matched rule | Check rule expression and severity |
| Empty policy list | No policies defined or all soft-deleted | Check `ai_policies` table |
| High evaluation time | Too many policies or rules matching | Check number of active policies |

## Cache Management

### Cache Namespaces

| Prefix | TTL | Content |
|--------|-----|---------|
| `policy:registry:` | 300s | Policy registry entries |
| `policy:compiled:` | 600s | Compiled policy expressions |
| `policy:metadata:` | 300s | Policy metadata |
| `policy:evaluation:` | 180s | Evaluation results |
| `policy:health:` | 60s | Health check responses |

### Cache Invalidation

Full invalidation:
```bash
curl -X POST /api/v1/policies/reload
```

This calls `PolicyRedisCacheService.invalidateAll()` which deletes all `policy:*` keys.

Selective eviction is not supported — invalidation always clears all namespaces.

### Monitoring Cache Performance

Metrics available via health endpoint:
- `cacheHits` — total cache hits since service start
- `cacheMisses` — total cache misses since service start
- Track hit ratio: `cacheHits / (cacheHits + cacheMisses)`

## Monitoring

### Micrometer Metrics (PolicyMonitoringService)

| Metric Name | Type | Description |
|-------------|------|-------------|
| `policy.evaluations.total` | Counter | Total evaluations performed |
| `policy.violations.total` | Counter | Total violations detected |
| `policy.decisions.allowed` | Counter | ALLOW/BYPASS decisions |
| `policy.decisions.denied` | Counter | DENY/CHALLENGE decisions |
| `policy.decisions.review` | Counter | REVIEW decisions |
| `policy.activations.total` | Counter | Policy activations |
| `policy.deactivations.total` | Counter | Policy deactivations |
| `policy.evaluation.time` | Timer | Evaluation duration distribution |
| `policy.registry.size` | Gauge | Number of registered policies |
| `policy.cache.hits` | Gauge | Cache hit counter |
| `policy.cache.misses` | Gauge | Cache miss counter |

### Grafana Recommendations

Create panels for:
- Evaluation rate (per minute): `rate(policy.evaluations.total[5m])`
- Decision distribution: `policy.decisions.*` breakdown
- P95 evaluation latency: `policy.evaluation.time.percentile(0.95)`
- Cache hit ratio: `policy.cache.hits / (policy.cache.hits + policy.cache.misses)`
- Violation rate: `rate(policy.violations.total[5m])`

## Kafka Events

### Topic Configuration

- Topic name: `policy-events`
- Partitions: 3
- Replication factor: 1
- Configured in: `KafkaConfig.policyEventsTopic()`

### Event Types (8)

| Event | Trigger | Key Fields |
|-------|---------|-----------|
| PolicyCreated | POST /api/v1/policies | policyId, name |
| PolicyUpdated | PUT /api/v1/policies/{id} | policyId, name |
| PolicyDeleted | DELETE /api/v1/policies/{id} | policyId |
| PolicyActivated | Lifecycle activation | policyId |
| PolicyDeactivated | Lifecycle deactivation | policyId |
| PolicyEvaluated | POST /api/v1/policies/evaluate | requestId, decision, evaluationTimeMs |
| PolicyViolationDetected | Evaluation with violations | requestId, ruleName, severity |
| PolicyEvaluationFailed | Evaluation error | requestId, error |

### Troubleshooting Kafka

| Symptom | Likely Cause | Action |
|---------|-------------|--------|
| Events not published | KafkaTemplate not configured | Check `spring.kafka.bootstrap-servers` |
| Serialization errors | ObjectMapper configuration | Check Jackson config for date/time formats |
| Missing events | Topic not created | Check `KafkaConfig` beans |

## Configuration

### Feature Flags (application.yml)

```yaml
ai:
  features:
    policy-enabled: true        # Master enable/disable
    policy-caching: true        # Redis caching
    policy-audit: true          # Audit logging
    policy-monitoring: true     # Metrics collection
    policy-evaluation: true     # Evaluation pipeline
```

### PolicyConfig Properties

```yaml
policy:
  enabled: true
  default-decision: ALLOW
  default-conflict-strategy: DENY_OVERRIDES
  cache:
    registry-ttl-seconds: 300
    compiled-ttl-seconds: 600
    metadata-ttl-seconds: 300
    evaluation-ttl-seconds: 180
    health-ttl-seconds: 60
  kafka:
    topic: policy-events
    partitions: 3
    replication-factor: 1
```

## Troubleshooting

### Common Issues

| Problem | Diagnosis | Solution |
|---------|-----------|----------|
| Policies not evaluating | Check `policy-enabled` feature flag | Set to `true` |
| Evaluations always return ALLOW | No active policies with matching module | Create policy with matching module |
| Redis cache not working | Check `policy-caching` flag | Set to `true` |
| Kafka events missing | Check topic configuration | Verify `kafka.bootstrap-servers` |
| Audit records not created | Check `policy-audit` flag | Set to `true` |
| Health shows errors | PolicyMonitoringService initialization | Ensure MeterRegistry bean exists |

### Restart Recovery

On service restart:
1. In-memory configuration (`PolicyConfigurationServiceImpl`) is lost — reconfigure if needed
2. Redis caches are cold — first evaluations will be cache misses
3. Kafka offsets reset based on consumer group configuration
4. Database audits are persistent — all historical audits survive restart
