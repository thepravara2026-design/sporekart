# Decision Engine Runbook

## Health Checks

### Endpoint
```
GET /api/v1/decisions/health
```

### Expected Response
```json
{
  "status": "UP",
  "service": "decision-engine",
  "timestamp": 1720800000000,
  "details": {
    "metrics": { "totalDecisions": 42, "allowed": 30, "denied": 8, ... }
  }
}
```

### Health Indicators
- `status`: Always `"UP"` in current implementation
- `mode`: `"DEVELOPMENT"` in current implementation
- Metrics are always returned; no component health checks

## Decision Evaluation

### Evaluate a Request
```bash
curl -X POST http://localhost:8080/api/v1/decisions/evaluate \
  -H "Content-Type: application/json" \
  -d '{
    "module": "content",
    "action": "GENERATE",
    "payload": {"contentType": "product_description"},
    "userId": "user-123",
    "roles": ["editor"]
  }'
```

### Get Decision by ID
```bash
curl http://localhost:8080/api/v1/decisions/550e8400-e29b-41d4-a716-446655440000
```

### Get Statistics
```bash
curl http://localhost:8080/api/v1/decisions/statistics
```

### Replay a Decision
```bash
curl -X POST http://localhost:8080/api/v1/decisions/replay \
  -H "Content-Type: application/json" \
  -d '{"requestId": "550e8400-e29b-41d4-a716-446655440000"}'
```

## Explanation Management

Explanations are automatically generated during evaluation. To retrieve:

### Get Explanations
```bash
curl http://localhost:8080/api/v1/decisions/explanations
curl "http://localhost:8080/api/v1/decisions/explanations?decisionId=550e8400-e29b-41d4-a716-446655440000"
```

**Note:** Currently returns empty array (stub).

## Cache Management

### Redis Cache Namespaces

| Namespace | Prefix | TTL | Cleared When |
|-----------|--------|-----|--------------|
| Decision Result | `decision:result:` | 300s | TTL expiry or `invalidateAll()` |
| Metadata | `decision:metadata:` | 300s | TTL expiry |
| Registry | `decision:registry:` | 300s | TTL expiry |
| Statistics | `decision:stats:` | 120s | TTL expiry |
| Explanation | `decision:explanation:` | 300s | TTL expiry |

### Invalidate All Caches
```bash
# Trigger cache invalidation via DecisionRedisCacheService.invalidateAll()
redis-cli KEYS "decision:*" | xargs redis-cli DEL
```

## Configuration

Configuration is stored in-memory via `DecisionConfigurationServiceImpl` (ConcurrentHashMap).

### Get Config Value
```bash
# Not exposed via REST — use application code:
# decisionConfigurationService.getConfig("key")
```

### Set Config Value
```bash
# Not exposed via REST — use application code:
# decisionConfigurationService.setConfig("key", "value", "description")
```

### Default Configuration
From `DecisionConfig` @ConfigurationProperties:
- `default-action`: ALLOW
- `default-conflict-strategy`: DENY_OVERRIDES
- Cache TTLs: result 300s, metadata 300s, registry 300s, stats 120s, explanation 300s
- Kafka topic: `decision-events` (3 partitions, replication factor 1)

**Note:** Configuration is lost on restart.

## Monitoring

### Micrometer Metrics

| Metric Name | Type | Description |
|-------------|------|-------------|
| `decision.total` | Counter | Total decisions evaluated |
| `decision.allowed` | Counter | ALLOW decisions |
| `decision.denied` | Counter | DENY/BLOCK_REQUEST decisions |
| `decision.escalated` | Counter | ESCALATE_TO_ADMIN decisions |
| `decision.approvals` | Counter | REQUIRE_APPROVAL decisions |
| `decision.conflicts` | Counter | Conflict resolutions |
| `decision.replays` | Counter | Decision replays |
| `decision.evaluation.time` | Timer | Decision evaluation duration |
| `decision.cache.hits` | Gauge | Cache hit count |
| `decision.cache.misses` | Gauge | Cache miss count |

### View Metrics
```bash
# Via Micrometer /actuator endpoint
curl http://localhost:8080/actuator/metrics/decision.total
curl http://localhost:8080/actuator/metrics/decision.evaluation.time
```

## Kafka Events

Topic: `decision-events` (3 partitions, replication factor 1)

### Event Types

| Event | When Published |
|-------|----------------|
| `DecisionEvaluated` | After POST /evaluate responds |
| `DecisionAllowed` | When decision action is ALLOW |
| `DecisionDenied` | When decision action is DENY |
| `DecisionEscalated` | When decision action is ESCALATE_TO_ADMIN |
| `DecisionExplanationGenerated` | After explanation creation |
| `DecisionAuditCreated` | After audit record creation |
| `DecisionReplayStarted` | At start of replay |
| `DecisionReplayCompleted` | At end of replay |

### Event Payload Structure
```json
{
  "id": "uuid",
  "type": "DecisionEvaluated",
  "timestamp": "2026-07-12T10:30:00Z",
  "source": "decision",
  "details": {
    "requestId": "uuid",
    "action": "ALLOW",
    "status": "ALLOWED",
    "timeMs": 12
  }
}
```

## Troubleshooting

### Problem: Evaluation returns unexpected action
- Check that `DecisionConfig.default-action` is set correctly
- Verify `ai_decision_rules` table has active rules with correct priority/weight
- Empty rules default to ALLOW
- Conflict resolution defaults to DENY_OVERRIDES

### Problem: Metrics show zero
- Metrics are in-memory AtomicLong counters
- Reset on application restart — no persistence
- Verify `POST /evaluate` is being called

### Problem: Configuration changes not persisting
- `DecisionConfigurationServiceImpl` uses in-memory ConcurrentHashMap
- All configuration is lost on restart
- No DB-backed or file-backed persistence

### Problem: Cache not working
- Verify Redis is running and accessible
- Check `DecisionRedisCacheService` is properly injected
- Default TTLs: 120-300 seconds
- No automatic cache warming

### Problem: Kafka events not publishing
- Verify Kafka is running and `decision-events` topic exists
- Check `KafkaConfig.decisionEventsTopic()` is configured with correct partitions/replication
- Events are fire-and-forget (no retry or DLQ in current implementation)

### Problem: replay returns null
- `DecisionEngineImpl.replay()` is a stub — logs the request, records metrics, returns null
- Not yet implemented for actual replay logic
