# Risk Framework Runbook

## Health Checks

### API Health Endpoint

```bash
curl -X GET http://localhost:8080/api/v1/risk/health
```

Expected response (200):
```json
{
  "status": "UP",
  "activeAssessments": 3,
  "pendingAssessments": 1,
  "averageRiskScore": 35,
  "averageTrustScore": 72,
  "averageConfidenceScore": 68,
  "cacheHitRate": 0.85,
  "lastEvaluationTimeMs": 180,
  "version": "1.0.0"
}
```

### Health Indicators

| Check | Component | Failure Impact |
|-------|-----------|---------------|
| Assessment engine | RiskEngine | All risk evaluations fail |
| Factor resolution | RiskScoringService | Risk scores unavailable |
| Trust calculator | TrustScoreCalculator | Trust scores unavailable |
| Confidence engine | ConfidenceCalculatorEngine | Confidence scores unavailable |
| Redis connectivity | Cache | Degraded performance |
| Database connectivity | JPA | Assessment persistence unavailable |
| Kafka connectivity | Kafka publisher | Events not published |

### Prometheus Metrics

| Metric | Type | Description |
|--------|------|-------------|
| `risk_assessments_total` | Counter | Total assessments by status |
| `risk_assessment_duration_seconds` | Timer | Assessment duration |
| `risk_scores_by_level` | Counter | Risk scores by level |
| `risk_trust_scores` | Gauge | Current average trust score |
| `risk_confidence_scores` | Gauge | Current average confidence score |
| `risk_factors_identified_total` | Counter | Risk factors by category |
| `risk_recommendations_total` | Counter | Recommendations by type |
| `risk_cache_hit_ratio` | Gauge | Redis cache hit ratio |
| `risk_pending_assessments` | Gauge | Assessments in progress |

### Grafana Alerts

| Alert | Condition | Severity | Action |
|-------|-----------|----------|--------|
| HighRiskRate | `risk_scores_by_level{level="CRITICAL"} > 10/min` | CRITICAL | Immediate incident response |
| MediumRiskSpike | `risk_scores_by_level{level="HIGH"} > 50/min` | WARNING | Investigate factor patterns |
| LowTrustScore | `risk_trust_scores < 40` | WARNING | Review provider/knowledge health |
| LowConfidenceScore | `risk_confidence_scores < 30` | WARNING | Review factor quality |
| CacheHitLow | `risk_cache_hit_ratio < 0.50` | INFO | Review cache TTL config |
| AssessmentBacklog | `risk_pending_assessments > 50` | WARNING | Scale evaluation workers |
| AssessmentFailure | `risk_assessments_total{status="FAILED"} > 5/min` | WARNING | Investigate failures |

## Cache Invalidation

### Manual Cache Clear

```bash
# Clear all risk caches
redis-cli KEYS "risk:*" | xargs redis-cli DEL

# Clear specific namespace
redis-cli DEL "risk:assessment:*"
redis-cli DEL "risk:factors:*"
redis-cli DEL "risk:trust:*"
redis-cli DEL "risk:confidence:*"
redis-cli DEL "risk:config:*"
```

### Automatic Invalidation Events

| Event | Cache Invalidated |
|-------|------------------|
| Assessment created/completed | risk:assessment:* |
| Factor added | risk:factors:* |
| Trust evaluated | risk:trust:* |
| Confidence calculated | risk:confidence:* |
| Config updated | risk:config:* |

## Threshold Configuration

### View Current Thresholds

```bash
curl -s http://localhost:8080/api/v1/risk/configuration/thresholds
```

Response:
```json
{
  "lowMax": 20,
  "mediumMax": 40,
  "highMax": 70,
  "criticalMax": 100
}
```

### Update Thresholds

Requires `AI_ADMINISTRATOR` role.

```bash
curl -X PUT http://localhost:8080/api/v1/risk/configuration/thresholds \
  -H "Content-Type: application/json" \
  -d '{
    "lowMax": 25,
    "mediumMax": 50,
    "highMax": 75,
    "criticalMax": 100
  }'
```

### Update Factor Weights

Requires `AI_ADMINISTRATOR` role.

```bash
curl -X PUT http://localhost:8080/api/v1/risk/configuration/weights \
  -H "Content-Type: application/json" \
  -d '{
    "trust": {
      "PROVIDER_RELIABILITY": 0.15,
      "KNOWLEDGE_QUALITY": 0.15,
      "SEMANTIC_CONFIDENCE": 0.12,
      "PROMPT_VALIDATION": 0.12,
      "HISTORICAL_ACCURACY": 0.12,
      "POLICY_COMPLIANCE": 0.10,
      "WORKFLOW_SUCCESS": 0.08,
      "CONTEXT_COMPLETENESS": 0.08,
      "OUTPUT_VALIDATION": 0.08
    },
    "confidence": {
      "KNOWLEDGE_MATCH": 0.25,
      "SEMANTIC_SIMILARITY": 0.20,
      "PROMPT_QUALITY": 0.18,
      "CONVERSATION_CONTEXT": 0.15,
      "WORKFLOW_SUCCESS": 0.12,
      "PROVIDER_METADATA": 0.10
    }
  }'
```

