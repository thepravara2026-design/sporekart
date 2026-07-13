# Compliance Framework Runbook

## Health Checks

### API Health Endpoint

```bash
curl -X GET http://localhost:8080/api/v1/compliance/health
```

Expected response (200):
```json
{
  "status": "UP",
  "frameworkCount": 6,
  "activeRuleCount": 12,
  "pendingAssessments": 0,
  "cacheHitRate": 0.85,
  "lastEvaluationTimeMs": 180,
  "version": "1.0.0"
}
```

### Health Indicators

| Check | Component | Failure Impact |
|-------|-----------|---------------|
| Framework registry | ComplianceRegistry | All validations fail |
| Rule cache | Redis | Degraded performance |
| Database connectivity | JPA | Assessments/reports unavailable |
| Kafka connectivity | Kafka publisher | Events not published |

### Prometheus Metrics

| Metric | Type | Description |
|--------|------|-------------|
| `compliance_validations_total` | Counter | Total validations by status |
| `compliance_validation_duration_seconds` | Timer | Validation duration |
| `compliance_violations_total` | Counter | Violations by severity |
| `compliance_frameworks_active` | Gauge | Active framework count |
| `compliance_rules_active` | Gauge | Active rule count |
| `compliance_cache_hit_ratio` | Gauge | Redis cache hit ratio |
| `compliance_pending_assessments` | Gauge | Assessments in progress |

### Grafana Alerts

| Alert | Condition | Severity | Action |
|-------|-----------|----------|--------|
| HighViolationRate | `compliance_violations_total > 100/min` | WARNING | Investigate rule patterns |
| CriticalViolation | `violation_severity == "CRITICAL"` | CRITICAL | Immediate incident response |
| LowComplianceRate | `compliance_rate < 0.80` | WARNING | Review framework configuration |
| CacheHitLow | `compliance_cache_hit_ratio < 0.50` | INFO | Review cache TTL config |
| AssessmentBacklog | `compliance_pending_assessments > 50` | WARNING | Scale evaluation workers |
| FrameworkInactive | `framework_status == "INACTIVE"` | INFO | Review framework lifecycle |

## Cache Invalidation

### Manual Cache Clear

```bash
# Clear all compliance caches
redis-cli KEYS "compliance:*" | xargs redis-cli DEL

# Clear specific namespace
redis-cli DEL "compliance:framework:*"
redis-cli DEL "compliance:rules:*"
redis-cli DEL "compliance:assessment:*"
redis-cli DEL "compliance:report:*"
redis-cli DEL "compliance:config:*"
```

### Automatic Invalidation Events

| Event | Cache Invalidated |
|-------|------------------|
| Framework registered/updated | compliance:framework:* |
| Rule created/updated/deleted | compliance:rules:* |
| Assessment completed | compliance:assessment:* |
| Report generated | compliance:report:* |
| Config updated | compliance:config:* |

## Monitoring & Observability

### Key Metrics to Watch

| Metric | Warning | Critical |
|--------|---------|----------|
| Average evaluation time | > 1s | > 5s |
| Cache hit ratio | < 0.60 | < 0.30 |
| Pending assessments | > 20 | > 50 |
| Violation rate (ERROR+) | > 10/min | > 50/min |
| Exception expiry rate | > 5/day | > 20/day |

### Logging

```yaml
logging:
  level:
    com.sporekart.ai.compliance: DEBUG
    com.sporekart.ai.compliance.engine: TRACE  # Rule evaluation details
```

### Structured Log Fields

| Field | Description | Example |
|-------|-------------|---------|
| `compliance.assessmentId` | Assessment UUID | asmt-abc-123 |
| `compliance.frameworkType` | Framework type | GDPR |
| `compliance.status` | Validation status | NON_COMPLIANT |
| `compliance.violationCount` | Number of violations | 2 |
| `compliance.evaluationMs` | Evaluation duration | 245 |
| `compliance.severity` | Max violation severity | ERROR |

## Troubleshooting

### Issue: All validations return NON_COMPLIANT

**Possible causes:**
1. No active rules registered — check `compliance_rules` table
2. Framework is INACTIVE — verify framework status
3. Expression engine failing silently — enable TRACE logging
4. Evidence collection failing — check evidence integrity hashes

**Resolution:**
```bash
# Check active rule count
curl -s http://localhost:8080/api/v1/compliance/health | jq .activeRuleCount

# Check framework status
curl -s http://localhost:8080/api/v1/compliance/frameworks | jq '.frameworks[] | {id, status}'

# Enable debug logging and retry
# Check application logs for evidence collection errors
```

### Issue: Cache miss rate too high

**Possible causes:**
1. Cache TTL too short — review `compliance.cache.*-ttl` configuration
2. High rate of framework/rule updates — evaluate update frequency
3. Redis memory pressure — monitor Redis `used_memory`

**Resolution:**
```yaml
# Adjust cache TTLs
compliance:
  cache:
    framework-ttl: 900    # 15 minutes (was 600)
    rules-ttl: 600        # 10 minutes (was 300)
```

### Issue: Compliance events not publishing to Kafka

**Possible causes:**
1. Kafka broker unavailable — check `kafka.bootstrap-servers`
2. Topic `compliance-events` not created — check topic list
3. Serialization error — verify event objects are serializable

**Resolution:**
```bash
# Check Kafka topic exists
kafka-topics --bootstrap-server localhost:9092 --list | grep compliance-events

# Check consumer group
kafka-consumer-groups --bootstrap-server localhost:9092 --group compliance-group --describe

# Test event publishing
# Enable DEBUG logging for ComplianceKafkaEventPublisher
```

### Issue: Exception approval not taking effect

**Possible causes:**
1. Exception is still REQUESTED — verify status
2. Exception has expired — check `expiresAt`
3. Cache not invalidated — exception rule cache still serving old state

**Resolution:**
```bash
# Check exception status
curl -s http://localhost:8080/api/v1/compliance/exceptions?status=APPROVED

# Invalidate rules cache
redis-cli DEL "compliance:rules:*"

# Verify exception in evaluation context
```

## SLA Targets

| Metric | Target | Measurement |
|--------|--------|-------------|
| Validation response time (P95) | < 500ms | Timer metric |
| Cache hit ratio | > 80% | Gauge metric |
| Compliance endpoint uptime | 99.9% | Health check |
| Report generation time | < 2s | Timer metric |
| Exception resolution time | < 24h | Audit timestamps |