## Monitoring & Observability

### Key Metrics to Watch

| Metric | Warning | Critical |
|--------|---------|----------|
| Average evaluation time | > 1s | > 5s |
| Cache hit ratio | < 0.60 | < 0.30 |
| Pending assessments | > 20 | > 50 |
| CRITICAL risk rate | > 5/min | > 10/min |
| Average trust score | < 50 | < 30 |
| Average confidence score | < 40 | < 20 |
| Assessment failure rate | > 2/min | > 5/min |

### Logging

```yaml
logging:
  level:
    com.sporekart.ai.risk: DEBUG
    com.sporekart.ai.risk.engine: TRACE
```

### Structured Log Fields

| Field | Description | Example |
|-------|-------------|---------|
| `risk.assessmentId` | Assessment UUID | rsk-abc-123 |
| `risk.status` | Assessment status | COMPLETED |
| `risk.level` | Risk level | MEDIUM |
| `risk.score` | Risk score | 35 |
| `risk.trustScore` | Trust score | 72 |
| `risk.confidenceScore` | Confidence score | 68 |
| `risk.recommendation` | Recommendation | REVIEW |
| `risk.evaluationMs` | Evaluation duration | 245 |
| `risk.factorCount` | Number of factors | 3 |

## Troubleshooting

### Issue: All assessments return CRITICAL

**Possible causes:**
1. Thresholds misconfigured — check `risk.thresholds.*-max` values
2. All risk factors scoring maximum — verify factor evidence sources
3. Weight configuration invalid — verify weights sum to 1.0
4. Empty factor list causing default behavior

**Resolution:**
```bash
# Check threshold configuration
curl -s http://localhost:8080/api/v1/risk/configuration/thresholds

# Check active assessment to see factor scores
curl -s http://localhost:8080/api/v1/risk/assessments?status=COMPLETED | jq '.assessments[0]'

# Reset thresholds to defaults
# Review and update risk factor evaluation logic
```

### Issue: Trust scores consistently low

**Possible causes:**
1. Provider health degraded — check provider monitoring
2. Knowledge sources stale — verify document freshness
3. Weight configuration overweighting low-scoring factors
4. Historical data insufficient for accuracy calculation

**Resolution:**
```bash
# Check factor breakdown for a completed trust evaluation
curl -s http://localhost:8080/api/v1/risk/assess/rsk-abc-123/trust | jq '.factors'

# Verify provider health
curl -s http://localhost:8080/api/v1/ai/providers/health

# Review and adjust factor weights if needed
```

### Issue: Cache miss rate too high

**Possible causes:**
1. Cache TTL too short — review `risk.cache.*-ttl` configuration
2. High rate of assessments — evaluate assessment frequency
3. Redis memory pressure — monitor Redis `used_memory`

**Resolution:**
```yaml
# Adjust cache TTLs
risk:
  cache:
    assessment-ttl: 600    # 10 minutes (was 300)
    factors-ttl: 300        # 5 minutes (was 180)
    trust-ttl: 600          # 10 minutes (was 300)
    confidence-ttl: 600     # 10 minutes (was 300)
```

### Issue: Risk events not publishing to Kafka

**Possible causes:**
1. Kafka broker unavailable — check `kafka.bootstrap-servers`
2. Topic `risk-events` not created — check topic list
3. Serialization error — verify event objects are serializable

**Resolution:**
```bash
# Check Kafka topic exists
kafka-topics --bootstrap-server localhost:9092 --list | grep risk-events

# Check consumer group
kafka-consumer-groups --bootstrap-server localhost:9092 --group risk-group --describe

# Test event publishing
# Enable DEBUG logging for RiskKafkaEventPublisher
```

### Issue: Assessment stuck in IN_PROGRESS

**Possible causes:**
1. Pipeline step hanging — check evaluation timeout
2. External dependency unavailable — provider health, knowledge service
3. Concurrent assessment limit reached

**Resolution:**
```bash
# Find stuck assessments
curl -s "http://localhost:8080/api/v1/risk/assessments?status=IN_PROGRESS&from=2026-07-12T00:00:00Z"

# Cancel stuck assessment (requires AI_ADMINISTRATOR)
curl -X POST http://localhost:8080/api/v1/risk/assessments/{id}/cancel

# Check pipeline configuration
# Verify dependent services are healthy
```

## SLA Targets

| Metric | Target | Measurement |
|--------|--------|-------------|
| Assessment response time (P95) | < 1s | Timer metric |
| Trust evaluation time (P95) | < 500ms | Timer metric |
| Confidence calculation time (P95) | < 500ms | Timer metric |
| Cache hit ratio | > 80% | Gauge metric |
| Risk endpoint uptime | 99.9% | Health check |
| Assessment completion rate | > 99% | Counter ratio |
